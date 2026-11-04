package modelo;

import modelo.enums.EstadoMision;

public class ProgresoMision {

    private int idProgresoMision;
    private int idPartida;
    private String codigoMision;
    private int progreso;
    private EstadoMision estado;

    public ProgresoMision(int idProgresoMision, int idPartida, String codigoMision,
                          int progreso, EstadoMision estado) {
        this.idProgresoMision = idProgresoMision;
        this.idPartida = idPartida;
        this.codigoMision = codigoMision;
        this.progreso = progreso;
        this.estado = estado;
    }

    public ProgresoMision(int idPartida, String codigoMision) {
        this(0, idPartida, codigoMision, 0, EstadoMision.ACTIVA);
    }

    public int getIdProgresoMision() {return idProgresoMision;}
    public void setIdProgresoMision(int idProgresoMision) {this.idProgresoMision = idProgresoMision;}

    public int getIdPartida() {return idPartida;}
    public void setIdPartida(int idPartida) {this.idPartida = idPartida;}

    public String getCodigoMision() {return codigoMision;}
    public void setCodigoMision(String codigoMision) {this.codigoMision = codigoMision;}

    public int getProgreso() {return progreso;}
    public void setProgreso(int progreso) {this.progreso = progreso;}

    public EstadoMision getEstado() {return estado;}
    public void setEstado(EstadoMision estado) {this.estado = estado;}

    @Override
    public String toString() {
        return "ProgresoMision{idProgresoMision=" + idProgresoMision +
                ", idPartida=" + idPartida +
                ", codigoMision='" + codigoMision + '\'' +
                ", progreso=" + progreso +
                ", estado=" + estado +
                '}';
    }
}
