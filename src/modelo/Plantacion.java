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


    public Plantacion(int idPlantacion, int idPartida,
                      int idCultivo, int numeroParcela,
                      long minutoSiembraJuego,
                      EstadoPlantacion estado,
                      boolean activo) {

        setIdPlantacion(idPlantacion);
        setIdPartida(idPartida);
        setIdCultivo(idCultivo);
        setNumeroParcela(numeroParcela);
        setMinutoSiembraJuego(minutoSiembraJuego);
        setEstado(estado);

        this.activo = activo;
    }

    public Plantacion(int idPartida, int idCultivo, int numeroParcela, long minutoSiembraJuego) {
        this(0, idPartida, idCultivo, numeroParcela, minutoSiembraJuego,
                EstadoPlantacion.CRECIENDO, true);
    }


    public int getIdPlantacion() {
        return idPlantacion;
    }

    public void setIdPlantacion(int idPlantacion) {

        // El 0 representa una plantacion nueva
        // que todavia no fue insertada en MySQL.
        if (idPlantacion < 0) {
            throw new IllegalArgumentException(
                    "El ID de la plantacion no puede ser negativo."
            );
        }

        this.idPlantacion = idPlantacion;
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


    public int getIdCultivo() {
        return idCultivo;
    }

    public void setIdCultivo(int idCultivo) {

        if (idCultivo <= 0) {
            throw new IllegalArgumentException(
                    "El ID del cultivo debe ser mayor a 0."
            );
        }

        this.idCultivo = idCultivo;
    }


    public int getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {

        if (numeroParcela <= 0) {
            throw new IllegalArgumentException(
                    "El numero de parcela debe ser mayor a 0."
            );
        }

        this.numeroParcela = numeroParcela;
    }


    public long getMinutoSiembraJuego() {
        return minutoSiembraJuego;
    }

    public void setMinutoSiembraJuego(long minutoSiembraJuego) {

        if (minutoSiembraJuego < 0) {
            throw new IllegalArgumentException(
                    "El minuto de siembra no puede ser negativo."
            );
        }

        this.minutoSiembraJuego = minutoSiembraJuego;
    }


    public EstadoPlantacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPlantacion estado) {

        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado de la plantacion no puede ser null."
            );
        }

        this.estado = estado;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Plantacion{" +
                "idPlantacion=" + idPlantacion +
                ", idPartida=" + idPartida +
                ", idCultivo=" + idCultivo +
                ", numeroParcela=" + numeroParcela +
                ", minutoSiembraJuego=" + minutoSiembraJuego +
                ", estado=" + estado +
                ", activo=" + activo +
                '}';
    }
}