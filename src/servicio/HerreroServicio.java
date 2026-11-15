package servicio;

import conexion.ConexionBD;
import dao.InventarioDao;
import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import excepciones.MonedasInsuficientesException;
import excepciones.StockInsuficienteException;
import modelo.Inventario;
import modelo.Partida;
import modelo.enums.Rareza;
import modelo.enums.TipoHerramienta;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Gestiona las mejoras y construcciones disponibles en el herrero.
 * Valida monedas y materiales, actualiza la partida y mantiene
 * todas las modificaciones relacionadas dentro de una misma transacción.
 */
public class HerreroServicio {

    public static final int MINUTOS_HERRERO = 30;

    // Costos de herramientas segun la rareza nueva.
    public static final int COSTO_POCO_COMUN_MONEDAS = 300;
    public static final int COSTO_POCO_COMUN_MATERIAL = 10;

    public static final int COSTO_RARA_MONEDAS = 700;
    public static final int COSTO_RARA_MATERIAL = 20;

    public static final int COSTO_EPICA_MONEDAS = 1500;
    public static final int COSTO_EPICA_MATERIAL = 40;

    // La parcela 3 cuesta 300, la 4 cuesta 600, etc.
    public static final int COSTO_PARCELA_BASE = 300;

    public static final int COSTO_GALLINERO_MONEDAS = 800;
    public static final int COSTO_GALLINERO_MADERA = 20;

    public static final int COSTO_ESTABLO_MONEDAS = 1500;
    public static final int COSTO_ESTABLO_MADERA = 30;
    public static final int COSTO_ESTABLO_PIEDRA = 20;

    public static final int COSTO_TERRENO_MONEDAS = 2000;
    public static final int COSTO_TERRENO_PIEDRA = 30;

    // IDs definidos en la tabla item.
    private static final int ID_MADERA = 46;
    private static final int ID_PIEDRA = 47;

    private final InventarioDao inventarioDao;
    private final PartidaDao partidaDao;


    public HerreroServicio() {
        this.inventarioDao = new InventarioDao();
        this.partidaDao = new PartidaDao();
    }


    public void mejorarHerramienta(
            Partida partida,
            TipoHerramienta herramienta
    ) throws SQLException,
            AccionNoPermitidaException,
            MonedasInsuficientesException,
            StockInsuficienteException {

        if (herramienta == null) {
            throw new IllegalArgumentException(
                    "La herramienta no puede ser null."
            );
        }

        Rareza actual =
                rarezaActual(partida, herramienta);

        Rareza nueva =
                siguienteRareza(actual);

        int costoMonedas =
                costoMonedasHerramienta(nueva);

        int costoMaterial =
                costoMaterialHerramienta(nueva);

        /*
         * Hacha y cania usan madera.
         * El pico usa piedra.
         */
        int idMaterial =
                herramienta == TipoHerramienta.PICO
                        ? ID_PIEDRA
                        : ID_MADERA;

        Inventario material =
                obtenerMaterial(
                        partida,
                        idMaterial
                );

        validarRecursos(
                partida,
                costoMonedas,
                material,
                costoMaterial
        );

        aplicarMejora(
                partida,
                costoMonedas,
                material,
                costoMaterial,
                null,
                0,
                () -> setRareza(
                        partida,
                        herramienta,
                        nueva
                )
        );
    }


    public void comprarParcela(
            Partida partida
    ) throws SQLException,
            AccionNoPermitidaException,
            MonedasInsuficientesException {

        if (partida.getParcelasDesbloqueadas()
                >= Partida.MAX_PARCELAS) {

            throw new AccionNoPermitidaException(
                    "Ya tenes todas las parcelas desbloqueadas."
            );
        }

        int nuevasParcelas =
                partida.getParcelasDesbloqueadas() + 1;

        int costoMonedas =
                costoParcela(nuevasParcelas);

        validarMonedas(
                partida,
                costoMonedas
        );

        aplicarMejora(
                partida,
                costoMonedas,
                null,
                0,
                null,
                0,
                () -> partida.setParcelasDesbloqueadas(
                        nuevasParcelas
                )
        );
    }


    public void construirGallinero(
            Partida partida
    ) throws SQLException,
            AccionNoPermitidaException,
            MonedasInsuficientesException,
            StockInsuficienteException {

        if (partida.isTieneGallinero()) {

            throw new AccionNoPermitidaException(
                    "Ya tenes un gallinero."
            );
        }

        Inventario madera =
                obtenerMaterial(
                        partida,
                        ID_MADERA
                );

        validarRecursos(
                partida,
                COSTO_GALLINERO_MONEDAS,
                madera,
                COSTO_GALLINERO_MADERA
        );

        aplicarMejora(
                partida,
                COSTO_GALLINERO_MONEDAS,
                madera,
                COSTO_GALLINERO_MADERA,
                null,
                0,
                () -> partida.setTieneGallinero(true)
        );
    }


