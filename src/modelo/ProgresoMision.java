package modelo;

import modelo.enums.EstadoMision;

public class ProgresoMision {

    private int idProgresoMision;
    private int idPartida;
    private String codigoMision;
    private int progreso;
    private EstadoMision estado;


    public ProgresoMision(int idProgresoMision,
                          int idPartida,
                          String codigoMision,
                          int progreso,
                          EstadoMision estado) {

        setIdProgresoMision(idProgresoMision);
        setIdPartida(idPartida);
        setCodigoMision(codigoMision);
        setProgreso(progreso);
        setEstado(estado);
    }


    public ProgresoMision(int idPartida, String codigoMision) {

        this(
                0,
                idPartida,
                codigoMision,
                0,
                EstadoMision.ACTIVA
        );
    }


    public int getIdProgresoMision() {
        return idProgresoMision;
    }

    public void setIdProgresoMision(int idProgresoMision) {

        // El ID 0 representa un progreso que todavia
        // no fue guardado en la base.
        if (idProgresoMision < 0) {
            throw new IllegalArgumentException(
                    "El ID del progreso de mision no puede ser negativo."
            );
        }

        this.idProgresoMision = idProgresoMision;
    }


    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {

        if (idPartida <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la partida debe ser mayor a 0."
            );
        }

        this.idPartida = idPartida;
    }


    public String getCodigoMision() {
        return codigoMision;
    }

    public void setCodigoMision(String codigoMision) {

        if (codigoMision == null || codigoMision.isBlank()) {
            throw new IllegalArgumentException(
                    "El codigo de la mision no puede estar vacio."
            );
        }

        // En MySQL codigo_mision es VARCHAR(50).
        if (codigoMision.length() > 50) {
            throw new IllegalArgumentException(
                    "El codigo de la mision no puede superar los 50 caracteres."
            );
        }

        this.codigoMision = codigoMision.trim();
    }


    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {

        if (progreso < 0) {
            throw new IllegalArgumentException(
                    "El progreso de la mision no puede ser negativo."
            );
        }

        this.progreso = progreso;
    }


    public EstadoMision getEstado() {
        return estado;
    }

    public void setEstado(EstadoMision estado) {

        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado de la mision no puede ser null."
            );
        }

        this.estado = estado;
    }


    @Override
    public String toString() {
        return "ProgresoMision{" +
                "idProgresoMision=" + idProgresoMision +
                ", idPartida=" + idPartida +
                ", codigoMision='" + codigoMision + '\'' +
                ", progreso=" + progreso +
                ", estado=" + estado +
                '}';
    }
}