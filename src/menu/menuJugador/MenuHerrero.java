package menu.menuJugador;

import excepciones.AccionNoPermitidaException;
import excepciones.MonedasInsuficientesException;
import excepciones.StockInsuficienteException;
import modelo.Partida;
import modelo.enums.TipoHerramienta;
import servicio.HerreroServicio;
import util.Consola;
import vista.DibujoHerrero;

import java.sql.SQLException;
import java.util.List;

public class MenuHerrero {

    private final HerreroServicio herreroServicio;
    private final PantallaJugador pantallaJugador;

    private String mensaje;


    public MenuHerrero(
            PantallaJugador pantallaJugador
    ) {

        this.herreroServicio =
                new HerreroServicio();

        this.pantallaJugador =
                pantallaJugador;
    }


    public void mostrar(
            Partida partida
    ) {

        int opcion;

        do {

            List<String> opciones =
                    List.of(
                            "  HERRERO",
                            "",
                            "  1. Mejorar hacha",
                            "  2. Mejorar pico",
                            "  3. Mejorar cania",
                            "  4. Comprar parcela",
                            "  5. Construir gallinero",
                            "  6. Construir establo",
                            "  7. Mejorar terreno",
                            "",
                            "  0. Volver"
                    );

            mensaje =
                    pantallaJugador.mostrar(
                            opciones,
                            partida,
                            mensaje,
                            DibujoHerrero.dibujar(
                                    partida
                            )
                    );

            opcion =
                    Consola.leerEntero(
                            "Opcion: ",
                            0,
                            7
                    );

            mensaje = null;

            switch (opcion) {

                case 1 ->
                        mejorarHerramienta(
                                partida,
                                TipoHerramienta.HACHA
                        );

                case 2 ->
                        mejorarHerramienta(
                                partida,
                                TipoHerramienta.PICO
                        );

                case 3 ->
                        mejorarHerramienta(
                                partida,
                                TipoHerramienta.CANIA
                        );

                case 4 ->
                        comprarParcela(
                                partida
                        );

                case 5 ->
                        construirGallinero(
                                partida
                        );

                case 6 ->
                        construirEstablo(
                                partida
                        );

                case 7 ->
                        mejorarTerreno(
                                partida
                        );
            }

        } while (opcion != 0);
    }


    private void mejorarHerramienta(
            Partida partida,
            TipoHerramienta herramienta
    ) {

        try {

            herreroServicio.mejorarHerramienta(
                    partida,
                    herramienta
            );

            mensaje =
                    switch (herramienta) {

                        case HACHA ->
                                "Mejoraste el hacha.";

                        case PICO ->
                                "Mejoraste el pico.";

                        case CANIA ->
                                "Mejoraste la cania.";
                    };

        } catch (AccionNoPermitidaException
                 | MonedasInsuficientesException
                 | StockInsuficienteException
                 | SQLException e) {

            mensaje =
                    "No se pudo mejorar la herramienta: "
                            + e.getMessage();
        }
    }


    private void comprarParcela(
            Partida partida
    ) {

        try {

            herreroServicio.comprarParcela(
                    partida
            );

            mensaje =
                    "Compraste una nueva parcela.";

        } catch (AccionNoPermitidaException
                 | MonedasInsuficientesException
                 | SQLException e) {

            mensaje =
                    "No se pudo comprar la parcela: "
                            + e.getMessage();
        }
    }


    private void construirGallinero(
            Partida partida
    ) {

        try {

            herreroServicio.construirGallinero(
                    partida
            );

            mensaje =
                    "Construiste el gallinero.";

        } catch (AccionNoPermitidaException
                 | MonedasInsuficientesException
                 | StockInsuficienteException
                 | SQLException e) {

            mensaje =
                    "No se pudo construir el gallinero: "
                            + e.getMessage();
        }
    }


    private void construirEstablo(
            Partida partida
    ) {

        try {

            herreroServicio.construirEstablo(
                    partida
            );

            mensaje =
                    "Construiste el establo.";

        } catch (AccionNoPermitidaException
                 | MonedasInsuficientesException
                 | StockInsuficienteException
                 | SQLException e) {

            mensaje =
                    "No se pudo construir el establo: "
                            + e.getMessage();
        }
    }


    private void mejorarTerreno(
            Partida partida
    ) {

        try {

            herreroServicio.mejorarTerreno(
                    partida
            );

            mensaje =
                    "Mejoraste el terreno al nivel "
                            + partida.getNivelTerreno()
                            + ".";

        } catch (AccionNoPermitidaException
                 | MonedasInsuficientesException
                 | StockInsuficienteException
                 | SQLException e) {

            mensaje =
                    "No se pudo mejorar el terreno: "
                            + e.getMessage();
        }
    }
}