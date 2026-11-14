package servicio;

// clase cortita solo para llevar datos
// Sirve para que el servicio le devuelva al menú varios datos juntos sin armar texto,
// porque el texto lo arma el menú.
public record ResultadoCosecha(String nombreCultivo, int cantidad, int experiencia, boolean subioNivel) {
}


