package menu.menuJugador;

import modelo.Partida;
import util.Consola;

import java.util.List;

public class MenuInventario {

    private final PantallaJugador pantallaJugador;


    public MenuInventario(PantallaJugador pantallaJugador) {
        this.pantallaJugador = pantallaJugador;
    }


    /*
     * Muestra el inventario actual de la partida.
     */
    public void mostrar(Partida partida) {

        List<String> opciones =
                List.of(
                        "  INVENTARIO",
                        "",
                        "  Tus objetos se",
                        "  muestran a la",
                        "  derecha.",
                        "",
                        "  Enter para volver"
                );

        pantallaJugador.mostrar(
                opciones,
                partida,
                "Inventario actual de la granja."
        );

        Consola.pausar();
    }
}