    public void construirEstablo(
            Partida partida
    ) throws SQLException,
            AccionNoPermitidaException,
            MonedasInsuficientesException,
            StockInsuficienteException {

        if (partida.isTieneEstablo()) {

            throw new AccionNoPermitidaException(
                    "Ya tenes un establo."
            );
        }

        Inventario madera =
                obtenerMaterial(
                        partida,
                        ID_MADERA
                );

        Inventario piedra =
                obtenerMaterial(
                        partida,
                        ID_PIEDRA
                );

        validarMonedas(
                partida,
                COSTO_ESTABLO_MONEDAS
        );

        validarStock(
                madera,
                COSTO_ESTABLO_MADERA
        );

        validarStock(
                piedra,
                COSTO_ESTABLO_PIEDRA
        );

        aplicarMejora(
                partida,
                COSTO_ESTABLO_MONEDAS,
                madera,
                COSTO_ESTABLO_MADERA,
                piedra,
                COSTO_ESTABLO_PIEDRA,
                () -> partida.setTieneEstablo(true)
        );
    }


    public void mejorarTerreno(
            Partida partida
    ) throws SQLException,
            AccionNoPermitidaException,
            MonedasInsuficientesException,
            StockInsuficienteException {

        if (partida.getNivelTerreno() >= 2) {

            throw new AccionNoPermitidaException(
                    "Ya tenes el terreno al maximo."
            );
        }

        Inventario piedra =
                obtenerMaterial(
                        partida,
                        ID_PIEDRA
                );

        validarRecursos(
                partida,
                COSTO_TERRENO_MONEDAS,
                piedra,
                COSTO_TERRENO_PIEDRA
        );

        aplicarMejora(
                partida,
                COSTO_TERRENO_MONEDAS,
                piedra,
                COSTO_TERRENO_PIEDRA,
                null,
                0,
                () -> partida.setNivelTerreno(2)
        );
    }


    public int costoMonedasHerramienta(
            Rareza nuevaRareza
    ) {

        return switch (nuevaRareza) {

            case POCO_COMUN ->
                    COSTO_POCO_COMUN_MONEDAS;

            case RARA ->
                    COSTO_RARA_MONEDAS;

            case EPICA ->
                    COSTO_EPICA_MONEDAS;

            case COMUN ->
                    throw new IllegalArgumentException(
                            "COMUN no es una mejora."
                    );
        };
    }


    public int costoMaterialHerramienta(
            Rareza nuevaRareza
    ) {

        return switch (nuevaRareza) {

            case POCO_COMUN ->
                    COSTO_POCO_COMUN_MATERIAL;

            case RARA ->
                    COSTO_RARA_MATERIAL;

            case EPICA ->
                    COSTO_EPICA_MATERIAL;

            case COMUN ->
                    throw new IllegalArgumentException(
                            "COMUN no es una mejora."
                    );
        };
    }


    public int costoParcela(
            int parcelasQueVaATener
    ) {

        if (parcelasQueVaATener < 3
                || parcelasQueVaATener > Partida.MAX_PARCELAS) {

            throw new IllegalArgumentException(
                    "La cantidad de parcelas no es valida."
            );
        }

        return COSTO_PARCELA_BASE
                * (parcelasQueVaATener - 2);
    }


