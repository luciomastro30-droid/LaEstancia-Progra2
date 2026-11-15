package menu.menuJugador;

import modelo.Partida;
import util.Consola;
import vista.DibujoPueblo;

import java.util.List;

public class MenuPueblo {

    private final PantallaJugador pantallaJugador;

    private String mensaje;


    public MenuPueblo(
            PantallaJugador pantallaJugador
    ) {

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
                        mensaje =
                                "Tienda agricola: proximamente";

                case 2 ->
                        mensaje =
                                "Herrero: proximamente";

                case 3 ->
                        mensaje =
                                "Mercado: proximamente";
            }

        } while (opcion != 0);
    }
}