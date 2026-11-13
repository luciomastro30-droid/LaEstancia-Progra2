package modelo.eventos;

import modelo.Partida;

import java.util.Random;

public class Tormenta extends Evento {

    private static final int PROBABILIDAD_PROTEGER = 45;
    private static final int PROBABILIDAD_REFUGIARSE = 85;


    public Tormenta() {

        super(
                "Tormenta",

                "El cielo se oscurece rapidamente y una fuerte tormenta "
                        + "se acerca a la granja.",

                "Salir a proteger la granja",
                PROBABILIDAD_PROTEGER,

                "Refugiarse y esperar a que pase",
                PROBABILIDAD_REFUGIARSE
        );
    }


    /**
     * Resuelve la primera opcion:
     *
     * PROTEGER LA GRANJA
     *
     * Es la opcion mas riesgosa.
     *
     * Exito:
     * +160 monedas
     * +45 experiencia
     *
     * Fracaso:
     * -90 monedas
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
                    "Lograste asegurar la granja antes de que "
                            + "la tormenta causara daños importantes.",
                    160,
                    45
            );
        }

        return aplicarResultado(
                partida,
                false,
                "La tormenta fue demasiado fuerte y tuviste "
                        + "que gastar monedas reparando algunos daños.",
                -90,
                0
        );
    }


    /**
     * Resuelve la segunda opcion:
     *
     * REFUGIARSE
     *
     * Es una opcion mucho mas segura,
     * aunque entrega una recompensa menor.
     *
     * Exito:
     * +20 experiencia
     *
     * Fracaso:
     * -30 monedas
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
                    "Esperaste a salvo hasta que la tormenta termino "
                            + "sin causar daños importantes.",
                    0,
                    20
            );
        }

        return aplicarResultado(
                partida,
                false,
                "Aunque te refugiaste, algunos daños menores "
                        + "te obligaron a gastar monedas en reparaciones.",
                -30,
                0
        );
    }
}