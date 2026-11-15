package menu.menuJugador;

import modelo.Partida;
import servicio.RecoleccionServicio;
import servicio.ResultadoRecoleccion;
import util.Consola;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class MenuRecoleccion {

    private final RecoleccionServicio recoleccionServicio;
    private final PantallaJugador pantallaJugador;
    private final Random random;

    private String mensaje;


    public MenuRecoleccion(PantallaJugador pantallaJugador) {
        this.recoleccionServicio = new RecoleccionServicio();
        this.pantallaJugador = pantallaJugador;
        this.random = new Random();
    }


    public void mostrar(Partida partida) {

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
                            "  60 minutos.",
                            "",
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

                switch (opcion) {

                    case 1 ->
                            resultado =
                                    recoleccionServicio.talar(
                                            partida
                                    );

                    case 2 ->
                            resultado =
                                    recoleccionServicio.picar(
                                            partida
                                    );

                    case 3 ->
                            resultado =
                                    recoleccionServicio.pescar(
                                            partida,
                                            random
                                    );

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
                }

            } catch (SQLException e) {

                mensaje =
                        "No se pudo recolectar: "
                                + e.getMessage();
            }

        } while (opcion != 0);
    }
}