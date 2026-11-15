package servicio;

import conexion.ConexionBD;
import dao.InventarioDao;
import dao.ItemDao;
import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import modelo.Inventario;
import modelo.Item;
import modelo.Partida;
import modelo.enums.Rareza;
import util.Reloj;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public class RecoleccionServicio {

    // Cada accion de recoleccion hace avanzar una hora el reloj del juego.
    public static final int MINUTOS_RECOLECTAR = 120;

    // IDs fijos de los recursos cargados en la tabla item.
    private static final int ID_MADERA = 46;
    private static final int ID_PIEDRA = 47;
    private static final int[] IDS_PECES = {48, 49, 50, 51, 52};

    private final InventarioDao inventarioDao;
    private final ItemDao itemDao;
    private final PartidaDao partidaDao;


    public RecoleccionServicio() {
        this.inventarioDao = new InventarioDao();
        this.itemDao = new ItemDao();
        this.partidaDao = new PartidaDao();
    }

    //No se puede talar,picar ni pescar de 20:00 a 5:59.
    private void validarHorario(Partida partida) throws AccionNoPermitidaException{
        Reloj reloj = new Reloj(partida.getMinutoJuegoTotal());
        if(reloj.esDeNoche()){
            throw new AccionNoPermitidaException("Es de noche. Se puede recolectar de 06:00 a 20:00 (dormi para que sea de dia).");
        }
    }


    public ResultadoRecoleccion talar(Partida partida)
            throws SQLException, AccionNoPermitidaException {

        validarHorario(partida);
        // La rareza del hacha define cuantas unidades de madera obtiene.
        int cantidad = partida.getRarezaHacha().getUnidadesRecoleccion();

        return recolectar(partida, ID_MADERA, cantidad);
    }


    public ResultadoRecoleccion picar(Partida partida)
            throws SQLException, AccionNoPermitidaException {

        validarHorario(partida);
        // La rareza del pico define cuantas unidades de piedra obtiene.
        int cantidad = partida.getRarezaPico().getUnidadesRecoleccion();

        return recolectar(partida, ID_PIEDRA, cantidad);
    }


    public ResultadoRecoleccion pescar(Partida partida, Random random)
            throws SQLException, AccionNoPermitidaException {

        validarHorario(partida);
        /*
         * Random se recibe por parametro para poder controlar
         * los resultados durante las pruebas.
         */
        if (random == null) {
            throw new IllegalArgumentException(
                    "Random no puede ser null."
            );
        }

        int probabilidad =
                probabilidadPesca(partida.getRarezaCania());

        // nextInt(100) devuelve un valor entre 0 y 99.
        if (random.nextInt(100) < probabilidad) {

            /*
             * Si pica un pez, se elige uno al azar
             * entre los IDs de peces disponibles.
             */
            int idPez =
                    IDS_PECES[
                            random.nextInt(IDS_PECES.length)
                            ];

            return recolectar(
                    partida,
                    idPez,
                    1
            );
        }

        /*
         * Aunque no pique ningun pez, la accion igual consume tiempo.
         * Guardo el minuto anterior para restaurarlo si falla la BD.
         */
        long minutoAnterior =
                partida.getMinutoJuegoTotal();

        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            /*
             * Desactivo el autoCommit para que el avance del tiempo
             * forme parte de una transaccion controlada.
             */
            con.setAutoCommit(false);

            try {

                partida.avanzarMinutos(
                        MINUTOS_RECOLECTAR
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

                return new ResultadoRecoleccion(
                        "nada",
                        0
                );

            } catch (SQLException e) {

                /*
                 * Si falla la base, deshago los cambios en MySQL
                 * y tambien restauro el objeto Partida en memoria.
                 */
                con.rollback();

                partida.setMinutoJuegoTotal(
                        minutoAnterior
                );

                throw e;
            }
        }
    }


    private ResultadoRecoleccion recolectar(
            Partida partida,
            int idItem,
            int cantidad
    ) throws SQLException {

        /*
         * Busco el Item para obtener su nombre.
         * Ese nombre se devuelve luego al menu.
         */
        Item item =
                itemDao.consultarPorId(idItem);

        if (item == null) {
            throw new SQLException(
                    "No existe el item de recoleccion con ID "
                            + idItem
                            + "."
            );
        }

        /*
         * Puede que la partida nunca haya tenido este recurso,
         * por eso la fila de inventario puede ser null.
         */
        Inventario fila =
                inventarioDao.consultarPorPartidaEItem(
                        partida.getIdPartida(),
                        idItem
                );

        // Guardo el tiempo actual por si la transaccion falla.
        long minutoAnterior =
                partida.getMinutoJuegoTotal();

        /*
         * Inventario y tiempo se actualizan dentro de la misma
         * transaccion para que no pueda guardarse uno sin el otro.
         */
        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);

            try {

                if (fila == null) {

                    // Si nunca tuvo el item, creo una nueva fila.
                    if (!inventarioDao.alta(
                            con,
                            new Inventario(
                                    partida.getIdPartida(),
                                    idItem,
                                    cantidad
                            )
                    )) {

                        throw new SQLException(
                                "No se pudo guardar el recurso recolectado."
                        );
                    }

                } else {

                    // Si ya existe, solamente aumento su cantidad.
                    fila.setCantidad(
                            fila.getCantidad()
                                    + cantidad
                    );

                    if (!inventarioDao.modificar(
                            con,
                            fila
                    )) {

                        throw new SQLException(
                                "No se pudo actualizar el recurso recolectado."
                        );
                    }
                }

                /*
                 * La recoleccion consume tiempo y ese cambio
                 * tambien debe guardarse dentro de la transaccion.
                 */
                partida.avanzarMinutos(
                        MINUTOS_RECOLECTAR
                );

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar el avance del tiempo."
                    );
                }

                /*
                 * Solo confirmo la transaccion cuando inventario
                 * y partida se actualizaron correctamente.
                 */
                con.commit();

                return new ResultadoRecoleccion(
                        item.getNombre(),
                        cantidad
                );

            } catch (SQLException e) {

                /*
                 * Si cualquier consulta falla, vuelvo la base
                 * al estado anterior.
                 */
                con.rollback();

                /*
                 * El rollback afecta MySQL, pero no modifica
                 * automaticamente el objeto Java.
                 * Por eso restauramos tambien el minuto.
                 */
                partida.setMinutoJuegoTotal(
                        minutoAnterior
                );

                throw e;
            }
        }
    }


    private int probabilidadPesca(Rareza rareza) {

        // Una mejor rareza de cania aumenta la posibilidad de pescar.
        return switch (rareza) {
            case COMUN -> 50;
            case POCO_COMUN -> 65;
            case RARA -> 80;
            case EPICA -> 95;
        };
    }
}