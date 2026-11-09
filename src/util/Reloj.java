package util;

public class Reloj {

    // Defino constantes para no repetir numeros sueltos en el codigo.
    private static final int MINUTOS_POR_HORA = 60;
    private static final int HORAS_POR_DIA = 24;

    private static final int MINUTOS_POR_DIA =
            MINUTOS_POR_HORA * HORAS_POR_DIA;

    // Guardo el minuto total del juego que quiero interpretar.
    // Uso final porque este objeto Reloj representa un momento especifico
    // y no necesito modificarlo una vez creado.
    private final long minutoJuegoTotal;


    public Reloj(long minutoJuegoTotal) {
        if (minutoJuegoTotal < 0) {
            throw new IllegalArgumentException(
                    "El minuto total del juego no puede ser negativo."
            );
        }

        this.minutoJuegoTotal = minutoJuegoTotal;
    }


    public long getDia() {

        /*
         * Divido los minutos totales por la cantidad de minutos que tiene un dia.
         * Por ejemplo:
         * 480 / 1440 = 0
         * Como para el jugador el primer dia se muestra como Dia 1
         * y no Dia 0, le sumo 1 al resultado.
         */
        return (minutoJuegoTotal / MINUTOS_POR_DIA) + 1;
    }


    public int getHora() {

        /*
         * Con el operador % obtengo cuantos minutos corresponden
         * solamente al dia actual.
         *
         * Por ejemplo:
         * si pasaron 2000 minutos, primero saco los dias completos
         * y me quedo con los minutos restantes de ese dia.
         */
        long minutoDelDia = minutoJuegoTotal % MINUTOS_POR_DIA;

        // Divido esos minutos por 60 para obtener la hora actual.
        return (int) (minutoDelDia / MINUTOS_POR_HORA);
    }


    public int getMinuto() {

        /*
         * El resto de dividir los minutos totales por 60
         * me indica en que minuto de la hora estoy.
         *
         * Por ejemplo:
         * 870 % 60 = 30
         */
        return (int) (minutoJuegoTotal % MINUTOS_POR_HORA);
    }


    public String getTexto() {

        /*
         * Muestro el tiempo del juego con el formato pedido.
         * %02d hace que hora y minuto siempre tengan dos digitos.
         * Ejemplo:
         * en vez de 8:5 se muestra 08:05.
         */
        return String.format(
                "Día %d - %02d:%02d",
                getDia(),
                getHora(),
                getMinuto()
        );
    }


    public boolean esDeNoche() {

        int hora = getHora();

        /*
         * Se considera noche desde las 20:00 hasta las 05:59.
         *
         * Uso || porque este horario cruza la medianoche
        */
        return hora >= 20 || hora < 6;
    }


    public boolean comerciosAbiertos() {

        int hora = getHora();
        int minuto = getMinuto();

        /*
         * Convierto la hora actual nuevamente a minutos para poder
         * comparar los horarios de una forma mas simple.
         *
         * Ejemplo:
         * 14:30 = 14 * 60 + 30 = 870 minutos.
         */
        int minutoActual =
                hora * MINUTOS_POR_HORA + minuto;

        // Los comercios abren a las 08:00.
        int apertura =
                8 * MINUTOS_POR_HORA;

        // Los comercios cierran a las 18:00.
        int cierre =
                18 * MINUTOS_POR_HORA;


        return minutoActual >= apertura
                && minutoActual <= cierre;
    }
}