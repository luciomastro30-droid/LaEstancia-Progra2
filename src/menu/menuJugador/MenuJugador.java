package menu.menuJugador;

import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import modelo.Jugador;
import modelo.Partida;
import servicio.GranjaServicio;
import servicio.RegistroServicio;
import util.Consola;
import util.Radio;
import util.Reloj;

import java.sql.SQLException;
import java.util.List;

/**
 * Controla el menu principal del jugador dentro de la granja.
 * Se encarga de mostrar las opciones disponibles y delegar cada accion
 * al menu o servicio correspondiente.
 *
 * Si el jugador todavía no tiene una partida activa, permite crearla
 * antes de ingresar al menu principal.
 */
public class MenuJugador {

    // Genera las opciones cada vez que se dibuja el menu para que
    // el texto de la radio refleje su estado actual.
    private List<String> opcionesGranja() {
        return List.of(
                "  MENU DE LA GRANJA",
                "",
                "  1. Plantar",
                "  2. Cosechar",
                "  3. Inventario",
                "  4. Animales",
                "  5. Ir al pueblo",
                "  6. Recolectar",
                "  7. Dormir",
                "  8. Misiones",
                MenuRadio.opcion(9),
                "  0. Guardar y salir"
        );
    }

    private final Jugador jugador;
    private final RegistroServicio registroServicio;
    private final GranjaServicio granjaServicio;
    private final PantallaJugador pantallaJugador;
    private final MenuCultivos menuCultivos;
    private final MenuInventario menuInventario;
    private final MenuAnimales menuAnimales;
    private final MenuPueblo menuPueblo;
    private final MenuRecoleccion menuRecoleccion;
    private final MenuMisiones menuMisiones;
    private final MenuEvento menuEvento;

    // Resultado de la ultima accion; se muestra en la pantalla con "> ".
    private String mensaje;


    public MenuJugador(Jugador jugador) {
        this.jugador = jugador;
        this.registroServicio = new RegistroServicio();
        this.granjaServicio = new GranjaServicio();
        this.pantallaJugador = new PantallaJugador();

        this.menuCultivos =
                new MenuCultivos(
                        pantallaJugador
                );

        this.menuInventario =
                new MenuInventario(
                        pantallaJugador
                );

        this.menuAnimales =
                new MenuAnimales(
                        pantallaJugador
                );

        this.menuPueblo =
                new MenuPueblo(
                        pantallaJugador
                );

        this.menuRecoleccion =
                new MenuRecoleccion(
                        pantallaJugador
                );

        this.menuMisiones =
                new MenuMisiones(
                        pantallaJugador
                );

        this.menuEvento =
                new MenuEvento(
                        pantallaJugador
                );
    }


    public void mostrar() {

        if (!jugador.tienePartidaActual()) {

            System.out.println(
                    "No tienes ninguna partida activa. Vamos a crear una nueva"
            );

            String nombreGranja =
                    Consola.leerTexto(
                            "Escriba el nombre de la granja: "
                    );

            try {

                Partida partida =
                        registroServicio.crearPartidaNueva(
                                jugador.getIdUsuario(),
                                nombreGranja
                        );

                jugador.setPartidaActual(partida);

            } catch (AccionNoPermitidaException | SQLException e) {

                System.out.println(
                        "No se pudo crear la partida: "
                                + e.getMessage()
                );

                return;
            }
        }

        // La radio suena mientras se juega en la granja.
        Radio.encender();
        int opcion;

        do {

            Partida partida =
                    jugador.getPartidaActual();

            dibujarGranja(
                    opcionesGranja(),
                    partida
            );

            opcion =
                    Consola.leerEntero(
                            "Opcion: ",
                            0,
                            9
                    );

            mensaje = null;

            switch (opcion) {

                case 1 ->
                        mensaje =
                                menuCultivos.plantar(
                                        partida
                                );

                case 2 ->
                        mensaje =
                                menuCultivos.cosechar(
                                        partida
                                );

                case 3 ->
                        menuInventario.mostrar(
                                partida
                        );

                case 4 ->
                        menuAnimales.mostrar(
                                partida
                        );

                case 5 ->
                        menuPueblo.mostrar(
                                partida
                        );

                case 6 ->
                        menuRecoleccion.mostrar(
                                partida
                        );

                case 7 ->
                        dormir(
                                partida
                        );

                case 8 ->
                        menuMisiones.mostrar(
                                partida
                        );

                case 9 ->
                        mensaje = MenuRadio.alternar();

                case 0 -> {

                    if (!guardar(
                            partida
                    )) {

                        opcion = -1;
                    }
                }
            }

        } while (opcion != 0);
        // Al salir de la granja se apaga la radio.
        Radio.apagar();
    }


    /*
     * Dibuja la pantalla principal de la granja.
     */
    private void dibujarGranja(
            List<String> opciones,
            Partida partida
    ) {

        mensaje =
                pantallaJugador.mostrar(
                        opciones,
                        partida,
                        mensaje
                );
    }


    /*
     * Duerme hasta el siguiente dia.
     */
    private void dormir(
            Partida partida
    ) {

        try {

            granjaServicio.dormir(
                    partida
            );

            Reloj reloj =
                    new Reloj(
                            partida.getMinutoJuegoTotal()
                    );

            mensaje =
                    "Dormiste. Ahora es "
                            + reloj.getTexto()
                            + ".";

            /*
             * Dormir avanza el tiempo, por eso
             * puede aparecer un evento.
             */
            menuEvento.intentarMostrar(
                    partida
            );

        } catch (SQLException e) {

            mensaje =
                    "No se pudo dormir: "
                            + e.getMessage();
        }
    }


    /*
     * Guarda el estado actual de la partida.
     */
    private boolean guardar(
            Partida partida
    ) {

        try {

            boolean guardado =
                    new PartidaDao().modificar(
                            partida
                    );

            if (!guardado) {

                System.out.println(
                        "No se pudo guardar la partida."
                );

                return false;
            }

            System.out.println(
                    "Partida guardada."
            );

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar la partida: "
                            + e.getMessage()
            );

            return false;
        }
    }
}