package modelo;

public abstract class ElementoGranja {

    private int id;
    private String nombre;
    private int precioVenta;
    private boolean activo;

    protected ElementoGranja(int id, String nombre, int precioVenta, boolean activo) {

        setId(id);
        setNombre(nombre);
        setPrecioVenta(precioVenta);
        this.activo = activo;
    }


    // Cada hijo informa cuanto cuesta conseguirlo
    // y cuanto tarda en producir.
    public abstract int getPrecioCompra();

    public abstract int getTiempoProduccionMin();


    public int getId() {
        return id;
    }

    public void setId(int id) {

        // El 0 representa un elemento que todavia
        // no fue guardado en MySQL.
        if (id < 0) {
            throw new IllegalArgumentException(
                    "El ID no puede ser negativo."
            );
        }

        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede estar vacio."
            );
        }

        // Cultivo y TipoAnimal utilizan VARCHAR(50).
        if (nombre.length() > 50) {
            throw new IllegalArgumentException(
                    "El nombre no puede superar los 50 caracteres."
            );
        }

        this.nombre = nombre;
    }


    public int getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(int precioVenta) {

        if (precioVenta < 0) {
            throw new IllegalArgumentException(
                    "El precio de venta no puede ser negativo."
            );
        }

        this.precioVenta = precioVenta;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    @Override
    public String toString() {
        return "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precioVenta=" + precioVenta +
                ", activo=" + activo;
    }
}