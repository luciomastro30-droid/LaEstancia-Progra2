import java.time.LocalDateTime;

public class Animal {

    private int idAnimal;
    private int idPartida;
    private int idTipoAnimal;
    private String nombre;
    private int hambre;
    private Long minutoUltimaAlimentacion;
    private Long minutoUltimaProduccion;
    private LocalDateTime fechaAdquisicion;
    private boolean activo;


    public Animal(int idAnimal, int idPartida, int idTipoAnimal,
                  String nombre, int hambre,
                  Long minutoUltimaAlimentacion,
                  Long minutoUltimaProduccion,
                  LocalDateTime fechaAdquisicion,
                  boolean activo) {

        setIdAnimal(idAnimal);
        setIdPartida(idPartida);
        setIdTipoAnimal(idTipoAnimal);
        setNombre(nombre);
        setHambre(hambre);
        setMinutoUltimaAlimentacion(minutoUltimaAlimentacion);
        setMinutoUltimaProduccion(minutoUltimaProduccion);

        if (fechaAdquisicion == null) {
            throw new IllegalArgumentException(
                    "La fecha de adquisicion no puede ser null."
            );
        }

        this.fechaAdquisicion = fechaAdquisicion;
        this.activo = activo;
    }


    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {

        // El 0 representa un animal que todavia
        // no fue guardado en la base.
        if (idAnimal < 0) {
            throw new IllegalArgumentException(
                    "El ID del animal no puede ser negativo."
            );
        }

        this.idAnimal = idAnimal;
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


    public int getIdTipoAnimal() {
        return idTipoAnimal;
    }

    public void setIdTipoAnimal(int idTipoAnimal) {

        if (idTipoAnimal <= 0) {
            throw new IllegalArgumentException(
                    "El ID del tipo de animal debe ser mayor a 0."
            );
        }

        this.idTipoAnimal = idTipoAnimal;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        // El nombre puede ser null porque en MySQL no es obligatorio.
        if (nombre != null) {

            if (nombre.isBlank()) {
                throw new IllegalArgumentException(
                        "El nombre del animal no puede estar vacio."
                );
            }

            if (nombre.length() > 50) {
                throw new IllegalArgumentException(
                        "El nombre del animal no puede superar los 50 caracteres."
                );
            }

            nombre = nombre.trim();
        }

        this.nombre = nombre;
    }


    public int getHambre() {
        return hambre;
    }

    public void setHambre(int hambre) {

        if (hambre < 0 || hambre > 100) {
            throw new IllegalArgumentException(
                    "El hambre del animal debe estar entre 0 y 100."
            );
        }

        this.hambre = hambre;
    }


    public Long getMinutoUltimaAlimentacion() {
        return minutoUltimaAlimentacion;
    }

    public void setMinutoUltimaAlimentacion(Long minutoUltimaAlimentacion) {

        if (minutoUltimaAlimentacion != null &&
                minutoUltimaAlimentacion < 0) {

            throw new IllegalArgumentException(
                    "El minuto de ultima alimentacion no puede ser negativo."
            );
        }

        this.minutoUltimaAlimentacion = minutoUltimaAlimentacion;
    }


    public Long getMinutoUltimaProduccion() {
        return minutoUltimaProduccion;
    }

    public void setMinutoUltimaProduccion(Long minutoUltimaProduccion) {

        if (minutoUltimaProduccion != null &&
                minutoUltimaProduccion < 0) {

            throw new IllegalArgumentException(
                    "El minuto de ultima produccion no puede ser negativo."
            );
        }

        this.minutoUltimaProduccion = minutoUltimaProduccion;
    }


    public LocalDateTime getFechaAdquisicion() {
        return fechaAdquisicion;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}