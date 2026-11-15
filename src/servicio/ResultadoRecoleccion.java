package servicio;

/**
 * Agrupa los datos obtenidos después de una acción de recolección.
 * Permite devolver al menú el recurso obtenido y su cantidad.
 */
public record ResultadoRecoleccion(
        String nombreItem,
        int cantidad
) {
}