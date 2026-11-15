package servicio;

import conexion.ConexionBD;
import dao.CultivoDao;
import dao.InventarioDao;
import dao.PartidaDao;
import dao.PlantacionDao;
import excepciones.AccionNoPermitidaException;
import excepciones.StockInsuficienteException;
import modelo.Cultivo;
import modelo.Inventario;
import modelo.Partida;
import modelo.Plantacion;
import modelo.enums.EstadoPlantacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.time.LocalDateTime;

/**
 * Gestiona la lógica principal relacionada con los cultivos de la granja.
 * Coordina plantaciones, semillas, cosechas, experiencia y avance del tiempo.
 */
public class GranjaServicio {

    public static final int MINUTOS_PLANTAR = 15;
    public static final int MINUTOS_COSECHAR = 15;

    // Unidades que da cada cosecha. Se multiplica por el nivel del terreno.
    public static final int CANTIDAD_COSECHA = 1;

    private final PlantacionDao plantacionDao;
    private final CultivoDao cultivoDao;
    private final InventarioDao inventarioDao;
    private final PartidaDao partidaDao;


    public GranjaServicio() {
        this.plantacionDao = new PlantacionDao();
        this.cultivoDao = new CultivoDao();
        this.inventarioDao = new InventarioDao();
        this.partidaDao = new PartidaDao();
    }


    // Devuelve lo que hay plantado en cada parcela.
    public Map<Integer, Plantacion> plantacionesPorParcela(Partida partida)
            throws SQLException {

        Map<Integer, Plantacion> plantaciones = new HashMap<>();

        for (Plantacion plantacion :
                plantacionDao.listarPorPartida(partida.getIdPartida())) {

            plantaciones.put(
                    plantacion.getNumeroParcela(),
                    plantacion
            );
        }

        return plantaciones;
    }


    /*
     * Recupera los cultivos relacionados con las plantaciones.
     * Si varias parcelas tienen el mismo cultivo, ese cultivo se consulta una sola vez.
     */
    public Map<Integer, Cultivo> cultivosDe(
            Map<Integer, Plantacion> plantaciones
    ) throws SQLException {

        Map<Integer, Cultivo> cultivos = new HashMap<>();

        for (Plantacion plantacion : plantaciones.values()) {

            int idCultivo =
                    plantacion.getIdCultivo();

            if (!cultivos.containsKey(idCultivo)) {

                cultivos.put(
                        idCultivo,
                        cultivoDao.consultarPorId(idCultivo)
                );
            }
        }

        return cultivos;
    }


    // Devuelve únicamente los cultivos para los que el jugador tiene semillas disponibles.
    public Map<Cultivo, Integer> semillasDisponibles(
            Partida partida
    ) throws SQLException {

        Map<Cultivo, Integer> semillas =
                new LinkedHashMap<>();

        for (Cultivo cultivo : cultivoDao.listar()) {

            if (cultivo.getIdItemSemilla() == null) {
                continue;
            }

            Inventario inventario =
                    inventarioDao.consultarPorPartidaEItem(
                            partida.getIdPartida(),
                            cultivo.getIdItemSemilla()
                    );

            if (inventario != null
                    && inventario.getCantidad() > 0) {

                semillas.put(
                        cultivo,
                        inventario.getCantidad()
                );
            }
        }

        return semillas;
    }