    /*
     * Centraliza las operaciones del herrero que modifican monedas,
     * materiales y algún aspecto de la partida.
     * Runnable permite recibir la mejora concreta que debe aplicarse
     * sin repetir toda la lógica transaccional en cada método.
     */
    private void aplicarMejora(
            Partida partida,
            int costoMonedas,
            Inventario material1,
            int costoMaterial1,
            Inventario material2,
            int costoMaterial2,
            Runnable mejora
    ) throws SQLException {

        /*
         * Guardo el estado anterior porque un rollback
         * restaura MySQL pero no el objeto Partida.
         */
        int monedasAnteriores =
                partida.getMonedas();

        long minutoAnterior =
                partida.getMinutoJuegoTotal();

        int parcelasAnteriores =
                partida.getParcelasDesbloqueadas();

        int nivelTerrenoAnterior =
                partida.getNivelTerreno();

        boolean gallineroAnterior =
                partida.isTieneGallinero();

        boolean establoAnterior =
                partida.isTieneEstablo();

        Rareza hachaAnterior =
                partida.getRarezaHacha();

        Rareza picoAnterior =
                partida.getRarezaPico();

        Rareza caniaAnterior =
                partida.getRarezaCania();

        LocalDateTime fechaGuardadoAnterior =
                partida.getFechaGuardado();


        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                partida.setMonedas(
                        partida.getMonedas()
                                - costoMonedas
                );

                /*
                 * Los materiales, la mejora y la partida
                 * se guardan dentro de una sola transaccion.
                 */
                descontarMaterial(
                        con,
                        material1,
                        costoMaterial1
                );

                descontarMaterial(
                        con,
                        material2,
                        costoMaterial2
                );

                mejora.run();

                partida.avanzarMinutos(
                        MINUTOS_HERRERO
                );

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar la mejora."
                    );
                }

                con.commit();

            } catch (SQLException e) {

                con.rollback();

                // Restauro todos los cambios hechos sobre Partida.
                partida.setMonedas(
                        monedasAnteriores
                );

                partida.setMinutoJuegoTotal(
                        minutoAnterior
                );

                partida.setParcelasDesbloqueadas(
                        parcelasAnteriores
                );

                partida.setNivelTerreno(
                        nivelTerrenoAnterior
                );

                partida.setTieneGallinero(
                        gallineroAnterior
                );

                partida.setTieneEstablo(
                        establoAnterior
                );

                partida.setRarezaHacha(
                        hachaAnterior
                );

                partida.setRarezaPico(
                        picoAnterior
                );

                partida.setRarezaCania(
                        caniaAnterior
                );

                partida.setFechaGuardado(
                        fechaGuardadoAnterior
                );

                throw e;
            }
        }
    }


    private void descontarMaterial(
            Connection con,
            Inventario fila,
            int cantidad
    ) throws SQLException {

        if (fila == null || cantidad == 0) {
            return;
        }

        fila.setCantidad(
                fila.getCantidad() - cantidad
        );

        if (!inventarioDao.modificar(
                con,
                fila
        )) {

            throw new SQLException(
                    "No se pudo descontar el material."
            );
        }
    }


    private Inventario obtenerMaterial(
            Partida partida,
            int idItem
    ) throws SQLException {

        return inventarioDao.consultarPorPartidaEItem(
                partida.getIdPartida(),
                idItem
        );
    }


    private void validarRecursos(
            Partida partida,
            int costoMonedas,
            Inventario material,
            int costoMaterial
    ) throws MonedasInsuficientesException,
            StockInsuficienteException {

        validarMonedas(
                partida,
                costoMonedas
        );

        validarStock(
                material,
                costoMaterial
        );
    }


    private void validarMonedas(
            Partida partida,
            int costoMonedas
    ) throws MonedasInsuficientesException {

        if (partida.getMonedas()
                < costoMonedas) {

            throw new MonedasInsuficientesException(
                    costoMonedas,
                    partida.getMonedas()
            );
        }
    }


    private void validarStock(
            Inventario fila,
            int necesario
    ) throws StockInsuficienteException {

        int disponible =
                fila == null
                        ? 0
                        : fila.getCantidad();

        if (disponible < necesario) {

            throw new StockInsuficienteException(
                    necesario,
                    disponible
            );
        }
    }


    private Rareza rarezaActual(
            Partida partida,
            TipoHerramienta herramienta
    ) {

        return switch (herramienta) {

            case HACHA ->
                    partida.getRarezaHacha();

            case PICO ->
                    partida.getRarezaPico();

            case CANIA ->
                    partida.getRarezaCania();
        };
    }


    private Rareza siguienteRareza(
            Rareza actual
    ) throws AccionNoPermitidaException {

        return switch (actual) {

            case COMUN ->
                    Rareza.POCO_COMUN;

            case POCO_COMUN ->
                    Rareza.RARA;

            case RARA ->
                    Rareza.EPICA;

            case EPICA ->
                    throw new AccionNoPermitidaException(
                            "Ya tenes la mejor herramienta."
                    );
        };
    }


    private void setRareza(
            Partida partida,
            TipoHerramienta herramienta,
            Rareza nueva
    ) {

        switch (herramienta) {

            case HACHA ->
                    partida.setRarezaHacha(nueva);

            case PICO ->
                    partida.setRarezaPico(nueva);

            case CANIA ->
                    partida.setRarezaCania(nueva);
        }
    }
}