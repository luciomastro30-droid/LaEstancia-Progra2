package vista;

import modelo.Partida;
import modelo.enums.Rareza;
import servicio.HerreroServicio;

import java.util.ArrayList;
import java.util.List;

public class DibujoHerrero {

    private static final int ANCHO = 58;
    private static final int ANCHO_INTERIOR = 56;


    public static List<String> dibujar(Partida partida) {

        if (partida == null) {
            throw new IllegalArgumentException(
                    "La partida no puede ser null."
            );
        }

        List<String> lineas = new ArrayList<>();

        lineas.add(encabezado("HERRERO"));

        lineas.add(
                lineaHerramienta(
                        1,
                        "Hacha",
                        partida.getRarezaHacha(),
                        "Madera"
                )
        );

        lineas.add(
                lineaHerramienta(
                        2,
                        "Pico",
                        partida.getRarezaPico(),
                        "Piedra"
                )
        );

        lineas.add(
                lineaHerramienta(
                        3,
                        "Cania",
                        partida.getRarezaCania(),
                        "Madera"
                )
        );

        lineas.add(linea(""));

        lineas.add(
                lineaParcela(partida)
        );

        lineas.add(
                lineaConstruccion(
                        5,
                        "Gallinero",
                        partida.isTieneGallinero(),
                        "$" + HerreroServicio.COSTO_GALLINERO_MONEDAS
                                + " + "
                                + HerreroServicio.COSTO_GALLINERO_MADERA
                                + " Madera"
                )
        );

        lineas.add(
                lineaConstruccion(
                        6,
                        "Establo",
                        partida.isTieneEstablo(),
                        "$" + HerreroServicio.COSTO_ESTABLO_MONEDAS
                                + " + "
                                + HerreroServicio.COSTO_ESTABLO_MADERA
                                + " Madera + "
                                + HerreroServicio.COSTO_ESTABLO_PIEDRA
                                + " Piedra"
                )
        );

        lineas.add(
                lineaTerreno(partida)
        );

        lineas.add(borde());

        return lineas;
    }


    private static String lineaHerramienta(
            int opcion,
            String nombre,
            Rareza actual,
            String material
    ) {

        if (actual == Rareza.EPICA) {

            return linea(
                    opcion
                            + ". "
                            + nombre
                            + " | EPICA | MAXIMO"
            );
        }

        Rareza siguiente =
                siguienteRareza(actual);

        int monedas =
                costoMonedas(siguiente);

        int materialNecesario =
                costoMaterial(siguiente);

        return linea(
                opcion
                        + ". "
                        + nombre
                        + " "
                        + nombreRareza(actual)
                        + " -> "
                        + nombreRareza(siguiente)
                        + " | $"
                        + monedas
                        + " + "
                        + materialNecesario
                        + " "
                        + material
        );
    }


    private static String lineaParcela(
            Partida partida
    ) {

        if (partida.getParcelasDesbloqueadas()
                >= Partida.MAX_PARCELAS) {

            return linea(
                    "4. Parcelas | "
                            + partida.getParcelasDesbloqueadas()
                            + "/"
                            + Partida.MAX_PARCELAS
                            + " | MAXIMO"
            );
        }

        int nuevasParcelas =
                partida.getParcelasDesbloqueadas() + 1;

        int costo =
                HerreroServicio.COSTO_PARCELA_BASE
                        * (nuevasParcelas - 2);

        return linea(
                "4. Comprar parcela "
                        + nuevasParcelas
                        + "/"
                        + Partida.MAX_PARCELAS
                        + " | $"
                        + costo
        );
    }


    private static String lineaConstruccion(
            int opcion,
            String nombre,
            boolean construido,
            String costo
    ) {

        if (construido) {

            return linea(
                    opcion
                            + ". "
                            + nombre
                            + " | CONSTRUIDO"
            );
        }

        return linea(
                opcion
                        + ". Construir "
                        + nombre
                        + " | "
                        + costo
        );
    }


    private static String lineaTerreno(
            Partida partida
    ) {

        if (partida.getNivelTerreno() >= 2) {

            return linea(
                    "7. Terreno | Nivel 2 | MAXIMO"
            );
        }

        return linea(
                "7. Mejorar terreno | $"
                        + HerreroServicio.COSTO_TERRENO_MONEDAS
                        + " + "
                        + HerreroServicio.COSTO_TERRENO_PIEDRA
                        + " Piedra"
        );
    }


    private static Rareza siguienteRareza(
            Rareza actual
    ) {

        return switch (actual) {

            case COMUN ->
                    Rareza.POCO_COMUN;

            case POCO_COMUN ->
                    Rareza.RARA;

            case RARA, EPICA ->
                    Rareza.EPICA;
        };
    }


    private static int costoMonedas(
            Rareza rareza
    ) {

        return switch (rareza) {

            case POCO_COMUN ->
                    HerreroServicio.COSTO_POCO_COMUN_MONEDAS;

            case RARA ->
                    HerreroServicio.COSTO_RARA_MONEDAS;

            case EPICA ->
                    HerreroServicio.COSTO_EPICA_MONEDAS;

            case COMUN -> 0;
        };
    }


    private static int costoMaterial(
            Rareza rareza
    ) {

        return switch (rareza) {

            case POCO_COMUN ->
                    HerreroServicio.COSTO_POCO_COMUN_MATERIAL;

            case RARA ->
                    HerreroServicio.COSTO_RARA_MATERIAL;

            case EPICA ->
                    HerreroServicio.COSTO_EPICA_MATERIAL;

            case COMUN -> 0;
        };
    }


    private static String nombreRareza(
            Rareza rareza
    ) {

        return rareza.name();
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