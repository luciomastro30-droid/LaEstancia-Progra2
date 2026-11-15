package vista;

import modelo.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DibujoInventario {

    private static final int ANCHO = 32;
    private static final int ANCHO_INTERIOR = 30;


    public static List<String> dibujar(
            Map<Item, Integer> contenido
    ) {

        List<String> lineas = new ArrayList<>();

        lineas.add(
                encabezado("INVENTARIO")
        );

        if (contenido == null || contenido.isEmpty()) {

            lineas.add(
                    linea("")
            );

            lineas.add(
                    lineaCentrada("(vacio)")
            );

            lineas.add(
                    linea("")
            );

            lineas.add(
                    borde()
            );

            return lineas;
        }

        for (Map.Entry<Item, Integer> entrada :
                contenido.entrySet()) {

            lineas.add(
                    lineaItem(
                            entrada.getKey().getNombre(),
                            entrada.getValue()
                    )
            );
        }

        lineas.add(
                borde()
        );

        return lineas;
    }


    private static String lineaItem(
            String nombre,
            int cantidad
    ) {

        String cantidadTexto =
                "x" + cantidad;

        /*
         * Dejamos un espacio a cada lado.
         * El nombre se corta solo si no entra en la columna.
         */
        int anchoNombre =
                ANCHO_INTERIOR
                        - cantidadTexto.length()
                        - 2;

        String nombreVisible =
                nombre.length() > anchoNombre
                        ? nombre.substring(0, anchoNombre)
                        : nombre;

        int espacios =
                ANCHO_INTERIOR
                        - nombreVisible.length()
                        - cantidadTexto.length();

        String contenido =
                nombreVisible
                        + " ".repeat(
                        Math.max(1, espacios)
                )
                        + cantidadTexto;

        return linea(contenido);
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
                + "-".repeat(ANCHO_INTERIOR)
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

        if (texto.length() >= ANCHO_INTERIOR) {
            return linea(texto);
        }

        int espacios =
                ANCHO_INTERIOR
                        - texto.length();

        int izquierda =
                espacios / 2;

        return linea(
                " ".repeat(izquierda)
                        + texto
        );
    }


    private static String ajustar(
            String texto
    ) {

        if (texto.length() > ANCHO_INTERIOR) {
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