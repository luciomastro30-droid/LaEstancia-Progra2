import dao.InventarioDao;
import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import menu.menuJugador.MenuJugador;
import modelo.Inventario;
import modelo.Jugador;
import modelo.Partida;
import servicio.AnimalServicio;
import servicio.RegistroServicio;

public class PruebaIntegracionActual {

    public static void main(String[] args) {

        try {

            RegistroServicio registroServicio =
                    new RegistroServicio();

            String usuario =
                    "pruebaTotal"
                            + System.currentTimeMillis();

            Jugador jugador =
                    registroServicio.registrar(
                            usuario,
                            null,
                            "1234",
                            "Granja Prueba Total"
                    );

            Partida partida =
                    jugador.getPartidaActual();


            // =================================================
            // PRUEBA DE VALIDACION DE ANIMALES
            // =================================================

            AnimalServicio animalServicio =
                    new AnimalServicio();

            try {

                animalServicio.comprar(
                        partida,
                        animalServicio.tiposDisponibles().get(0),
                        "Prueba"
                );

                System.out.println(
                        "ERROR: permitio comprar sin alojamiento."
                );

            } catch (AccionNoPermitidaException e) {

                System.out.println(
                        "OK - compra sin alojamiento bloqueada:"
                );

                System.out.println(
                        e.getMessage()
                );
            }


            // =================================================
            // PREPARACION PARA PROBAR TODO DESDE EL MENU
            // =================================================

            /*
             * Herrero todavia no esta integrado.
             * Por eso construimos los alojamientos manualmente
             * solo para esta prueba temporal.
             */
            partida.setTieneGallinero(true);
            partida.setTieneEstablo(true);

            /*
             * Agrego monedas para poder comprar distintos animales.
             */
            partida.setMonedas(10000);

            PartidaDao partidaDao =
                    new PartidaDao();

            partidaDao.modificar(
                    partida
            );


            // =================================================
            // ALIMENTO PARA LOS ANIMALES
            // =================================================

            InventarioDao inventarioDao =
                    new InventarioDao();

            Inventario alimento =
                    inventarioDao.consultarPorPartidaEItem(
                            partida.getIdPartida(),
                            53
                    );

            if (alimento == null) {

                inventarioDao.alta(
                        new Inventario(
                                partida.getIdPartida(),
                                53,
                                20
                        )
                );

            } else {

                alimento.setCantidad(20);

                inventarioDao.modificar(
                        alimento
                );
            }


            System.out.println();
            System.out.println(
                    "======================================"
            );

            System.out.println(
                    " PARTIDA PREPARADA PARA PRUEBA TOTAL"
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Gallinero: construido"
            );

            System.out.println(
                    "Establo: construido"
            );

            System.out.println(
                    "Monedas: "
                            + partida.getMonedas()
            );

            System.out.println(
                    "Alimento: 20"
            );

            System.out.println();

            /*
             * Desde aca se prueba exactamente el menu real
             * que usara el jugador.
             */
            MenuJugador menuJugador =
                    new MenuJugador(
                            jugador
                    );

            menuJugador.mostrar();

        } catch (Exception e) {

            System.out.println(
                    "ERROR EN LA PRUEBA: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}