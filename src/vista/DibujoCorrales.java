package vista;

import modelo.enums.Alojamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Genera la representacion visual ASCII de los corrales de la granja.
 * Muestra el estado del gallinero y del establo, su capacidad
 * y un resumen de los animales que contiene cada alojamiento.
 */
public class DibujoCorrales {

    private static final int ANCHO = 22;
    private static final int ANCHO_INTERIOR = 20;


    public static List<String> dibujar(
            boolean tieneGallinero,
            Map<String, Integer> gallinero,
            boolean tieneEstablo,
            Map<String, Integer> establo
    ) {

        List<String> lineas = new ArrayList<>();

        agregarCorral(
                lineas,
                "GALLINERO",
                tieneGallinero,
                gallinero,
                Alojamiento.GALLINERO.getCapacidad()
        );

        agregarCorral(
                lineas,
                "ESTABLO",
                tieneEstablo,
                establo,
                Alojamiento.ESTABLO.getCapacidad()
        );

        // Cierra visualmente todo el bloque de corrales.
        lineas.add(
                "+--------------------+"
        );

        return lineas;
    }


    private static void agregarCorral(
            List<String> lineas,
            String titulo,
            boolean construido,
            Map<String, Integer> animales,
            int capacidad
    ) {

        if (!construido) {

            lineas.add(
                    encabezado(
                            titulo,
                            null
                    )
            );

            lineas.add(
                    linea(
                            "  [sin construir]"
                    )
            );

            lineas.add(
                    linea("")
            );

            return;
        }

        int total =
                totalAnimales(animales);

        lineas.add(
                encabezado(
                        titulo,
                        total + "/" + capacidad
                )
        );

        if (animales == null
                || animales.isEmpty()) {

            lineas.add(
                    linea(
                            "      (vacio)"
                    )
            );

            lineas.add(
                    linea("")
            );

            return;
        }

        List<Map.Entry<String, Integer>> tipos =
                new ArrayList<>(
                        animales.entrySet()
                );

        lineas.add(
                lineaAnimal(
                        tipos.get(0)
                )
        );

        /*
         * Si hay mas de dos tipos mostramos uno
         * y resumimos los restantes.
         */
        if (tipos.size() > 2) {

            lineas.add(
                    linea(
                            " ... y "
                                    + (tipos.size() - 1)
                                    + " mas"
                    )
            );

        } else if (tipos.size() == 2) {

            lineas.add(
                    lineaAnimal(
                            tipos.get(1)
                    )
            );

        } else {

            lineas.add(
                    linea("")
            );
        }
    }


    private static String lineaAnimal(
            Map.Entry<String, Integer> entrada
    ) {

        return linea(
                " "
                        + entrada.getKey()
                        + " x"
                        + entrada.getValue()
        );
    }


    private static int totalAnimales(
            Map<String, Integer> animales
    ) {

        if (animales == null) {
            return 0;
        }

        int total = 0;

        for (int cantidad : animales.values()) {
            total += cantidad;
        }

        return total;
    }


    private static String encabezado(
            String titulo,
            String capacidad
    ) {

        String inicio =
                " " + titulo + " ";

        String fin =
                capacidad == null
                        ? ""
                        : " " + capacidad;

        int guiones =
                ANCHO_INTERIOR
                        - inicio.length()
                        - fin.length();

        String contenido =
                inicio
                        + "-".repeat(
                        Math.max(0, guiones)
                )
                        + fin;

        return "+"
                + ajustarInterior(
                contenido
        )
                + "+";
    }


    private static String linea(
            String texto
    ) {

        return "|"
                + ajustarInterior(
                texto
        )
                + "|";
    }


    private static String ajustarInterior(
            String texto
    ) {

        String ajustado =
                String.format(
                        "%-" + ANCHO_INTERIOR + "s",
                        texto
                );

        return ajustado.substring(
                0,
                ANCHO_INTERIOR
        );
    }
}