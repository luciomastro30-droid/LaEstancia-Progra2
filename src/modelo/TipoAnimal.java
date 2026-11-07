package modelo;

public class TipoAnimal extends ElementoGranja {

    private String descripcion;
    private int precioCompra;
    private Integer idItemProducto;
    private int cantidadProducto;
    private int tiempoProduccionMin;


    public TipoAnimal(int idTipoAnimal, String nombre, String descripcion,
                      int precioCompra, int precioVenta,
                      Integer idItemProducto, int cantidadProducto,
                      int tiempoProduccionMin, boolean activo) {

        super(idTipoAnimal, nombre, precioVenta, activo);

        setDescripcion(descripcion);
        setPrecioCompra(precioCompra);
        setIdItemProducto(idItemProducto);
        setCantidadProducto(cantidadProducto);
        setTiempoProduccionMin(tiempoProduccionMin);
    }


    public TipoAnimal(String nombre, String descripcion,
                      int precioCompra, int precioVenta,
                      Integer idItemProducto, int cantidadProducto,
                      int tiempoProduccionMin) {

        this(0, nombre, descripcion, precioCompra, precioVenta,
                idItemProducto, cantidadProducto, tiempoProduccionMin, true);
    }


    @Override
    public int getPrecioCompra() {
        return precioCompra;
    }


    @Override
    public int getTiempoProduccionMin() {
        return tiempoProduccionMin;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        // La descripcion puede ser null en MySQL.
        if (descripcion != null) {

            if (descripcion.length() > 255) {
                throw new IllegalArgumentException(
                        "La descripcion no puede superar los 255 caracteres."
                );
            }

            descripcion = descripcion.trim();
        }

        this.descripcion = descripcion;
    }


    public void setPrecioCompra(int precioCompra) {

        if (precioCompra < 0) {
            throw new IllegalArgumentException(
                    "El precio de compra no puede ser negativo."
            );
        }

        this.precioCompra = precioCompra;
    }


    public Integer getIdItemProducto() {
        return idItemProducto;
    }

    public void setIdItemProducto(Integer idItemProducto) {

        // Puede ser null si el animal no tiene ningun producto asociado.
        if (idItemProducto != null && idItemProducto <= 0) {
            throw new IllegalArgumentException(
                    "El ID del item producto debe ser mayor a 0."
            );
        }

        this.idItemProducto = idItemProducto;
    }


    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {

        if (cantidadProducto <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de producto debe ser mayor a 0."
            );
        }

        this.cantidadProducto = cantidadProducto;
    }


    public void setTiempoProduccionMin(int tiempoProduccionMin) {

        if (tiempoProduccionMin <= 0) {
            throw new IllegalArgumentException(
                    "El tiempo de produccion debe ser mayor a 0."
            );
        }

        this.tiempoProduccionMin = tiempoProduccionMin;
    }


    @Override
    public String toString() {
        return "TipoAnimal{" + super.toString() +
                ", descripcion='" + descripcion + '\'' +
                ", precioCompra=" + precioCompra +
                ", idItemProducto=" + idItemProducto +
                ", cantidadProducto=" + cantidadProducto +
                ", tiempoProduccionMin=" + tiempoProduccionMin +
                '}';
    }
}