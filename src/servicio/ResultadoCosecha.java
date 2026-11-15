package servicio;

/**
 * Agrupa los datos obtenidos al realizar una cosecha.
 * Permite que el servicio devuelva varios valores juntos
 * sin encargarse de construir el texto que luego mostrará el menú.
 */
public record ResultadoCosecha(
        String nombreCultivo,
        int cantidad,
        int experiencia,
        boolean subioNivel
) {
}