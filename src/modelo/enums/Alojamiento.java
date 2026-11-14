package modelo.enums;

public enum Alojamiento {

    GALLINERO(6),
    ESTABLO(6);

    // Cada alojamiento define la cantidad maxima de animales que admite.
    private final int capacidad;


    Alojamiento(int capacidad) {
        this.capacidad = capacidad;
    }


    public int getCapacidad() {
        return capacidad;
    }
}