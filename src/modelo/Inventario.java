package modelo;

public class Inventario {

    private int idInventario;
    private int idPartida;
    private int idItem;
    private int cantidad;


    public Inventario(int idInventario, int idPartida,
                      int idItem, int cantidad) {

        setIdInventario(idInventario);
        setIdPartida(idPartida);
        setIdItem(idItem);
        setCantidad(cantidad);
    }


    public Inventario(int idPartida, int idItem, int cantidad) {

        this(
                0,
                idPartida,
                idItem,
                cantidad
        );
    }


    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {

        // El 0 representa un registro nuevo que todavia
        // no fue guardado en MySQL.
        if (idInventario < 0) {
            throw new IllegalArgumentException(
                    "El ID del inventario no puede ser negativo."
            );
        }

        this.idInventario = idInventario;
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


    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {

        if (idItem <= 0) {
            throw new IllegalArgumentException(
                    "El ID del item debe ser mayor a 0."
            );
        }

        this.idItem = idItem;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {

        if (cantidad < 0) {
            throw new IllegalArgumentException(
                    "La cantidad del inventario no puede ser negativa."
            );
        }

        this.cantidad = cantidad;
    }


    @Override
    public String toString() {
        return "Inventario{" +
                "idInventario=" + idInventario +
                ", idPartida=" + idPartida +
                ", idItem=" + idItem +
                ", cantidad=" + cantidad +
                '}';
    }
}