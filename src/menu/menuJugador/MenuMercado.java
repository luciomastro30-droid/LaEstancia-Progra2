package menu.menuJugador;

import excepciones.AccionNoPermitidaException;
import excepciones.StockInsuficienteException;
import modelo.Item;
import modelo.Partida;
import servicio.MercadoServicio;
import util.Consola;
import vista.DibujoMercado;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gestiona la interacción del jugador con el mercado.
 * Permite visualizar los productos vendibles del inventario y realizar ventas.
 */
public class MenuMercado {

    private final MercadoServicio mercadoServicio;
    private final PantallaJugador pantallaJugador;

    private String mensaje;


    public MenuMercado(
            PantallaJugador pantallaJugador
    ) {

        this.mercadoServicio =
                new MercadoServicio();

        this.pantallaJugador =
                pantallaJugador;
    }


    public void mostrar(
            Partida partida
    ) {

        int opcion;

        do {

            Map<Item, Integer> productos;

            try {

                // Se cargan únicamente los productos del inventario
                // que pueden ser vendidos en el mercado.
                productos =
                        mercadoServicio.productosVendibles(
                                partida
                        );

            } catch (SQLException e) {

                mensaje =
                        "Error al cargar el mercado: "
                                + e.getMessage();

                return;
            }

            if (productos.isEmpty()) {

                mensaje =
                        "No tenes productos para vender.";

                pantallaJugador.mostrar(
                        List.of(
                                "  MERCADO",
                                "",
                                "  No tenes productos",
                                "  para vender.",
                                "",
                                "  0. Volver"
                        ),
                        partida,
                        mensaje,
                        DibujoMercado.dibujar(
                                productos
                        )
                );

                Consola.leerEntero(
                        "Opcion: ",
                        0,
                        0
                );

                return;
            }

            // Se transforma el conjunto de productos del Map en una lista
            // para poder seleccionarlos mediante una opción numérica.
            List<Item> items =
                    new ArrayList<>(
                            productos.keySet()
                    );

            List<String> opciones =
                    List.of(
                            "  MERCADO",
                            "",
                            "  Vende cosechas,",
                            "  productos animales,",
                            "  materiales y pescados.",
                            "",
                            "  0. Volver"
                    );

            mensaje =
                    pantallaJugador.mostrar(
                            opciones,
                            partida,
                            mensaje,
                            DibujoMercado.dibujar(
                                    productos
                            )
                    );

            opcion =
                    Consola.leerEntero(
                            "Producto: ",
                            0,
                            items.size()
                    );

            mensaje = null;

            if (opcion != 0) {

                vender(
                        partida,
                        items.get(
                                opcion - 1
                        ),
                        productos.get(
                                items.get(
                                        opcion - 1
                                )
                        )
                );
            }

        } while (opcion != 0);
    }


    private void vender(
            Partida partida,
            Item item,
            int disponibles
    ) {

        int cantidad =
                Consola.leerEntero(
                        "Cantidad: ",
                        1,
                        disponibles
                );

        try {

            int ganancia =
                    mercadoServicio.gananciaTotal(
                            item,
                            cantidad
                    );

            // La venta y las validaciones correspondientes
            // quedan delegadas al servicio.
            mercadoServicio.vender(
                    partida,
                    item,
                    cantidad
            );

            mensaje =
                    "Vendiste "
                            + cantidad
                            + " "
                            + item.getNombre()
                            + " por $"
                            + ganancia
                            + ".";

        } catch (AccionNoPermitidaException
                 | StockInsuficienteException
                 | SQLException
                 | IllegalArgumentException e) {

            mensaje =
                    "No se pudo vender: "
                            + e.getMessage();
        }
    }
}