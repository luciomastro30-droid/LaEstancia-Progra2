package menu;

import modelo.Administrador;
import util.Consola;

public class MenuAdmin {

    private final Administrador administrador;

    public MenuAdmin(Administrador administrador) {
        this.administrador = administrador;
    }

    public void mostrar() {
        int opcion;

        do {
            System.out.println("""
                    
                    === MENU ADMINISTRADOR ===
                    1. Gestionar items
                    2. Gestionar cultivos
                    3. Gestionar tipos de animales
                    4. Gestionar usuarios
                    0. Cerrar sesion
                    """);

            opcion = Consola.leerEntero("Opcion: ", 0, 4);

            switch (opcion) {
                case 1 -> gestionarItems();

                // Todavia no implementamos estas opciones.
                case 2 -> System.out.println("Gestion de cultivos en construccion.");
                case 3 -> System.out.println("Gestion de tipos de animales en construccion.");
                case 4 -> System.out.println("Gestion de usuarios en construccion.");

                case 0 -> System.out.println("Cerrando sesion...");
            }

        } while (opcion != 0);
    }

    private void gestionarItems() {
        MenuItemsAdmin menuItems = new MenuItemsAdmin();
        menuItems.mostrar();
    }
}