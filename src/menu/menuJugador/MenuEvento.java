package menu.menuJugador;

import dao.PartidaDao;
import modelo.Partida;
import modelo.eventos.Evento;
import modelo.eventos.ResultadoEvento;
import servicio.EventoServicio;
import util.Consola;
import vista.DibujoEvento;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MenuEvento {

    private final EventoServicio eventoServicio;
    private final PantallaJugador pantallaJugador;
    private final PartidaDao partidaDao;
    private final Random random;


    public MenuEvento(PantallaJugador pantallaJugador) {
        this.eventoServicio = new EventoServicio();
        this.pantallaJugador = pantallaJugador;
        this.partidaDao = new PartidaDao();
        this.random = new Random();
    }


    /*
     * Intenta generar un evento aleatorio.
     * Si no ocurre ninguno, devuelve null.
     */
    public String intentarMostrar(Partida partida) {

        int monedasAntes = partida.getMonedas();
        int experienciaAntes = partida.getExperiencia();
        int nivelAntes = partida.getNivel();
        Integer diaUltimoEventoAntes = partida.getDiaUltimoEvento();

        Evento evento =
                eventoServicio.intentarGenerarEvento(
                        partida,
                        random
                );

        if (evento == null) {
            return null;
        }

        List<String> opciones =
                new ArrayList<>();

        opciones.add("  EVENTO");
        opciones.add("");
        opciones.add("  " + evento.getNombre());
        opciones.add("");

        agregarOpcion(
                opciones,
                1,
                evento.getTextoOpcion1()
        );

        agregarOpcion(
                opciones,
                2,
                evento.getTextoOpcion2()
        );

        pantallaJugador.mostrar(
                opciones,
                partida,
                evento.getDescripcion(),
                DibujoEvento.dibujar(
                        evento
                )
        );

        int opcion =
                Consola.leerEntero(
                        "Opcion: ",
                        1,
                        2
                );

        ResultadoEvento resultado =
                evento.resolverOpcion(
                        opcion,
                        partida,
                        random
                );

        try {

            boolean guardado =
                    partidaDao.modificar(
                            partida
                    );

            if (!guardado) {

                restaurarPartida(
                        partida,
                        monedasAntes,
                        experienciaAntes,
                        nivelAntes,
                        diaUltimoEventoAntes
                );

                String error =
                        "No se pudo guardar el resultado del evento.";

                pantallaJugador.mostrar(
                        List.of(
                                "  ERROR",
                                "",
                                "  Enter para continuar"
                        ),
                        partida,
                        error,
                        DibujoEvento.dibujar(
                                evento
                        )
                );

                Consola.pausar();

                return error;
            }

        } catch (SQLException e) {

        restaurarPartida(
                partida,
                monedasAntes,
                experienciaAntes,
                nivelAntes,
                diaUltimoEventoAntes
        );

        String error =
                "No se pudo guardar el evento: "
                        + e.getMessage();

        pantallaJugador.mostrar(
                List.of(
                        "  ERROR",
                        "",
                        "  Enter para continuar"
                ),
                partida,
                error,
                DibujoEvento.dibujar(
                        evento
                )
        );

        Consola.pausar();

        return error;
    }

        String mensaje =
                resultado.getMensaje();

        if (resultado.getCambioMonedas() != 0) {

            mensaje +=
                    " Monedas: "
                            + formatearCambio(
                            resultado.getCambioMonedas()
                    )
                            + ".";
        }

        if (resultado.getCambioExperiencia() != 0) {

            mensaje +=
                    " XP: "
                            + formatearCambio(
                            resultado.getCambioExperiencia()
                    )
                            + ".";
        }

        pantallaJugador.mostrar(
                List.of(
                        "  EVENTO RESUELTO",
                        "",
                        "  Enter para continuar"
                ),
                partida,
                mensaje,
                DibujoEvento.dibujar(
                        evento
                )
        );

        Consola.pausar();

        return mensaje;
    }


    /*
     * Divide una opcion larga en varios renglones
     * para que no se corte en la pantalla.
     */
    private void agregarOpcion(
            List<String> opciones,
            int numero,
            String texto
    ) {

        final int ANCHO_TEXTO = 21;

        String prefijo =
                "  " + numero + ". ";

        String sangria =
                "     ";

        String[] palabras =
                texto.split(" ");

        StringBuilder renglon =
                new StringBuilder(
                        prefijo
                );

        int anchoDisponible =
                ANCHO_TEXTO;

        for (String palabra : palabras) {

            if (renglon.length()
                    + palabra.length()
                    + 1
                    > prefijo.length()
                    + anchoDisponible) {

                opciones.add(
                        renglon.toString()
                );

                renglon =
                        new StringBuilder(
                                sangria
                        );

                renglon.append(
                        palabra
                );

            } else {

                if (renglon.length()
                        > prefijo.length()) {

                    renglon.append(" ");
                }

                renglon.append(
                        palabra
                );
            }
        }

        opciones.add(
                renglon.toString()
        );
    }


    private void restaurarPartida(
            Partida partida,
            int monedas,
            int experiencia,
            int nivel,
            Integer diaUltimoEvento
    ) {

        partida.setMonedas(
                monedas
        );

        partida.setNivel(
                nivel
        );

        partida.setExperiencia(
                experiencia
        );

        partida.setDiaUltimoEvento(
                diaUltimoEvento
        );
    }


    private String formatearCambio(
            int cambio
    ) {

        if (cambio > 0) {
            return "+" + cambio;
        }

        return String.valueOf(
                cambio
        );
    }
}