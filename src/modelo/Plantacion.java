package modelo;

import modelo.enums.EstadoPlantacion;

public class Plantacion {

    private int idPlantacion;
    private int idPartida;
    private int idCultivo;
    private int numeroParcela;
    private long minutoSiembraJuego;
    private EstadoPlantacion estado;
    private boolean activo;

    public Plantacion(int idPlantacion, int idPartida, int idCultivo, int numeroParcela,
                      long minutoSiembraJuego, EstadoPlantacion estado, boolean activo) {
        this.idPlantacion = idPlantacion;
        this.idPartida = idPartida;
        this.idCultivo = idCultivo;
        this.numeroParcela = numeroParcela;
        this.minutoSiembraJuego = minutoSiembraJuego;
        this.estado = estado;
        this.activo = activo;
    }

    public Plantacion(int idPartida, int idCultivo, int numeroParcela, long minutoSiembraJuego) {
        this(0, idPartida, idCultivo, numeroParcela, minutoSiembraJuego,
                EstadoPlantacion.CRECIENDO, true);
    }

    public int getIdPlantacion() {return idPlantacion;}
    public void setIdPlantacion(int idPlantacion) {this.idPlantacion = idPlantacion;}

    public int getIdPartida() {return idPartida;}
    public void setIdPartida(int idPartida) {this.idPartida = idPartida;}

    public int getIdCultivo() {return idCultivo;}
    public void setIdCultivo(int idCultivo) {this.idCultivo = idCultivo;}

    public int getNumeroParcela() {return numeroParcela;}
    public void setNumeroParcela(int numeroParcela) {this.numeroParcela = numeroParcela;}

    public long getMinutoSiembraJuego() {return minutoSiembraJuego;}
    public void setMinutoSiembraJuego(long minutoSiembraJuego) {this.minutoSiembraJuego = minutoSiembraJuego;}

    public EstadoPlantacion getEstado() {return estado;}
    public void setEstado(EstadoPlantacion estado) {this.estado = estado;}

    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    @Override
    public String toString() {
        return "Plantacion{idPlantacion=" + idPlantacion +
                ", idPartida=" + idPartida +
                ", idCultivo=" + idCultivo +
                ", numeroParcela=" + numeroParcela +
                ", minutoSiembraJuego=" + minutoSiembraJuego +
                ", estado=" + estado +
                ", activo=" + activo +
                '}';
    }
}
