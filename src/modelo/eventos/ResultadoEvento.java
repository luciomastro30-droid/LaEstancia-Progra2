package modelo.eventos;


public class ResultadoEvento {

    private final String nombreEvento;
    private final boolean exito;
    private final String mensaje;
    private final int cambioMonedas;
    private final int cambioExperiencia;


    /**
     * Creo un resultado con los cambios que realmente se aplicaron a la Partida.
     *
     * Los cambios pueden ser positivos, negativos o cero.
     *
     * Por ejemplo:
     *
     * cambioMonedas = 100
     * -> gano 100 monedas
     *
     * cambioMonedas = -40
     * -> perdio 40 monedas
     *
     * cambioMonedas = 0
     * -> las monedas no cambiaron
     *
     * La responsabilidad de impedir que las monedas o la experiencia finales
     * queden por debajo de 0 pertenece a la logica del Evento que aplica el efecto.
     */
    public ResultadoEvento(
            String nombreEvento,
            boolean exito,
            String mensaje,
            int cambioMonedas,
            int cambioExperiencia
    ) {

        if (nombreEvento == null || nombreEvento.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del evento no puede estar vacio."
            );
        }

        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException(
                    "El mensaje del resultado no puede estar vacio."
            );
        }

        this.nombreEvento = nombreEvento.trim();
        this.exito = exito;
        this.mensaje = mensaje.trim();
        this.cambioMonedas = cambioMonedas;
        this.cambioExperiencia = cambioExperiencia;
    }


    public String getNombreEvento() {
        return nombreEvento;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getCambioMonedas() {
        return cambioMonedas;
    }

    public int getCambioExperiencia() {
        return cambioExperiencia;
    }


    @Override
    public String toString() {
        return "ResultadoEvento{" +
                "nombreEvento='" + nombreEvento + '\'' +
                ", exito=" + exito +
                ", mensaje='" + mensaje + '\'' +
                ", cambioMonedas=" + cambioMonedas +
                ", cambioExperiencia=" + cambioExperiencia +
                '}';
    }
}