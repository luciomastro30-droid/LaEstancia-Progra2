import dao.InventarioDao;
import dao.ItemDao;
import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import excepciones.MonedasInsuficientesException;
import modelo.Inventario;
import modelo.Item;
import modelo.Jugador;
import modelo.Partida;
import servicio.RegistroServicio;
import servicio.TiendaServicio;

public class PruebaTienda {

    public static void main(String[] args) {

        try {

            RegistroServicio registroServicio = new RegistroServicio();
            TiendaServicio tiendaServicio = new TiendaServicio();
            ItemDao itemDao = new ItemDao();
            InventarioDao inventarioDao = new InventarioDao();
            PartidaDao partidaDao = new PartidaDao();

            String usuario =
                    "pruebaTienda" + System.currentTimeMillis();

            Jugador jugador = registroServicio.registrar(
                    usuario,
                    null,
                    "1234",
                    "Granja Tienda"
            );

            Partida partida = jugador.getPartidaActual();

            /*
             * El jugador nuevo ya recibe semillas de trigo.
             * Usamos el mismo item para comprobar que la compra
             * se acumule sobre una fila existente.
             */
            Item semillaTrigo =
                    itemDao.consultarPorId(1);

            Item madera =
                    itemDao.consultarPorId(46);


            // =====================================================
            // ESTADO INICIAL
            // =====================================================

            System.out.println(
                    "===== ESTADO INICIAL ====="
            );

            mostrar(
                    partida,
                    semillaTrigo,
                    inventarioDao
            );


            // =====================================================
            // PRUEBA 1 - PRODUCTOS DISPONIBLES
            // =====================================================

            System.out.println(
                    "\n===== PRODUCTOS DISPONIBLES ====="
            );

            for (Item item :
                    tiendaServicio.productosDisponibles()) {

                System.out.println(
                        item.getNombre()
                                + " | "
                                + item.getTipo()
                                + " | $"
                                + item.getPrecioCompra()
                );
            }


            // =====================================================
            // PRUEBA 2 - COMPRA CORRECTA
            // =====================================================

            System.out.println(
                    "\n===== COMPRAR 5 SEMILLAS ====="
            );

            int costo =
                    tiendaServicio.costoTotal(
                            semillaTrigo,
                            5
                    );

            System.out.println(
                    "Costo esperado: $"
                            + costo
            );

            tiendaServicio.comprar(
                    partida,
                    semillaTrigo,
                    5
            );

            mostrar(
                    partida,
                    semillaTrigo,
                    inventarioDao
            );


            // =====================================================
            // PRUEBA 3 - VOLVER A COMPRAR
            // =====================================================

            System.out.println(
                    "\n===== ACUMULAR INVENTARIO ====="
            );

            tiendaServicio.comprar(
                    partida,
                    semillaTrigo,
                    2
            );

            mostrar(
                    partida,
                    semillaTrigo,
                    inventarioDao
            );


            // =====================================================
            // PRUEBA 4 - SIN MONEDAS
            // =====================================================

            System.out.println(
                    "\n===== ERROR SIN MONEDAS ====="
            );

            partida.setMonedas(0);
            partidaDao.modificar(partida);

            try {

                tiendaServicio.comprar(
                        partida,
                        semillaTrigo,
                        1
                );

                System.out.println(
                        "ERROR: permitio comprar sin monedas."
                );

            } catch (MonedasInsuficientesException e) {

                System.out.println(
                        "Correcto: "
                                + e.getMessage()
                );
            }


            // =====================================================
            // PRUEBA 5 - ITEM NO COMPRABLE
            // =====================================================

            System.out.println(
                    "\n===== ERROR ITEM NO COMPRABLE ====="
            );

            partida.setMonedas(10000);
            partidaDao.modificar(partida);

            try {

                tiendaServicio.comprar(
                        partida,
                        madera,
                        1
                );

                System.out.println(
                        "ERROR: permitio comprar madera."
                );

            } catch (AccionNoPermitidaException e) {

                System.out.println(
                        "Correcto: "
                                + e.getMessage()
                );
            }


            System.out.println(
                    "\n===== FIN DE LA PRUEBA ====="
            );

        } catch (Exception e) {

            System.out.println(
                    "Ocurrio un error durante la prueba: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }


    private static void mostrar(
            Partida partida,
            Item item,
            InventarioDao inventarioDao
    ) throws Exception {

        Inventario fila =
                inventarioDao.consultarPorPartidaEItem(
                        partida.getIdPartida(),
                        item.getIdItem()
                );

        System.out.println(
                "Monedas: "
                        + partida.getMonedas()
        );

        System.out.println(
                item.getNombre()
                        + ": "
                        + (fila == null
                        ? 0
                        : fila.getCantidad())
        );
    }
}