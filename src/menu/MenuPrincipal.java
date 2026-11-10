package menu;

import excepciones.AccionNoPermitidaException;
import excepciones.LoginInvalidoException;
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

    public void mostrar(){
        int opcion;

        do {
            System.out.println("=== LA ESTANCIA ===\n" +
                    "1. Iniciar sesion\n" +
                    "2. Registrarse\n" +
                    "0. Salir");
            opcion = Consola.leerEntero("Opcion: ", 0, 2);
            switch (opcion) {
                case 1 -> iniciarSesion();
                case 2 -> registrarse();
                case 0 -> System.out.println("Saliendo...");
            }
        } while (opcion != 0);
    }

    private void iniciarSesion(){

        String nombre = Consola.leerTexto("Usuario: ");
        String contrasenia = Consola.leerTexto("Contraseña: ");

        try {
            Usuario usuario = loginServicio.iniciarSesion(nombre,contrasenia);
            System.out.println("Bienvenido " + nombre);

            if(usuario instanceof Jugador jugador){
                MenuJugador menuJugador = new MenuJugador(jugador);
                menuJugador.mostrar();
            } else if (usuario instanceof Administrador administrador) {
                MenuAdmin menuAdmin = new MenuAdmin(administrador);
                menuAdmin.mostrar();
            }

        } catch (SQLException e) {
            System.out.println("Error de conexion en la base de datos");
        } catch (LoginInvalidoException e) {
            System.out.println(e.getMessage());
        }
    }
    private void registrarse(){

        String nombre = Consola.leerTexto("Escriba un nombre de usuario: ");
        String contrasenia = Consola.leerTexto("Escriba una contraseña: ");
        String email = Consola.leerTextoOpcional("Escriba su email (*Opcional): ");
        String nombreGranja = Consola.leerTexto("Escriba el nombre de la granja: ");

        try {
            Jugador jugador = registroServicio.registrar(nombre,email,contrasenia,nombreGranja);
            System.out.println("Registro exitoso. Tu granja " + nombreGranja + " te espera");
            MenuJugador menuJugador = new MenuJugador(jugador);
            menuJugador.mostrar();

        } catch (AccionNoPermitidaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexion en la base de datos");
        }

    }

}
