package modelo;

import modelo.enums.TipoItem;

public class Item {

    private int idItem;
    private String nombre;
    private String descripcion;
    private TipoItem tipo;
    private int precioCompra;
    private int precioVenta;
    private boolean activo;

    public Item(int idItem, String nombre, String descripcion, TipoItem tipo,
                int precioCompra, int precioVenta, boolean activo) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.activo = activo;
    }

    public Item(String nombre, String descripcion, TipoItem tipo, int precioCompra, int precioVenta) {
        this(0, nombre, descripcion, tipo, precioCompra, precioVenta, true);
    }

    public int getIdItem() {return idItem;}
    public void setIdItem(int idItem) {this.idItem = idItem;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public TipoItem getTipo() {return tipo;}
    public void setTipo(TipoItem tipo) {this.tipo = tipo;}

    public int getPrecioCompra() {return precioCompra;}
    public void setPrecioCompra(int precioCompra) {this.precioCompra = precioCompra;}

    public int getPrecioVenta() {return precioVenta;}
    public void setPrecioVenta(int precioVenta) {this.precioVenta = precioVenta;}

    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;

        if (idItem == 0 || item.idItem == 0) {
            return false;
        }

        return idItem == item.idItem;
    }

    @Override
    public int hashCode() {return Integer.hashCode(idItem);}

    @Override
    public String toString() {
        return "Item{idItem=" + idItem +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipo=" + tipo +
                ", precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                ", activo=" + activo +
                '}';
    }
}
