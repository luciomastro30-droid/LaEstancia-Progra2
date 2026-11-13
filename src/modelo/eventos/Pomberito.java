package modelo.eventos;

import modelo.Partida;

import java.util.Random;

/**
 * Evento concreto del Pomberito.
 *
 * Extiende de Evento porque comparte la estructura general
 * de todos los eventos del juego:
 *
 * - nombre
 * - descripcion
 * - dos opciones
 * - probabilidad de exito para cada opcion
 *
 * Esta clase solamente define que ocurre especificamente
 * cuando aparece el Pomberito.
 */
public class Pomberito extends Evento {
    private static final int PROBABILIDAD_ENFRENTAR = 40;
    private static final int PROBABILIDAD_ESCONDERSE = 80;


    public Pomberito() {

        super(
                "Pomberito",

                "Escuchas silbidos cerca de la granja y una pequeña figura "
                        + "aparece entre los arboles.",

                "Enfrentarlo para proteger la granja",
                PROBABILIDAD_ENFRENTAR,

                "Esconderse y esperar a que se vaya",
                PROBABILIDAD_ESCONDERSE
        );
    }


    /**
     * Resuelve la primera opcion:
     *
     * ENFRENTAR AL POMBERITO
     *
     * Es la opcion mas arriesgada.
     *
     * Exito:
     * +180 monedas
     * +50 experiencia
     *
     * Fracaso:
     * -80 monedas
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
                    "Lograste ahuyentar al Pomberito "
                            + "y protegiste la granja.",
                    180,
                    50
            );
        }

        return aplicarResultado(
                partida,
                false,
                "El Pomberito te sorprendio y perdiste "
                        + "algunas monedas durante el enfrentamiento.",
                -80,
                0
        );
    }


    /**
     * Resuelve la segunda opcion:
     *
     * ESCONDERSE
     *
     * Tiene una probabilidad de exito mayor,
     * pero entrega una recompensa menor.
     *
     * Exito:
     * +15 experiencia
     *
     * Fracaso:
     * -25 monedas
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
                    "Esperaste en silencio hasta que "
                            + "el Pomberito se alejo de la granja.",
                    0,
                    15
            );
        }

        return aplicarResultado(
                partida,
                false,
                "El Pomberito encontro tu escondite "
                        + "y te hizo perder algunas monedas.",
                -25,
                0
        );
    }
}