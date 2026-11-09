package modelo.enums;

public enum DefinicionMision {

    PRIMERA_COSECHA(
            "Agricultor",
            "Realiza tu primera cosecha.",
            1,
            100,
            25
    ),

    AYUDANDO_CARPINTERO(
            "Carpintero",
            "Recolecta madera para ayudar con sus trabajos.",
            10,
            200,
            50
    ),

    PEDIDO_PESCADOR(
            "Pescador",
            "Consegui algunos peces para completar un pedido.",
            5,
            180,
            40
    ),

    PIEDRA_PARA_EL_HERRERO(
            "Herrero",
            "Recolecta piedra para el taller del herrero.",
            8,
            200,
            50
    ),

    PRIMER_HUEVO(
            "Granjero",
            "Recolecta el primer huevo de tus animales.",
            1,
            120,
            30
    ),

    LECHE_FRESCA(
            "Granjero",
            "Consegui leche fresca de uno de tus animales.",
            1,
            150,
            35
    );


    // Estos datos son fijos para cada mision.
    // El progreso de cada jugador se guarda aparte en ProgresoMision.
    private final String npc;
    private final String descripcion;
    private final int objetivo;
    private final int recompensaMonedas;
    private final int recompensaExperiencia;


    DefinicionMision(
            String npc,
            String descripcion,
            int objetivo,
            int recompensaMonedas,
            int recompensaExperiencia
    ) {

        this.npc = npc;
        this.descripcion = descripcion;
        this.objetivo = objetivo;
        this.recompensaMonedas = recompensaMonedas;
        this.recompensaExperiencia = recompensaExperiencia;
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