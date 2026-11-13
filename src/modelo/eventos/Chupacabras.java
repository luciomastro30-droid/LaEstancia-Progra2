package modelo.eventos;

import modelo.Partida;

import java.util.Random;

public class Chupacabras extends Evento {

    private static final int PROBABILIDAD_ENFRENTAR = 35;
    private static final int PROBABILIDAD_ESPANTAR = 75;


    public Chupacabras() {

        super(
                "Chupacabras",

                "Escuchas ruidos extraños cerca de los animales. "
                        + "Al acercarte, ves una criatura merodeando la granja.",

                "Enfrentarlo para proteger a los animales",
                PROBABILIDAD_ENFRENTAR,

                "Intentar espantarlo con ruido y luces",
                PROBABILIDAD_ESPANTAR
        );
    }


    /**
     * Resuelve la primera opcion:
     *
     * ENFRENTAR AL CHUPACABRAS
     *
     * Es la opcion mas riesgosa.
     *
     * Exito:
     * +220 monedas
     * +60 experiencia
     *
     * Fracaso:
     * -100 monedas
     */
    @Override
    protected ResultadoEvento resolverOpcion1(
            Partida partida,
            Random random
    ) {

        boolean exito = tuvoExito(
                random,
                getProbabilidadExitoOpcion1()
        );

        if (exito) {

            return aplicarResultado(
                    partida,
                    true,
                    "Lograste ahuyentar al Chupacabras "
                            + "y proteger a los animales de la granja.",
                    220,
                    60
            );
        }

        return aplicarResultado(
                partida,
                false,
                "El Chupacabras logro escapar y los destrozos "
                        + "te hicieron perder algunas monedas.",
                -100,
                0
        );
    }


    /**
     * Resuelve la segunda opcion:
     *
     * ESPANTAR AL CHUPACABRAS
     *
     * Tiene mayor probabilidad de exito,
     * pero ofrece una recompensa menor.
     *
     * Exito:
     * +25 experiencia
     *
     * Fracaso:
     * -35 monedas
     */
    @Override
    protected ResultadoEvento resolverOpcion2(
            Partida partida,
            Random random
    ) {

        boolean exito = tuvoExito(
                random,
                getProbabilidadExitoOpcion2()
        );

        if (exito) {

            return aplicarResultado(
                    partida,
                    true,
                    "Los ruidos y las luces asustaron al Chupacabras "
                            + "y lograste alejarlo de la granja.",
                    0,
                    25
            );
        }

        return aplicarResultado(
                partida,
                false,
                "El Chupacabras no se asusto y causo algunos destrozos "
                        + "antes de escapar.",
                -35,
                0
        );
    }
}