    /*
     * Planta un cultivo en una parcela.
     * En una misma transacción descuenta la semilla, crea la plantación
     * y guarda el avance del tiempo.
     */
    public void plantar(
            Partida partida,
            Cultivo cultivo,
            int numeroParcela
    ) throws SQLException,
            AccionNoPermitidaException,
            StockInsuficienteException {

        if (numeroParcela < 1
                || numeroParcela > partida.getParcelasDesbloqueadas()) {

            throw new AccionNoPermitidaException(
                    "La parcela "
                            + numeroParcela
                            + " no esta desbloqueada."
            );
        }

        if (plantacionDao.consultarPorParcela(
                partida.getIdPartida(),
                numeroParcela
        ) != null) {

            throw new AccionNoPermitidaException(
                    "La parcela "
                            + numeroParcela
                            + " ya esta ocupada."
            );
        }

        if (cultivo.getIdItemSemilla() == null) {

            throw new AccionNoPermitidaException(
                    "Ese cultivo no tiene semilla asociada."
            );
        }

        Inventario semillas =
                inventarioDao.consultarPorPartidaEItem(
                        partida.getIdPartida(),
                        cultivo.getIdItemSemilla()
                );

        int disponibles =
                (semillas == null)
                        ? 0
                        : semillas.getCantidad();

        if (disponibles < 1) {
            throw new StockInsuficienteException(
                    1,
                    disponibles
            );
        }

        long minutoAnterior = partida.getMinutoJuegoTotal();
        LocalDateTime fechaGuardadoAnterior = partida.getFechaGuardado();

        Plantacion nueva =
                new Plantacion(
                        partida.getIdPartida(),
                        cultivo.getId(),
                        numeroParcela,
                        minutoAnterior
                );

        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                semillas.setCantidad(
                        disponibles - 1
                );

                if (!inventarioDao.modificar(
                        con,
                        semillas
                )) {

                    throw new SQLException(
                            "No se pudo descontar la semilla."
                    );
                }

                if (!plantacionDao.alta(
                        con,
                        nueva
                )) {

                    throw new SQLException(
                            "No se pudo guardar la plantacion."
                    );
                }

                partida.avanzarMinutos(
                        MINUTOS_PLANTAR
                );

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar el avance del tiempo."
                    );
                }

                con.commit();

            } catch (SQLException e) {
                con.rollback();

                // La base volvio atras, asi que el objeto en memoria tambien.
                partida.setMinutoJuegoTotal(minutoAnterior);

                partida.setFechaGuardado(
                        fechaGuardadoAnterior
                );

                throw e;
            }
        }
    }


    /*
     * Devuelve las parcelas que ya están listas para cosechar.
     * TreeMap mantiene las claves ordenadas para mostrarlas de menor a mayor.
     */
    public Map<Integer, Cultivo> parcelasListas(
            Partida partida
    ) throws SQLException {

        Map<Integer, Plantacion> plantaciones =
                plantacionesPorParcela(partida);

        Map<Integer, Cultivo> cultivos =
                cultivosDe(plantaciones);

        Map<Integer, Cultivo> listas =
                new TreeMap<>();

        for (Plantacion plantacion :
                plantaciones.values()) {

            Cultivo cultivo =
                    cultivos.get(
                            plantacion.getIdCultivo()
                    );

            long minutosRestantes =
                    plantacion.minutosRestantes(
                            cultivo,
                            partida.getMinutoJuegoTotal()
                    );

            if (minutosRestantes == 0) {

                /*
                 * Si terminó de crecer pero todavía figura CRECIENDO,
                 * se sincroniza el estado de la plantación con la base.
                 */
                if (plantacion.getEstado()
                        == EstadoPlantacion.CRECIENDO) {

                    plantacion.setEstado(
                            EstadoPlantacion.LISTA
                    );

                    plantacionDao.modificar(
                            plantacion
                    );
                }

                listas.put(
                        plantacion.getNumeroParcela(),
                        cultivo
                );
            }
        }

        return listas;
    }


    /*
     * Cosecha una parcela.
     * En una sola transacción marca la plantación como COSECHADA,
     * suma el producto al inventario, otorga experiencia
     * y guarda el avance del tiempo.
     */
    public ResultadoCosecha cosechar(
            Partida partida,
            int numeroParcela
    ) throws SQLException,
            AccionNoPermitidaException {

        Plantacion plantacion =
                plantacionDao.consultarPorParcela(
                        partida.getIdPartida(),
                        numeroParcela
                );

        if (plantacion == null) {

            throw new AccionNoPermitidaException(
                    "La parcela "
                            + numeroParcela
                            + " esta vacia."
            );
        }

        Cultivo cultivo =
                cultivoDao.consultarPorId(
                        plantacion.getIdCultivo()
                );

        long restante =
                plantacion.minutosRestantes(
                        cultivo,
                        partida.getMinutoJuegoTotal()
                );

        if (restante > 0) {

            throw new AccionNoPermitidaException(
                    cultivo.getNombre()
                            + " todavia no esta listo (faltan "
                            + restante
                            + " minutos)."
            );
        }

        if (cultivo.getIdItemCosecha() == null) {

            throw new AccionNoPermitidaException(
                    "Ese cultivo no tiene item de cosecha asociado."
            );
        }

        // El terreno de nivel 2 produce el doble de unidades.
        int cantidad =
                CANTIDAD_COSECHA
                        * partida.getNivelTerreno();

        int experiencia =
                cultivo.getExperienciaRecompensa();

        Inventario cosechaGuardada =
                inventarioDao.consultarPorPartidaEItem(
                        partida.getIdPartida(),
                        cultivo.getIdItemCosecha()
                );

        /*
         * Guardamos el estado previo de la partida para restaurarlo
         * si la transacción falla y MySQL realiza rollback.
         */
        int nivelAnterior =
                partida.getNivel();

        int experienciaAnterior =
                partida.getExperiencia();

        long minutoAnterior =
                partida.getMinutoJuegoTotal();

        LocalDateTime fechaGuardadoAnterior =
                partida.getFechaGuardado();

        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                plantacion.setEstado(
                        EstadoPlantacion.COSECHADA
                );

                if (!plantacionDao.modificar(
                        con,
                        plantacion
                )) {

                    throw new SQLException(
                            "No se pudo actualizar la plantacion."
                    );
                }

                // Si nunca tuvo ese item se crea la fila; si ya la tenía, se suma.
                if (cosechaGuardada == null) {

                    if (!inventarioDao.alta(
                            con,
                            new Inventario(
                                    partida.getIdPartida(),
                                    cultivo.getIdItemCosecha(),
                                    cantidad
                            )
                    )) {

                        throw new SQLException(
                                "No se pudo guardar la cosecha."
                        );
                    }

                } else {

                    cosechaGuardada.setCantidad(
                            cosechaGuardada.getCantidad()
                                    + cantidad
                    );

                    if (!inventarioDao.modificar(
                            con,
                            cosechaGuardada
                    )) {

                        throw new SQLException(
                                "No se pudo actualizar la cosecha."
                        );
                    }
                }

                boolean subioNivel =
                        partida.sumarExperiencia(
                                experiencia
                        );

                partida.avanzarMinutos(
                        MINUTOS_COSECHAR
                );

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar la partida."
                    );
                }

                con.commit();

                return new ResultadoCosecha(
                        cultivo.getNombre(),
                        cantidad,
                        experiencia,
                        subioNivel
                );

            } catch (SQLException e) {
                con.rollback();

                // La base volvio atras, asi que el objeto en memoria tambien.
                partida.setNivel(nivelAnterior);
                partida.setExperiencia(experienciaAnterior);
                partida.setMinutoJuegoTotal(minutoAnterior);

                partida.setFechaGuardado(
                        fechaGuardadoAnterior
                );

                throw e;
            }
        }
    }


    // Avanza la partida hasta la mañana siguiente y guarda el nuevo tiempo.
    public void dormir(
            Partida partida
    ) throws SQLException {

        long minutoAnterior =
                partida.getMinutoJuegoTotal();

        partida.dormir();

        try {

            if (!partidaDao.modificar(partida)) {

                throw new SQLException(
                        "No se pudo guardar el avance del tiempo."
                );
            }

        } catch (SQLException e) {

            // Si no se puede guardar, se restaura también el tiempo del objeto en memoria.
            partida.setMinutoJuegoTotal(
                    minutoAnterior
            );

            throw e;
        }
    }

}