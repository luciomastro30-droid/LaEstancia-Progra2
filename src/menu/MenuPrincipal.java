package menu;

import excepciones.AccionNoPermitidaException;
import excepciones.LoginInvalidoException;
import menu.menuAdmin.MenuAdmin;
import menu.menuJugador.MenuJugador;
import modelo.Administrador;
import modelo.Jugador;
import modelo.Usuario;
import servicio.LoginServicio;
import servicio.RegistroServicio;
import util.Consola;

import java.sql.SQLException;

public class MenuPrincipal {

    private final LoginServicio loginServicio = new LoginServicio();
    private final RegistroServicio registroServicio = new RegistroServicio();

    public void mostrar() {
        int opcion;

        do {
            mostrarEncabezado();

            System.out.println("  [1] Iniciar sesion");
            System.out.println("  [2] Registrarse");
            System.out.println("  [0] Salir");

            System.out.println();
            System.out.println("------------------------------------------");

            opcion = Consola.leerEntero("  Seleccione una opcion: ", 0, 2);

            System.out.println();

            switch (opcion) {
                case 1 -> iniciarSesion();
                case 2 -> registrarse();
                case 0 -> mostrarDespedida();
            }

        } while (opcion != 0);
    }

    private void iniciarSesion() {

        System.out.println("==========================================");
        System.out.println("              INICIAR SESION              ");
        System.out.println("==========================================");
        System.out.println();

        String nombre = Consola.leerTexto("  Usuario: ");
        String contrasenia = Consola.leerTexto("  Contraseña: ");

        try {
            Usuario usuario = loginServicio.iniciarSesion(nombre, contrasenia);

            System.out.println();
            System.out.println("------------------------------------------");
            System.out.println("  Bienvenido a La Estancia, " + nombre + "!");
            System.out.println("------------------------------------------");
            System.out.println();

            if (usuario instanceof Jugador jugador) {

                MenuJugador menuJugador = new MenuJugador(jugador);
                menuJugador.mostrar();

            } else if (usuario instanceof Administrador administrador) {

                MenuAdmin menuAdmin = new MenuAdmin(administrador);
                menuAdmin.mostrar();
            }

        } catch (SQLException e) {

            System.out.println();
            System.out.println("[ERROR] No se pudo conectar con la base de datos.");
            System.out.println();

        } catch (LoginInvalidoException e) {

            System.out.println();
            System.out.println("[ERROR] " + e.getMessage());
            System.out.println();
        }
    }

    private void registrarse() {

        System.out.println("==========================================");
        System.out.println("                 REGISTRO                 ");
        System.out.println("==========================================");
        System.out.println();

        System.out.println("  Crea tu cuenta y comienza tu aventura.");
        System.out.println();

        String nombre = Consola.leerTexto("  Nombre de usuario: ");
        String contrasenia = Consola.leerTexto("  Contraseña: ");
        String email = Consola.leerTextoOpcional("  Email (opcional): ");
        String nombreGranja = Consola.leerTexto("  Nombre de la granja: ");

        try {
            Jugador jugador = registroServicio.registrar(
                    nombre,
                    email,
                    contrasenia,
                    nombreGranja
            );

            System.out.println();
            System.out.println("==========================================");
            System.out.println("          REGISTRO COMPLETADO             ");
            System.out.println("==========================================");
            System.out.println();
            System.out.println("  Bienvenido a La Estancia, " + nombre + "!");
            System.out.println("  Tu granja \"" + nombreGranja + "\" te espera.");
            System.out.println();

            MenuJugador menuJugador = new MenuJugador(jugador);
            menuJugador.mostrar();

        } catch (AccionNoPermitidaException e) {

            System.out.println();
            System.out.println("[ERROR] " + e.getMessage());
            System.out.println();

        } catch (SQLException e) {

            System.out.println();
            System.out.println("[ERROR] No se pudo conectar con la base de datos.");
            System.out.println();
        }
    }

    // Encabezado principal del juego.
    private void mostrarEncabezado() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("                                          ");
        System.out.println("               LA ESTANCIA                ");
        System.out.println("                                          ");
        System.out.println("          Simulador de Granja             ");
        System.out.println("==========================================");
        System.out.println();
    }

    private void mostrarDespedida() {

        System.out.println("==========================================");
        System.out.println("                                          ");
        System.out.println("       Gracias por jugar La Estancia      ");
        System.out.println("                                          ");
        System.out.println("               Hasta pronto!              ");
        System.out.println("==========================================");
    }
}