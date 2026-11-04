package modelo;

public class TipoAnimal extends ElementoGranja{

    private String descripcion;
    private int precioCompra;
    private Integer idItemProducto; //puede ser null;
    private int cantidadProducto;
    private int tiempoProduccionMin;

    public TipoAnimal(int idTipoAnimal, String nombre, String descripcion, int precioCompra,
                      int precioVenta, Integer idItemProducto, int cantidadProducto,
                      int tiempoProduccionMin, boolean activo) {
        super(idTipoAnimal, nombre, precioVenta, activo);
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.idItemProducto = idItemProducto;
        this.cantidadProducto = cantidadProducto;
        this.tiempoProduccionMin = tiempoProduccionMin;
    }

    public TipoAnimal(String nombre, String descripcion, int precioCompra, int precioVenta,
                      Integer idItemProducto, int cantidadProducto, int tiempoProduccionMin) {
        this(0, nombre, descripcion, precioCompra, precioVenta,
                idItemProducto, cantidadProducto, tiempoProduccionMin, true);
    }

    @Override
    public int getPrecioCompra() {return precioCompra;}

    @Override
    public int getTiempoProduccionMin() {return tiempoProduccionMin;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public void setPrecioCompra(int precioCompra) {this.precioCompra = precioCompra;}

    public Integer getIdItemProducto() {return idItemProducto;}
    public void setIdItemProducto(Integer idItemProducto) {this.idItemProducto = idItemProducto;}

    public int getCantidadProducto() {return cantidadProducto;}
    public void setCantidadProducto(int cantidadProducto) {this.cantidadProducto = cantidadProducto;}

    public void setTiempoProduccionMin(int tiempoProduccion) {this.tiempoProduccionMin = tiempoProduccion;}

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
