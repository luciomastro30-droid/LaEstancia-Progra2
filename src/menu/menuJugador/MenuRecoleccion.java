package menu.menuJugador;

import excepciones.AccionNoPermitidaException;
import modelo.Partida;
import modelo.enums.DefinicionMision;
import modelo.enums.TipoObjetivo;
import servicio.MisionServicio;
import servicio.RecoleccionServicio;
import servicio.ResultadoRecoleccion;
import util.Consola;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class MenuRecoleccion {

    private final RecoleccionServicio recoleccionServicio;
    private final MisionServicio misionServicio;
    private final PantallaJugador pantallaJugador;
    private final MenuEvento menuEvento;
    private final Random random;

    private String mensaje;


    public MenuRecoleccion(
            PantallaJugador pantallaJugador
    ) {

        this.recoleccionServicio =
                new RecoleccionServicio();

        this.misionServicio =
                new MisionServicio();

        this.pantallaJugador =
                pantallaJugador;

        this.menuEvento =
                new MenuEvento(
                        pantallaJugador
                );

        this.random =
                new Random();
    }


    public void mostrar(
            Partida partida
    ) {

        int opcion;

        do {

            List<String> opciones =
                    List.of(
                            "  RECOLECTAR",
                            "",
                            "  1. Talar",
                            "  2. Picar",
                            "  3. Pescar",
                            "",
                            "  Cada accion tarda",
                            "  2 horas. Solo de",
                            "  06:00 a 20:00.",
                            "  0. Volver"
                    );

            mensaje =
                    pantallaJugador.mostrar(
                            opciones,
                            partida,
                            mensaje
                    );

            opcion =
                    Consola.leerEntero(
                            "Opcion: ",
                            0,
                            3
                    );

            if (opcion == 0) {

                mensaje = null;

                return;
            }

            try {

                ResultadoRecoleccion resultado;

                TipoObjetivo tipoObjetivo;

                switch (opcion) {

                    case 1 -> {

                        resultado =
                                recoleccionServicio.talar(
                                        partida
                                );

                        tipoObjetivo =
                                TipoObjetivo.TALAR;
                    }

                    case 2 -> {

                        resultado =
                                recoleccionServicio.picar(
                                        partida
                                );

                        tipoObjetivo =
                                TipoObjetivo.PICAR;
                    }

                    case 3 -> {

                        resultado =
                                recoleccionServicio.pescar(
                                        partida,
                                        random
                                );

                        tipoObjetivo =
                                TipoObjetivo.PESCAR;
                    }

                    default -> {
                        return;
                    }
                }

                /*
                 * En la pesca puede no picar ningun pez.
                 * Igual pasan los 60 minutos.
                 */
                if (resultado.cantidad() == 0) {

                    mensaje =
                            "No conseguiste nada. Pasaron "
                                    + RecoleccionServicio.MINUTOS_RECOLECTAR
                                    + " minutos.";

                } else {

                    mensaje =
                            "Conseguiste "
                                    + resultado.cantidad()
                                    + " "
                                    + resultado.nombreItem()
                                    + ".";

                    registrarProgresoMision(
                            partida,
                            tipoObjetivo,
                            resultado.cantidad()
                    );
                }

                /*
                 * Despues de una accion valida puede aparecer
                 * un evento aleatorio.
                 */
                menuEvento.intentarMostrar(
                        partida
                );

            }catch (AccionNoPermitidaException | SQLException e) {

                mensaje =
                        "No se pudo recolectar: "
                                + e.getMessage();
            }

        } while (opcion != 0);
    }


    private void registrarProgresoMision(
            Partida partida,
            TipoObjetivo tipoObjetivo,
            int cantidad
    ) {

        try {

            List<DefinicionMision> completadas =
                    misionServicio.registrarProgreso(
                            partida,
                            tipoObjetivo,
                            cantidad
                    );

            mensaje +=
                    mensajeMisionesCompletadas(
                            completadas
                    );

        } catch (SQLException e) {

            mensaje +=
                    " No se pudo actualizar el progreso de las misiones.";
        }
    }


    private String mensajeMisionesCompletadas(
            List<DefinicionMision> completadas
    ) {

        StringBuilder texto =
                new StringBuilder();

        for (DefinicionMision mision
                : completadas) {

            texto.append(
                    " Completaste la mision de "
            );

            texto.append(
                    mision.getNpc()
            );

            texto.append(".");
        }

        return texto.toString();
    }
}