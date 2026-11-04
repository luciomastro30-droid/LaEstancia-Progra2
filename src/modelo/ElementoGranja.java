package modelo;

public abstract class ElementoGranja {

    private int id;
    private String nombre;
    private int precioVenta;
    private boolean activo;

    protected ElementoGranja(int id, String nombre, int precioVenta, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.activo = activo;
    }

    //Cada hijo informa cuanto cuesta conseguirlo y cuanto tarda en producir
    public abstract int getPrecioCompra();
    public abstract int getTiempoProduccionMin();

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public int getPrecioVenta() {return precioVenta;}
    public void setPrecioVenta(int precioVenta) {this.precioVenta = precioVenta;}

    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    //Solo los datos comunes: cada hija agrega su nombre de clase y sus atributos
    @Override
    public String toString() {
        return "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precioVenta=" + precioVenta +
                ", activo=" + activo;
    }
}
