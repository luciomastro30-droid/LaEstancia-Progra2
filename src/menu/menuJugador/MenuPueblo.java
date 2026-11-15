package menu.menuJugador;

import modelo.Partida;
import util.Consola;
import vista.DibujoPueblo;

import java.util.List;

public class MenuPueblo {

    private final PantallaJugador pantallaJugador;
    private final MenuTienda menuTienda;
    private final MenuHerrero menuHerrero;
    private final MenuMercado menuMercado;

    private String mensaje;


    public MenuPueblo(
            PantallaJugador pantallaJugador
    ) {

        this.pantallaJugador =
                pantallaJugador;

        this.menuTienda =
                new MenuTienda(
                        pantallaJugador
                );

        this.menuHerrero =
                new MenuHerrero(
                        pantallaJugador
                );

        this.menuMercado =
                new MenuMercado(
                        pantallaJugador
                );
    }


    public void mostrar(
            Partida partida
    ) {

        int opcion;

        do {

            List<String> opciones =
                    List.of(
                            "  PUEBLO",
                            "",
                            "  1. Tienda agricola",
                            "  2. Herrero",
                            "  3. Mercado",
                            "",
                            "  0. Volver"
                    );

            mensaje =
                    pantallaJugador.mostrar(
                            opciones,
                            partida,
                            mensaje,
                            DibujoPueblo.dibujar()
                    );

            opcion =
                    Consola.leerEntero(
                            "Opcion: ",
                            0,
                            3
                    );

            mensaje = null;

            switch (opcion) {

                case 1 ->
                        menuTienda.mostrar(
                                partida
                        );

                case 2 ->
                        menuHerrero.mostrar(
                                partida
                        );

                case 3 ->
                        menuMercado.mostrar(
                                partida
                        );
            }

        } while (opcion != 0);
    }
}