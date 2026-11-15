package menu.menuJugador;

import excepciones.AccionNoPermitidaException;
import excepciones.MonedasInsuficientesException;
import modelo.Item;
import modelo.Partida;
import servicio.TiendaServicio;
import util.Consola;
import vista.DibujoTienda;

import java.sql.SQLException;
import java.util.List;

public class MenuTienda {

    private final TiendaServicio tiendaServicio;
    private final PantallaJugador pantallaJugador;

    private String mensaje;


    public MenuTienda(
            PantallaJugador pantallaJugador
    ) {

        this.tiendaServicio =
                new TiendaServicio();

        this.pantallaJugador =
                pantallaJugador;
    }


    public void mostrar(
            Partida partida
    ) {

        int opcion;

        do {

            List<Item> productos;

            try {

                productos =
                        tiendaServicio.productosDisponibles();

            } catch (SQLException e) {

                mensaje =
                        "Error al cargar la tienda: "
                                + e.getMessage();

                return;
            }

            if (productos.isEmpty()) {

                mensaje =
                        "No hay productos disponibles.";

                return;
            }

            List<String> opciones =
                    List.of(
                            "  TIENDA AGRICOLA",
                            "",
                            "  Compra semillas",
                            "  y alimento.",
                            "",
                            "  0. Volver"
                    );

            mensaje =
                    pantallaJugador.mostrar(
                            opciones,
                            partida,
                            mensaje,
                            DibujoTienda.dibujar(
                                    productos
                            )
                    );

            opcion =
                    Consola.leerEntero(
                            "Producto: ",
                            0,
                            productos.size()
                    );

            mensaje = null;

            if (opcion != 0) {

                comprar(
                        partida,
                        productos.get(
                                opcion - 1
                        )
                );
            }

        } while (opcion != 0);
    }


    private void comprar(
            Partida partida,
            Item item
    ) {

        int cantidad =
                Consola.leerEntero(
                        "Cantidad: ",
                        1,
                        Integer.MAX_VALUE
                );

        int costo =
                tiendaServicio.costoTotal(
                        item,
                        cantidad
                );

        try {

            tiendaServicio.comprar(
                    partida,
                    item,
                    cantidad
            );

            mensaje =
                    "Compraste "
                            + cantidad
                            + " "
                            + item.getNombre()
                            + " por $"
                            + costo
                            + ".";

        } catch (AccionNoPermitidaException
                 | MonedasInsuficientesException
                 | SQLException e) {

            mensaje =
                    "No se pudo comprar: "
                            + e.getMessage();
        }
    }
}