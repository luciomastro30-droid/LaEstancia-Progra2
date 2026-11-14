package menu;

import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import modelo.Jugador;
import modelo.Partida;
import servicio.RegistroServicio;
import util.Consola;
import util.Reloj;

import java.sql.SQLException;

public class MenuJugador {
    private final Jugador jugador;
    private final RegistroServicio registroServicio;

    public MenuJugador(Jugador jugador) {
        this.jugador = jugador;
        this.registroServicio = new RegistroServicio();
    }

    public void mostrar(){

        if(!jugador.tienePartidaActual()) {
            System.out.println("No tienes ninguna partida activa. Vamos a crear una nueva");
            String nombreGranja = Consola.leerTexto("Escriba el nombre de la granja: ");
            try {
                Partida partida = registroServicio.crearPartidaNueva(jugador.getIdUsuario(), nombreGranja);
                jugador.setPartidaActual(partida);

            } catch (AccionNoPermitidaException | SQLException e) {
                System.out.println("No se pudo crear la partida: " + e.getMessage());
                return;
            }
        }

        int opcion = 0;
        do{

            Partida partida = jugador.getPartidaActual();
            Reloj reloj = new Reloj(partida.getMinutoJuegoTotal());
            System.out.println("\n=== " + partida.getNombreGranja() + " === "
                    + reloj.getTexto() + " | Monedas: " + partida.getMonedas()
                    + " | Nivel: " + partida.getNivel());
            System.out.println("""
                    1. Ver granja
                    2. Plantar
                    3. Cosechar
                    4. Inventario
                    5. Ir al pueblo
                    6. Recolectar
                    0. Guardar y cerrar sesion""");
            opcion = Consola.leerEntero("Opcion: ",0 , 6);

            switch (opcion){

                case 1 -> System.out.println("Proximamente");

                case 2 -> System.out.println("Proximamente");

                case 3 -> System.out.println("Proximamente");

                case 4 -> System.out.println("Proximamente");

                case 5 -> System.out.println("Proximamente");

                case 6 -> System.out.println("Proximamente");

                case 0 -> {
                    try {
                        new PartidaDao().modificar(partida);
                        System.out.println("Partida guardada.");
                    } catch (SQLException e) {
                        System.out.println("ERROR EN LA BDD " + e.getMessage());
                    }
                }
            }

        }while (opcion != 0);

    }
}


