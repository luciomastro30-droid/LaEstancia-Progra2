package modelo.enums;

/**
 * Enumera las misiones disponibles en el juego.
 * Cada misión define qué acción la hace avanzar, su objetivo,
 * el NPC relacionado y las recompensas que obtiene el jugador.
 */
public enum DefinicionMision {

    PRIMERA_COSECHA(
            TipoObjetivo.COSECHAR,
            "Agricultor",
            "Realiza tu primera cosecha.",
            1,
            100,
            25
    ),

    AYUDANDO_CARPINTERO(
            TipoObjetivo.TALAR,
            "Carpintero",
            "Recolecta madera para ayudar con sus trabajos.",
            10,
            200,
            50
    ),

    PEDIDO_PESCADOR(
            TipoObjetivo.PESCAR,
            "Pescador",
            "Consegui algunos peces para completar un pedido.",
            5,
            180,
            40
    ),

    PIEDRA_PARA_EL_HERRERO(
            TipoObjetivo.PICAR,
            "Herrero",
            "Recolecta piedra para el taller del herrero.",
            8,
            200,
            50
    ),

    PRIMER_HUEVO(
            TipoObjetivo.RECOLECTAR_HUEVO,
            "Granjero",
            "Recolecta el primer huevo de tus animales.",
            1,
            120,
            30
    ),

    LECHE_FRESCA(
            TipoObjetivo.ORDENIAR,
            "Granjero",
            "Consegui leche fresca de uno de tus animales.",
            1,
            150,
            35
    );


    /*
     * Indica que accion del jugador hace avanzar esta mision.
     * Los demas datos siguen siendo fijos para cada definicion.
     */
    private final TipoObjetivo tipoObjetivo;
    private final String npc;
    private final String descripcion;
    private final int objetivo;
    private final int recompensaMonedas;
    private final int recompensaExperiencia;


    DefinicionMision(
            TipoObjetivo tipoObjetivo,
            String npc,
            String descripcion,
            int objetivo,
            int recompensaMonedas,
            int recompensaExperiencia
    ) {

        this.tipoObjetivo = tipoObjetivo;
        this.npc = npc;
        this.descripcion = descripcion;
        this.objetivo = objetivo;
        this.recompensaMonedas = recompensaMonedas;
        this.recompensaExperiencia = recompensaExperiencia;
    }


    public TipoObjetivo getTipoObjetivo() {
        return tipoObjetivo;
    }


    public String getNpc() {
        return npc;
    }


    public String getDescripcion() {
        return descripcion;
    }


    public int getObjetivo() {
        return objetivo;
    }


    public int getRecompensaMonedas() {
        return recompensaMonedas;
    }


    public int getRecompensaExperiencia() {
        return recompensaExperiencia;
    }
}