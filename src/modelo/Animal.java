package modelo;

import java.time.LocalDateTime;

public class Animal {

    private static final int HAMBRE_INICIAL = 100;

    private int idAnimal;
    private int idPartida;
    private int idTipoAnimal;
    private String nombre;
    private int hambre;
    private Long minutoUltimaAlimentacion; // Long porq puede ser null;
    private Long minutoUltimaProduccion; // Puede ser null;
    private LocalDateTime fechaAdquisicion;
    private boolean activo;

    public Animal(int idAnimal, int idPartida, int idTipoAnimal, String nombre, int hambre,
                  Long minutoUltimaAlimentacion, Long minutoUltimaProduccion,
                  LocalDateTime fechaAdquisicion, boolean activo) {
        this.idAnimal = idAnimal;
        this.idPartida = idPartida;
        this.idTipoAnimal = idTipoAnimal;
        this.nombre = nombre;
        this.hambre = hambre;
        this.minutoUltimaAlimentacion = minutoUltimaAlimentacion;
        this.minutoUltimaProduccion = minutoUltimaProduccion;
        this.fechaAdquisicion = fechaAdquisicion;
        this.activo = activo;
    }

    public Animal(int idPartida, int idTipoAnimal, String nombre) {
        this(0, idPartida, idTipoAnimal, nombre, HAMBRE_INICIAL, null, null, LocalDateTime.now(), true);
    }

    public int getIdAnimal() {return idAnimal;}
    public void setIdAnimal(int idAnimal) {this.idAnimal = idAnimal;}

    public int getIdPartida() {return idPartida;}
    public void setIdPartida(int idPartida) {this.idPartida = idPartida;}

    public int getIdTipoAnimal() {return idTipoAnimal;}
    public void setIdTipoAnimal(int idTipoAnimal) {this.idTipoAnimal = idTipoAnimal;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public int getHambre() {return hambre;}
    public void setHambre(int hambre) {this.hambre = hambre;}

    public Long getMinutoUltimaAlimentacion() {return minutoUltimaAlimentacion;}
    public void setMinutoUltimaAlimentacion(Long minutoUltimaAlimentacion) {this.minutoUltimaAlimentacion = minutoUltimaAlimentacion;}

    public Long getMinutoUltimaProduccion() {return minutoUltimaProduccion;}
    public void setMinutoUltimaProduccion(Long minutoUltimaProduccion) {this.minutoUltimaProduccion = minutoUltimaProduccion;}

    public LocalDateTime getFechaAdquisicion() {return fechaAdquisicion;}
    public void setFechaAdquisicion(LocalDateTime fechaAdquisicion) {this.fechaAdquisicion = fechaAdquisicion;}

    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    @Override
    public String toString() {
        return "Animal{idAnimal=" + idAnimal +
                ", idPartida=" + idPartida +
                ", idTipoAnimal=" + idTipoAnimal +
                ", nombre='" + nombre + '\'' +
                ", hambre=" + hambre +
                ", minutoUltimaAlimentacion=" + minutoUltimaAlimentacion +
                ", minutoUltimaProduccion=" + minutoUltimaProduccion +
                ", fechaAdquisicion=" + fechaAdquisicion +
                ", activo=" + activo +
                '}';
    }
}
