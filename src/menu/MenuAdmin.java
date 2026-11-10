package menu;

import modelo.Administrador;
import util.Consola;

public class MenuAdmin {

    private final Administrador administrador;
    public MenuAdmin(Administrador administrador) { this.administrador = administrador; }
    public void mostrar() {
        System.out.println("Menu administrador en construccion");
        Consola.pausar();
    }
}
