package servicio;

import modelo.Partida;
import modelo.eventos.Chupacabras;
import modelo.eventos.Evento;
import modelo.eventos.Pomberito;
import modelo.eventos.Tormenta;
import util.Reloj;

import java.util.List;
import java.util.Random;

public class EventoServicio {

    private static final int PROBABILIDAD_DIA = 8;
    private static final int PROBABILIDAD_NOCHE = 20;

    private final List<Evento> eventos;



    public EventoServicio() {

        this.eventos = List.of(
                new Pomberito(),
                new Chupacabras(),
                new Tormenta()
        );
    }


    /**
     * Intenta generar un evento para la Partida recibida.
     *
     * Devuelve:
     *
     * Evento -> si ocurre un evento
     * null   -> si no ocurre ninguno
     *
     * Random se recibe como parametro para poder controlar
     * los resultados durante las pruebas.
     */
    public Evento intentarGenerarEvento(
            Partida partida,
            Random random
    ) {

        if (partida == null) {
            throw new IllegalArgumentException(
                    "La partida no puede ser null."
            );
        }

        if (random == null) {
            throw new IllegalArgumentException(
                    "Random no puede ser null."
            );
        }


        Reloj reloj =
                new Reloj(partida.getMinutoJuegoTotal());



        long diaReloj = reloj.getDia();

        if (diaReloj > Integer.MAX_VALUE) {
            throw new IllegalStateException(
                    "El dia actual supera el rango soportado por la partida."
            );
        }

        int diaActual = (int) diaReloj;


        /*
         * Si ya ocurrio un evento durante este mismo dia,
         * no permito generar otro.
         *
         * null significa que nunca ocurrio ningun evento.
         */
        Integer diaUltimoEvento =
                partida.getDiaUltimoEvento();

        if (diaUltimoEvento != null
                && diaUltimoEvento == diaActual) {

            return null;
        }


        int probabilidad;

        if (reloj.esDeNoche()) {

            probabilidad = PROBABILIDAD_NOCHE;

        } else {

            probabilidad = PROBABILIDAD_DIA;
        }


        /*
         * nextInt(100) genera valores entre 0 y 99.
         *
         * De dia:
         *
         * resultado < 8
         *
         * significa 8 resultados posibles sobre 100.
         *
         * De noche:
         *
         * resultado < 20
         *
         * significa 20 resultados posibles sobre 100.
         */
        boolean ocurreEvento =
                random.nextInt(100) < probabilidad;


        if (!ocurreEvento) {
            return null;
        }



        Evento evento =
                eventos.get(
                        random.nextInt(eventos.size())
                );


        /*
         * Marco inmediatamente que durante este dia
         * ya aparecio un evento.
         *
         * Lo hago cuando el evento APARECE y no cuando
         * el jugador termina de resolverlo.
         *
         * De esta manera nunca pueden aparecer
         * dos eventos durante el mismo dia.
         */
        partida.setDiaUltimoEvento(diaActual);


        return evento;
    }
}