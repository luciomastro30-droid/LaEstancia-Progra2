package vista;

import modelo.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Genera la representacion visual ASCII del mercado.
 * Muestra los productos que el jugador puede vender,
 * junto con la cantidad disponible y su precio de venta.
 */
public class DibujoMercado {

    private static final int ANCHO = 48;
    private static final int ANCHO_INTERIOR = 46;


    public static List<String> dibujar(
            Map<Item, Integer> productos
    ) {

        List<String> lineas =
                new ArrayList<>();

        lineas.add(
                encabezado(
                        "MERCADO"
                )
        );

        if (productos == null
                || productos.isEmpty()) {

            lineas.add(
                    linea("")
            );

            lineas.add(
                    lineaCentrada(
                            "No tenes productos para vender."
                    )
            );

            lineas.add(
                    linea("")
            );

            lineas.add(
                    borde()
            );

            return DibujoComerciante.alLado(
                    lineas, DibujoComerciante.mercado());
        }

        int opcion = 1;

        for (Map.Entry<Item, Integer> entrada :
                productos.entrySet()) {

            lineas.add(
                    lineaProducto(
                            opcion,
                            entrada.getKey(),
                            entrada.getValue()
                    )
            );

            opcion++;
        }

        lineas.add(
                borde()
        );

        return DibujoComerciante.alLado(
                lineas, DibujoComerciante.mercado());
    }


    private static String lineaProducto(
            int opcion,
            Item item,
            int cantidad
    ) {

        String inicio =
                opcion
                        + ". "
                        + item.getNombre();

        String stock =
                "x" + cantidad;

        String precio =
                "$" + item.getPrecioVenta();

        /*
         * Dejamos el nombre a la izquierda,
         * la cantidad en el medio y el precio a la derecha.
         */
        int espacioDisponible =
                ANCHO_INTERIOR
                        - stock.length()
                        - precio.length()
                        - 4;

        String nombreVisible =
                inicio;

        if (nombreVisible.length()
                > espacioDisponible) {

            nombreVisible =
                    nombreVisible.substring(
                            0,
                            espacioDisponible
                    );
        }

        int espaciosAntesStock =
                espacioDisponible
                        - nombreVisible.length()
                        + 2;

        return linea(
                nombreVisible
                        + " ".repeat(
                        Math.max(
                                1,
                                espaciosAntesStock
                        )
                )
                        + stock
                        + "  "
                        + precio
        );
    }


    private static String encabezado(
            String titulo
    ) {

        String inicio =
                " " + titulo + " ";

        int guiones =
                ANCHO_INTERIOR
                        - inicio.length();

        return "+"
                + inicio
                + "-".repeat(
                Math.max(
                        0,
                        guiones
                )
        )
                + "+";
    }


    private static String borde() {

        return "+"
                + "-".repeat(
                ANCHO_INTERIOR
        )
                + "+";
    }


    private static String linea(
            String texto
    ) {

        return "|"
                + ajustar(texto)
                + "|";
    }


    private static String lineaCentrada(
            String texto
    ) {

        if (texto.length()
                >= ANCHO_INTERIOR) {

            return linea(texto);
        }

        int espacios =
                ANCHO_INTERIOR
                        - texto.length();

        int izquierda =
                espacios / 2;

        return linea(
                " ".repeat(
                        izquierda
                )
                        + texto
        );
    }


    private static String ajustar(
            String texto
    ) {

        if (texto.length()
                > ANCHO_INTERIOR) {

            texto =
                    texto.substring(
                            0,
                            ANCHO_INTERIOR
                    );
        }

        return String.format(
                "%-" + ANCHO_INTERIOR + "s",
                texto
        );
    }
}