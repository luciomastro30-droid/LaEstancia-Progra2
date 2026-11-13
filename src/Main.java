import menu.MenuPrincipal;
import menu.menuAdmin.MenuAdmin;
import modelo.Administrador;
import util.Seguridad;

void main() {

    // Creo un administrador temporal solamente para probar el menu.
    Administrador administrador = new Administrador(
            "adminPrueba",
            "admin@prueba.com",
            Seguridad.hashear("1234")
    );

    // Abro directamente el menu del administrador.
    MenuAdmin menuAdmin = new MenuAdmin(administrador);

    menuAdmin.mostrar();
}

