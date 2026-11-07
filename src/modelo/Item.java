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

        setIdItem(idItem);
        setNombre(nombre);
        setDescripcion(descripcion);
        setTipo(tipo);
        setPrecioCompra(precioCompra);
        setPrecioVenta(precioVenta);

        this.activo = activo;
    }


    public Item(String nombre, String descripcion, TipoItem tipo,
                int precioCompra, int precioVenta) {

        this(0, nombre, descripcion, tipo, precioCompra, precioVenta, true);
    }


    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {

        // El ID 0 representa un Item que todavia no fue guardado en MySQL.
        if (idItem < 0) {
            throw new IllegalArgumentException(
                    "El ID del item no puede ser negativo."
            );
        }

        this.idItem = idItem;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del item no puede estar vacio."
            );
        }

        // En MySQL el nombre es VARCHAR(80).
        if (nombre.length() > 80) {
            throw new IllegalArgumentException(
                    "El nombre del item no puede superar los 80 caracteres."
            );
        }

        this.nombre = nombre.trim();
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        // La descripcion puede ser null porque la base tambien lo permite.
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


    public TipoItem getTipo() {
        return tipo;
    }

    public void setTipo(TipoItem tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo del item no puede ser null."
            );
        }

        this.tipo = tipo;
    }


    public int getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(int precioCompra) {

        if (precioCompra < 0) {
            throw new IllegalArgumentException(
                    "El precio de compra no puede ser negativo."
            );
        }

        this.precioCompra = precioCompra;
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
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Item item)) {
            return false;
        }

        // Dos Items que todavia no tienen un ID generado
        // no se consideran iguales solamente por tener ID 0.
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