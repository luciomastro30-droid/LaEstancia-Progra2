package vista;

import java.util.ArrayList;
import java.util.List;

/**
 * Dibujos de los comerciantes (tienda, mercado y herrero).
 * Van a la derecha de la lista de productos, para
 * aprovechar el espacio libre de la pantalla.
 */
public class DibujoComerciante {

    // espacio entre la lista y el dibujo
    private static final String SEPARACION = "   ";

    /*
     * Pone el dibujo a la derecha de la lista, renglon por renglon.
     * Si uno es mas alto que el otro, se completa con espacios.
     */
    public static List<String> alLado(List<String> lista,
                                      List<String> dibujo) {

        int ancho = 0;
        for (String renglon : lista) {
            ancho = Math.max(ancho, renglon.length());
        }

        int alto = Math.max(lista.size(), dibujo.size());
        List<String> resultado = new ArrayList<>();

        for (int i = 0; i < alto; i++) {
            String izquierda = (i < lista.size()) ? lista.get(i) : "";
            String derecha = (i < dibujo.size()) ? dibujo.get(i) : "";
            resultado.add(String.format("%-" + ancho + "s", izquierda)
                    + SEPARACION + derecha);
        }
        return resultado;
    }

    public static List<String> tienda() {
        return List.of(
                " ____________________________________________________ ",
                "|   _____    _____    _____    _____    _____        |",
                "|  |TRIGO|  |MAIZ |  |PAPA |  |TOMAT|  |AJO  |       |",
                "|  |_____|  |_____|  |_____|  |_____|  |_____|       |",
                "|====================================================|",
                "|                      ___________                   |",
                "|  .----.  .----.    _|___________|_                 |",
                "|  |HENO|  |ALIM|      ( o     o )                   |",
                "|  |____|  |____|      |    >    |     \"Buen dia!\"   |",
                "|                       \\  \\_/  /                    |",
                "|                    ____|_____|____                 |",
                "|                   /   |  |=|  |   \\                |",
                "|====================================================|",
                "|  TIENDA AGRICOLA     [ MOSTRADOR ]     $   $   $   |",
                "|____________________________________________________|"
        );
    }

    public static List<String> mercado() {
        return List.of(
                " ____________________________________________________ ",
                "|" + "/\\".repeat(26) + "|",
                "|                                                    |",
                "| \"Te compro todo!\" .-------.                        |",
                "|                  /  o   o  \\           __|__       |",
                "|                  |    ^    |          /  |  \\      |",
                "|                  |  \\~~~/  |        _/_  |  _\\_    |",
                "|                   \\_______/        (___) | (___)   |",
                "|                ____|_____|____        ___|___      |",
                "|               /   |  $$$  |   \\                    |",
                "|====================================================|",
                "|  [HUEVOS]  [LECHE]  [LANA]  [PESCADO]   MERCADO    |",
                "|____________________________________________________|"
        );
    }

    public static List<String> herrero() {
        return List.of(
                " ______________________________________________ ",
                "|  [HACHAS]  [PICOS]  [CANIAS]      ( )        |",
                "|=================================  ( )  ======|",
                "|    .-------.                      ) (        |",
                "|   /  o   o  \\    \"Mejoramos   ____| |____    |",
                "|   |    <    |     todo!\"      |  )   (  |    |",
                "|   \\  WWWWW  /  *  .           | ( ) ( ) |    |",
                "|    \\_______/  [###] *         |_^^^^^^^_|    |",
                "| ____|_____|____=====          |  FRAGUA |    |",
                "| /   |#####|   \\  __________   |_________|    |",
                "|/____|#####|____\\ \\________/                  |",
                "|==================  |    |     ============== |",
                "|  H E R R E R O    /______\\                   |",
                "|______________________________________________|"
        );
    }
}