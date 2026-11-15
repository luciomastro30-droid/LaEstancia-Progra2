package modelo.enums;

/**
 * Define las rarezas disponibles para las herramientas.
 * Cada rareza determina cuántas unidades puede obtener
 * la herramienta durante una acción de recolección.
 */
public enum Rareza {

    COMUN(2),
    POCO_COMUN(3),
    RARA(4),
    EPICA(5);

    // Cada rareza define cuantas unidades obtiene la herramienta al recolectar.
    private final int unidadesRecoleccion;


    Rareza(int unidadesRecoleccion) {
        this.unidadesRecoleccion = unidadesRecoleccion;
    }


    public int getUnidadesRecoleccion() {
        return unidadesRecoleccion;
    }
}