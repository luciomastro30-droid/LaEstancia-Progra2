package modelo;

public class Inventario {

    private int idInventario;
    private int idPartida;
    private int idItem;
    private int cantidad;

    public Inventario(int idInventario, int idPartida, int idItem, int cantidad) {
        this.idInventario = idInventario;
        this.idPartida = idPartida;
        this.idItem = idItem;
        this.cantidad = cantidad;
    }

    public Inventario(int idPartida, int idItem, int cantidad) {
        this(0, idPartida, idItem, cantidad);
    }

    public int getIdInventario() {return idInventario;}
    public void setIdInventario(int idInventario) {this.idInventario = idInventario;}

    public int getIdPartida() {return idPartida;}
    public void setIdPartida(int idPartida) {this.idPartida = idPartida;}

    public int getIdItem() {return idItem;}
    public void setIdItem(int idItem) {this.idItem = idItem;}

    public int getCantidad() {return cantidad;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    @Override
    public String toString() {
        return "Inventario{idInventario=" + idInventario +
                ", idPartida=" + idPartida +
                ", idItem=" + idItem +
                ", cantidad=" + cantidad +
                '}';
    }
}
