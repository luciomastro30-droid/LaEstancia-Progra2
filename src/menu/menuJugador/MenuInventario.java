package menu.menuJugador;

import modelo.Item;
import modelo.Partida;
import servicio.InventarioServicio;
import util.Consola;
import vista.DibujoInventario;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Controla la visualizacion del inventario del jugador.
 * Obtiene el contenido mediante InventarioServicio y delega
 * el dibujo detallado del inventario a DibujoInventario.
 */
public class MenuInventario {

    private final PantallaJugador pantallaJugador;
    private final InventarioServicio inventarioServicio;


    public MenuInventario(PantallaJugador pantallaJugador) {
        this.pantallaJugador = pantallaJugador;
        this.inventarioServicio = new InventarioServicio();
    }


    /*
     * Muestra el inventario completo de la partida, agrupado por tipo y con el precio de venta.
     * La columna de la derecha solo entra un resumen; aca se ve todo.
     */
    public void mostrar(Partida partida) {

        List<String> opciones = List.of(
                "  INVENTARIO",
                "",
                "  Precio = lo que te",
                "  pagan en el mercado",
                "  por cada uno.",
                "",
                "  Enter para volver"
        );

        Map<Item, Integer> contenido;
        try {
            contenido = inventarioServicio.listarContenido(partida);
        } catch (SQLException e) {
            pantallaJugador.mostrar(opciones, partida, "Error al cargar el inventario: " + e.getMessage());
            Consola.pausar();
            return;
        }

        pantallaJugador.mostrar(opciones, partida, "Inventario completo de la granja.",
                DibujoInventario.dibujarDetalle(contenido));

        Consola.pausar();
    }
}