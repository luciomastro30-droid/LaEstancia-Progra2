package modelo;

public class TipoAnimal extends ElementoGranja{

    private String descripcion;
    private int precioCompra;
    private Integer idItemProducto; //puede ser null;
    private int cantidadProducto;
    private int tiempoProduccion;

    public TipoAnimal(int idTipoAnimal, String nombre, int precioVenta, boolean activo,
                      String descripcion, int precioCompra, Integer idItemProducto,
                      int cantidadProducto, int tiempoProduccion) {
        super(idTipoAnimal, nombre, precioVenta, activo);
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.idItemProducto = idItemProducto;
        this.cantidadProducto = cantidadProducto;
        this.tiempoProduccion = tiempoProduccion;
    }

    public TipoAnimal(String nombre, String descripcion, int precioCompra, int precioVenta,
                      Integer idItemProducto, int cantidadProducto, int tiempoProduccionMin) {
        this(0, nombre, precioVenta, true, descripcion,
                precioCompra, idItemProducto, cantidadProducto, tiempoProduccionMin);
    }

    @Override
    public int getPrecioCompra() {return precioCompra;}

    @Override
    public int getTiempoProduccionMin() {return tiempoProduccion;}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecioCompra(int precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Integer getIdItemProducto() {
        return idItemProducto;
    }

    public void setIdItemProducto(Integer idItemProducto) {
        this.idItemProducto = idItemProducto;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public int getTiempoProduccion() {
        return tiempoProduccion;
    }

    public void setTiempoProduccion(int tiempoProduccion) {
        this.tiempoProduccion = tiempoProduccion;
    }
}
