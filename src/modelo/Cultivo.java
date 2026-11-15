package modelo;

/**
 * Representa un tipo de cultivo disponible en el juego.
 * Define su precio, tiempo de crecimiento, recompensa de experiencia
 * y los items asociados a la semilla y a la cosecha.
 */
public class Cultivo extends ElementoGranja {

    private int precioSemilla;
    private int tiempoCrecimientoMin;
    private int experienciaRecompensa;
    private Integer idItemSemilla; // Puede ser null.
    private Integer idItemCosecha; // Puede ser null.


    public Cultivo(int id, String nombre, int precioSemilla, int precioVenta,
                   int tiempoCrecimientoMin, int experienciaRecompensa,
                   Integer idItemSemilla, Integer idItemCosecha,
                   boolean activo) {

        super(id, nombre, precioVenta, activo);

        setPrecioSemilla(precioSemilla);
        setTiempoCrecimientoMin(tiempoCrecimientoMin);
        setExperienciaRecompensa(experienciaRecompensa);
        setIdItemSemilla(idItemSemilla);
        setIdItemCosecha(idItemCosecha);
    }


    public Cultivo(String nombre, int precioSemilla, int precioVenta,
                   int tiempoCrecimientoMin, int experienciaRecompensa,
                   Integer idItemSemilla, Integer idItemCosecha) {

        this(0, nombre, precioSemilla, precioVenta, tiempoCrecimientoMin,
                experienciaRecompensa, idItemSemilla, idItemCosecha, true);
    }


    @Override
    public int getPrecioCompra() {
        return precioSemilla;
    }


    @Override
    public int getTiempoProduccionMin() {
        return tiempoCrecimientoMin;
    }


    public int getPrecioSemilla() {
        return precioSemilla;
    }

    public void setPrecioSemilla(int precioSemilla) {

        if (precioSemilla < 0) {
            throw new IllegalArgumentException(
                    "El precio de la semilla no puede ser negativo."
            );
        }

        this.precioSemilla = precioSemilla;
    }


    public int getTiempoCrecimientoMin() {
        return tiempoCrecimientoMin;
    }

    public void setTiempoCrecimientoMin(int tiempoCrecimientoMin) {

        if (tiempoCrecimientoMin <= 0) {
            throw new IllegalArgumentException(
                    "El tiempo de crecimiento debe ser mayor a 0."
            );
        }

        this.tiempoCrecimientoMin = tiempoCrecimientoMin;
    }


    public int getExperienciaRecompensa() {
        return experienciaRecompensa;
    }

    public void setExperienciaRecompensa(int experienciaRecompensa) {

        if (experienciaRecompensa < 0) {
            throw new IllegalArgumentException(
                    "La experiencia de recompensa no puede ser negativa."
            );
        }

        this.experienciaRecompensa = experienciaRecompensa;
    }


    public Integer getIdItemSemilla() {
        return idItemSemilla;
    }

    public void setIdItemSemilla(Integer idItemSemilla) {

        // Puede ser null porque la relacion es opcional en MySQL.
        // Si existe un ID, tiene que ser positivo.
        if (idItemSemilla != null && idItemSemilla <= 0) {
            throw new IllegalArgumentException(
                    "El ID del item semilla debe ser mayor a 0."
            );
        }

        this.idItemSemilla = idItemSemilla;
    }


    public Integer getIdItemCosecha() {
        return idItemCosecha;
    }

    public void setIdItemCosecha(Integer idItemCosecha) {

        // También puede ser null si el cultivo no tiene un item
        // de cosecha asociado.
        if (idItemCosecha != null && idItemCosecha <= 0) {
            throw new IllegalArgumentException(
                    "El ID del item cosecha debe ser mayor a 0."
            );
        }

        this.idItemCosecha = idItemCosecha;
    }


    @Override
    public String toString() {
        return "Cultivo{" + super.toString() +
                ", precioSemilla=" + precioSemilla +
                ", tiempoCrecimientoMin=" + tiempoCrecimientoMin +
                ", experienciaRecompensa=" + experienciaRecompensa +
                ", idItemSemilla=" + idItemSemilla +
                ", idItemCosecha=" + idItemCosecha +
                '}';
    }
}