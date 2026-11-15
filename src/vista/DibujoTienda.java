package vista;

import modelo.Item;

import java.util.ArrayList;
import java.util.List;

public class DibujoTienda {

    private static final int ANCHO = 42;
    private static final int ANCHO_INTERIOR = 40;


    public static List<String> dibujar(
            List<Item> productos
    ) {

        List<String> lineas =
                new ArrayList<>();

        lineas.add(
                encabezado(
                        "TIENDA AGRICOLA"
                )
        );

        if (productos == null
                || productos.isEmpty()) {

            lineas.add(
                    linea("")
            );

            lineas.add(
                    lineaCentrada(
                            "No hay productos disponibles."
                    )
            );

            lineas.add(
                    linea("")
            );

            lineas.add(
                    borde()
            );

            return DibujoComerciante.alLado(
                    lineas, DibujoComerciante.tienda());
        }

        int opcion = 1;

        for (Item item : productos) {

            lineas.add(
                    lineaProducto(
                            opcion,
                            item
                    )
            );

            opcion++;
        }

        lineas.add(
                borde()
        );

        return DibujoComerciante.alLado(
                lineas, DibujoComerciante.tienda());
    }


    private static String lineaProducto(
            int opcion,
            Item item
    ) {

        String inicio =
                opcion
                        + ". "
                        + item.getNombre();

        String precio =
                "$"
                        + item.getPrecioCompra();

        int espacios =
                ANCHO_INTERIOR
                        - inicio.length()
                        - precio.length();

        if (espacios < 1) {

            int maxNombre =
                    ANCHO_INTERIOR
                            - precio.length()
                            - 4;

            String nombre =
                    item.getNombre();

            if (nombre.length()
                    > maxNombre) {

                nombre =
                        nombre.substring(
                                0,
                                maxNombre
                        );
            }

            inicio =
                    opcion
                            + ". "
                            + nombre;

            espacios =
                    ANCHO_INTERIOR
                            - inicio.length()
                            - precio.length();
        }

        return linea(
                inicio
                        + " ".repeat(
                        Math.max(1, espacios)
                )
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
                Math.max(0, guiones)
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