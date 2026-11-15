package modelo.enums;

/**
 * Define los tipos de alojamiento disponibles para los animales.
 * Cada alojamiento establece la cantidad máxima de animales
 * que puede contener.
 */
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