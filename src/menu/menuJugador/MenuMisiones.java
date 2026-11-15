package menu.menuJugador;

import excepciones.AccionNoPermitidaException;
import modelo.Partida;
import modelo.ProgresoMision;
import modelo.enums.DefinicionMision;
import servicio.MisionServicio;
import util.Consola;
import vista.DibujoMisiones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuMisiones {

    private final MisionServicio misionServicio;
    private final PantallaJugador pantallaJugador;

    private String mensaje;


    public MenuMisiones(
            PantallaJugador pantallaJugador
    ) {

        this.misionServicio =
                new MisionServicio();

        this.pantallaJugador =
                pantallaJugador;
    }


    public void mostrar(
            Partida partida
    ) {

        int opcion;

        do {

            List<ProgresoMision> enCurso;

            try {

                enCurso =
                        misionServicio.misionesEnCurso(
                                partida
                        );

            } catch (SQLException e) {

                mensaje =
                        "Error al cargar las misiones: "
                                + e.getMessage();

                return;
            }

            List<String> opciones =
                    List.of(
                            "  MISIONES",
                            "",
                            "  1. Misiones en curso",
                            "  2. Nuevas misiones",
                            "",
                            "  0. Volver"
                    );

            mensaje =
                    pantallaJugador.mostrar(
                            opciones,
                            partida,
                            mensaje,
                            DibujoMisiones.dibujarEnCurso(
                                    enCurso
                            )
                    );

            opcion =
                    Consola.leerEntero(
                            "Opcion: ",
                            0,
                            2
                    );

            mensaje = null;

            switch (opcion) {

                case 1 ->
                        verEnCurso(
                                partida
                        );

                case 2 ->
                        verDisponibles(
                                partida
                        );
            }

        } while (opcion != 0);
    }


    private void verEnCurso(
            Partida partida
    ) {

        List<ProgresoMision> enCurso;

        try {

            enCurso =
                    misionServicio.misionesEnCurso(
                            partida
                    );

        } catch (SQLException e) {

            mensaje =
                    "Error al cargar las misiones: "
                            + e.getMessage();

            return;
        }

        if (enCurso.isEmpty()) {

            mensaje =
                    "No tenes misiones en curso.";

            return;
        }

        List<String> opciones =
                new ArrayList<>();

        opciones.add(
                "  MISIONES EN CURSO"
        );

        opciones.add("");

        opciones.add(
                "  Elegi una completada"
        );

        opciones.add(
                "  para reclamarla."
        );

        opciones.add("");

        for (int i = 0;
             i < enCurso.size();
             i++) {

            ProgresoMision progreso =
                    enCurso.get(i);

            DefinicionMision definicion =
                    DefinicionMision.valueOf(
                            progreso.getCodigoMision()
                    );

            opciones.add(
                    "  "
                            + (i + 1)
                            + ". "
                            + definicion.getNpc()
            );
        }

        opciones.add("");
        opciones.add("  0. Volver");

        pantallaJugador.mostrar(
                opciones,
                partida,
                mensaje,
                DibujoMisiones.dibujarEnCurso(
                        enCurso
                )
        );

        int eleccion =
                Consola.leerEntero(
                        "Mision: ",
                        0,
                        enCurso.size()
                );

        if (eleccion == 0) {
            return;
        }

        ProgresoMision elegida =
                enCurso.get(
                        eleccion - 1
                );

        try {

            DefinicionMision reclamada =
                    misionServicio.reclamar(
                            partida,
                            elegida.getCodigoMision()
                    );

            mensaje =
                    "Reclamaste la mision de "
                            + reclamada.getNpc()
                            + ": $"
                            + reclamada.getRecompensaMonedas()
                            + " y "
                            + reclamada.getRecompensaExperiencia()
                            + " XP.";

        } catch (AccionNoPermitidaException
                 | SQLException e) {

            mensaje =
                    "No se pudo reclamar: "
                            + e.getMessage();
        }
    }


    private void verDisponibles(
            Partida partida
    ) {

        List<DefinicionMision> disponibles;

        try {

            disponibles =
                    misionServicio.misionesDisponibles(
                            partida
                    );

        } catch (SQLException e) {

            mensaje =
                    "Error al cargar las misiones: "
                            + e.getMessage();

            return;
        }

        if (disponibles.isEmpty()) {

            mensaje =
                    "No hay misiones nuevas.";

            return;
        }

        List<String> opciones =
                List.of(
                        "  NUEVAS MISIONES",
                        "",
                        "  Elegi una mision",
                        "  para aceptarla.",
                        "",
                        "  0. Volver"
                );

        pantallaJugador.mostrar(
                opciones,
                partida,
                mensaje,
                DibujoMisiones.dibujarDisponibles(
                        disponibles
                )
        );

        int eleccion =
                Consola.leerEntero(
                        "Mision: ",
                        0,
                        disponibles.size()
                );

        if (eleccion == 0) {
            return;
        }

        DefinicionMision elegida =
                disponibles.get(
                        eleccion - 1
                );

        try {

            misionServicio.aceptar(
                    partida,
                    elegida
            );

            mensaje =
                    "Aceptaste la mision de "
                            + elegida.getNpc()
                            + ".";

        } catch (AccionNoPermitidaException
                 | SQLException e) {

            mensaje =
                    "No se pudo aceptar: "
                            + e.getMessage();
        }
    }
}