package menu.menuJugador;

import excepciones.AccionNoPermitidaException;
import excepciones.StockInsuficienteException;
import modelo.Cultivo;
import modelo.Partida;
import modelo.enums.DefinicionMision;
import modelo.enums.TipoObjetivo;
import servicio.GranjaServicio;
import servicio.MisionServicio;
import servicio.ResultadoCosecha;
import util.Consola;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuCultivos {

    private final GranjaServicio granjaServicio;
    private final MisionServicio misionServicio;
    private final PantallaJugador pantallaJugador;
    private final MenuEvento menuEvento;


    public MenuCultivos(PantallaJugador pantallaJugador) {
        this.granjaServicio = new GranjaServicio();
        this.misionServicio = new MisionServicio();
        this.pantallaJugador = pantallaJugador;
        this.menuEvento = new MenuEvento(pantallaJugador);
    }


    /*
     * Permite plantar una semilla en una parcela disponible.
     */
    public String plantar(Partida partida) {

        Map<Cultivo, Integer> semillas;

        try {

            semillas =
                    granjaServicio.semillasDisponibles(
                            partida
                    );

        } catch (SQLException e) {

            return "Error al buscar semillas: "
                    + e.getMessage();
        }

        if (semillas.isEmpty()) {

            return "No tenes semillas. Compralas en el pueblo.";
        }

        // Paso las claves a una lista para poder elegirlas por numero.
        List<Cultivo> cultivos =
                new ArrayList<>(
                        semillas.keySet()
                );

        List<String> opciones =
                new ArrayList<>();

        opciones.add("  PLANTAR");
        opciones.add("");

        for (int i = 0;
             i < cultivos.size();
             i++) {

            Cultivo cultivo =
                    cultivos.get(i);

            opciones.add(
                    String.format(
                            "  %d. %-14s x%d",
                            i + 1,
                            cultivo.getNombre(),
                            semillas.get(cultivo)
                    )
            );
        }

        opciones.add("");
        opciones.add("  0. Volver");

        pantallaJugador.mostrar(
                opciones,
                partida,
                "Elegi una semilla y despues la parcela"
        );

        int eleccion =
                Consola.leerEntero(
                        "Semilla: ",
                        0,
                        cultivos.size()
                );

        if (eleccion == 0) {
            return null;
        }

        Cultivo elegido =
                cultivos.get(
                        eleccion - 1
                );

        int parcela =
                Consola.leerEntero(
                        "Parcela (1-"
                                + partida.getParcelasDesbloqueadas()
                                + "): ",
                        1,
                        partida.getParcelasDesbloqueadas()
                );

        try {

            granjaServicio.plantar(
                    partida,
                    elegido,
                    parcela
            );

            String mensaje =
                    "Plantaste "
                            + elegido.getNombre()
                            + " en la parcela "
                            + parcela
                            + ".";

            /*
             * Despues de plantar puede aparecer
             * un evento aleatorio.
             */
            menuEvento.intentarMostrar(
                    partida
            );

            return mensaje;

        } catch (AccionNoPermitidaException
                 | StockInsuficienteException
                 | SQLException e) {

            return "No se pudo plantar: "
                    + e.getMessage();
        }
    }


    /*
     * Permite cosechar una parcela que ya este lista.
     */
    public String cosechar(Partida partida) {

        Map<Integer, Cultivo> listas;

        try {

            listas =
                    granjaServicio.parcelasListas(
                            partida
                    );

        } catch (SQLException e) {

            return "Error al buscar las parcelas: "
                    + e.getMessage();
        }

        if (listas.isEmpty()) {

            return "No hay cultivos listos para cosechar.";
        }

        /*
         * Paso los numeros de parcela a una lista
         * para elegirlos por numero de opcion.
         */
        List<Integer> parcelas =
                new ArrayList<>(
                        listas.keySet()
                );

        List<String> opciones =
                new ArrayList<>();

        opciones.add("  COSECHAR");
        opciones.add("");

        for (int i = 0;
             i < parcelas.size();
             i++) {

            int numeroParcela =
                    parcelas.get(i);

            opciones.add(
                    String.format(
                            "  %d. Parcela %d: %s",
                            i + 1,
                            numeroParcela,
                            listas.get(
                                    numeroParcela
                            ).getNombre()
                    )
            );
        }

        opciones.add("");
        opciones.add("  0. Volver");

        pantallaJugador.mostrar(
                opciones,
                partida,
                "Elegi que parcela cosechar"
        );

        int eleccion =
                Consola.leerEntero(
                        "Opcion: ",
                        0,
                        parcelas.size()
                );

        if (eleccion == 0) {
            return null;
        }

        int numeroParcela =
                parcelas.get(
                        eleccion - 1
                );

        try {

            ResultadoCosecha resultado =
                    granjaServicio.cosechar(
                            partida,
                            numeroParcela
                    );

            String mensaje =
                    "Cosechaste "
                            + resultado.cantidad()
                            + " "
                            + resultado.nombreCultivo()
                            + " (+"
                            + resultado.experiencia()
                            + " XP).";

            if (resultado.subioNivel()) {

                mensaje +=
                        " Subiste a nivel "
                                + partida.getNivel()
                                + "!";
            }

            try {

                List<DefinicionMision> completadas =
                        misionServicio.registrarProgreso(
                                partida,
                                TipoObjetivo.COSECHAR,
                                1
                        );

                mensaje +=
                        mensajeMisionesCompletadas(
                                completadas
                        );

            } catch (SQLException e) {

                mensaje +=
                        " No se pudo actualizar el progreso de las misiones.";
            }

            /*
             * Despues de cosechar puede aparecer
             * un evento aleatorio.
             */
            menuEvento.intentarMostrar(
                    partida
            );

            return mensaje;

        } catch (AccionNoPermitidaException
                 | SQLException e) {

            return "No se pudo cosechar: "
                    + e.getMessage();
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