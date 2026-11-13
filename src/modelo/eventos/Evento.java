package modelo.eventos;

import modelo.Partida;

import java.util.Random;

/**
 * Clase base para todos los eventos aleatorios del juego.
 *
 * Un Evento define la informacion que tienen en comun
 * todos los eventos:
 *
 * - nombre
 * - descripcion
 * - dos opciones
 * - probabilidad de exito de cada opcion
 *
 * Las clases hijas, como Pomberito, Chupacabras o Tormenta,
 * van a decidir que ocurre concretamente al elegir cada opcion.
 *
 * Esta clase no usa consola, no accede a la base de datos
 * y no guarda la Partida.
 */
public abstract class Evento {

    private final String nombre;
    private final String descripcion;

    private final String textoOpcion1;
    private final int probabilidadExitoOpcion1;

    private final String textoOpcion2;
    private final int probabilidadExitoOpcion2;


    protected Evento(
            String nombre,
            String descripcion,
            String textoOpcion1,
            int probabilidadExitoOpcion1,
            String textoOpcion2,
            int probabilidadExitoOpcion2
    ) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del evento no puede estar vacio."
            );
        }

        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException(
                    "La descripcion del evento no puede estar vacia."
            );
        }

        if (textoOpcion1 == null || textoOpcion1.isBlank()) {
            throw new IllegalArgumentException(
                    "La opcion 1 no puede estar vacia."
            );
        }

        if (textoOpcion2 == null || textoOpcion2.isBlank()) {
            throw new IllegalArgumentException(
                    "La opcion 2 no puede estar vacia."
            );
        }

        validarProbabilidad(probabilidadExitoOpcion1);
        validarProbabilidad(probabilidadExitoOpcion2);

        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();

        this.textoOpcion1 = textoOpcion1.trim();
        this.probabilidadExitoOpcion1 = probabilidadExitoOpcion1;

        this.textoOpcion2 = textoOpcion2.trim();
        this.probabilidadExitoOpcion2 = probabilidadExitoOpcion2;
    }


    public final ResultadoEvento resolverOpcion(
            int opcion,
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

        return switch (opcion) {

            case 1 -> resolverOpcion1(partida, random);

            case 2 -> resolverOpcion2(partida, random);

            default -> throw new IllegalArgumentException(
                    "La opcion del evento debe ser 1 o 2."
            );
        };
    }

    protected abstract ResultadoEvento resolverOpcion1(
            Partida partida,
            Random random
    );

    protected abstract ResultadoEvento resolverOpcion2(
            Partida partida,
            Random random
    );


    /**
     * Determina si una opcion tuvo exito.
     *
     * Por ejemplo:
     *
     * probabilidad = 70
     *
     * significa que existe un 70% de posibilidades
     * de obtener true.
     *
     * Uso nextInt(100), que genera valores entre:
     *
     * 0 y 99
     *
     * Entonces:
     *
     * resultado < 70
     *
     * representa exactamente 70 resultados posibles
     * sobre un total de 100.
     */
    protected final boolean tuvoExito(
            Random random,
            int probabilidad
    ) {

        if (random == null) {
            throw new IllegalArgumentException(
                    "Random no puede ser null."
            );
        }

        validarProbabilidad(probabilidad);

        return random.nextInt(100) < probabilidad;
    }


    /**
     * Aplica los cambios de monedas y experiencia sobre la Partida.
     *
     * Este metodo centraliza esta logica para que Pomberito,
     * Chupacabras y Tormenta no tengan que repetirla.
     *
     * Los cambios pueden ser positivos o negativos.
     *
     * Ejemplo:
     *
     * +100 monedas
     * -50 monedas
     * +25 experiencia
     *
     * Nunca permito que monedas o experiencia queden por debajo de 0.
     */
    protected final ResultadoEvento aplicarResultado(
            Partida partida,
            boolean exito,
            String mensaje,
            int cambioMonedasDeseado,
            int cambioExperienciaDeseado
    ) {

        if (partida == null) {
            throw new IllegalArgumentException(
                    "La partida no puede ser null."
            );
        }

        int monedasAntes = partida.getMonedas();
        int experienciaAntes = partida.getExperiencia();

        /*
         * Calculo primero cuanto puede aplicarse realmente.
         *
         * Por ejemplo:
         *
         * tiene 20 monedas
         * el evento quiere quitar 50
         *
         * monedas finales = 0
         *
         * cambio real = -20
         */
        int monedasDespues = aplicarCambioSeguro(
                monedasAntes,
                cambioMonedasDeseado
        );

        int experienciaDespues = aplicarCambioSeguro(
                experienciaAntes,
                cambioExperienciaDeseado
        );

        /*
         * Modifico solamente el objeto Partida recibido.
         *
         * No guardo nada en MySQL desde aca.
         */
        partida.setMonedas(monedasDespues);
        partida.setExperiencia(experienciaDespues);

        /*
         * ResultadoEvento guarda el cambio REAL que se aplico.
         */
        return new ResultadoEvento(
                nombre,
                exito,
                mensaje,
                monedasDespues - monedasAntes,
                experienciaDespues - experienciaAntes
        );
    }


    /**
     * Aplica un cambio numerico evitando salir
     * del rango permitido.
     *
     * Uso long temporalmente para evitar problemas
     * de overflow al sumar dos int.
     */
    private int aplicarCambioSeguro(
            int valorActual,
            int cambio
    ) {

        long resultado =
                (long) valorActual + cambio;

        /*
         * Nunca permito valores negativos.
         */
        if (resultado < 0) {
            return 0;
        }

        /*
         * Tambien evito superar el limite maximo
         * que puede guardar un int.
         */
        if (resultado > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return (int) resultado;
    }


    /**
     * Compruebo que una probabilidad represente
     * un porcentaje valido.
     */
    private void validarProbabilidad(
            int probabilidad
    ) {

        if (probabilidad < 0 || probabilidad > 100) {
            throw new IllegalArgumentException(
                    "La probabilidad debe estar entre 0 y 100."
            );
        }
    }


    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTextoOpcion1() {
        return textoOpcion1;
    }

    public int getProbabilidadExitoOpcion1() {
        return probabilidadExitoOpcion1;
    }

    public String getTextoOpcion2() {
        return textoOpcion2;
    }

    public int getProbabilidadExitoOpcion2() {
        return probabilidadExitoOpcion2;
    }
}