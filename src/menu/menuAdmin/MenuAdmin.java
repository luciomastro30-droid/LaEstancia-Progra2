package menu.menuAdmin;

import modelo.Administrador;
import util.Consola;

/**
 * Menu principal del administrador.
 *
 * Su responsabilidad es mostrar las distintas opciones
 * de administracion y abrir el submenu correspondiente.
 */
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

            opcion = Consola.leerEntero(
                    "Opcion: ",
                    0,
                    4
            );

            switch (opcion) {

                case 1 -> {
                    MenuItemsAdmin menuItems =
                            new MenuItemsAdmin();

                    menuItems.mostrar();
                }

                case 2 -> {
                    MenuCultivosAdmin menuCultivos =
                            new MenuCultivosAdmin();

                    menuCultivos.mostrar();
                }

                case 3 -> {
                    MenuTiposAnimalAdmin menuTiposAnimal =
                            new MenuTiposAnimalAdmin();

                    menuTiposAnimal.mostrar();
                }

                case 4 -> {
                    MenuUsuariosAdmin menuUsuarios =
                            new MenuUsuariosAdmin(administrador);

                    menuUsuarios.mostrar();
                }

                case 0 ->
                        System.out.println("Cerrando sesion...");
            }

        } while (opcion != 0);
    }
}