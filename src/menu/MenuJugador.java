package menu;

import modelo.Jugador;
import util.Consola;

public class MenuJugador {
    private final Jugador jugador;
    public MenuJugador(Jugador jugador) { this.jugador = jugador; }

    public void mostrar() {
        System.out.println("Menu jugador en construccion");
        Consola.pausar();
    }

}
