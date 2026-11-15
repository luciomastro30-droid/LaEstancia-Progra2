package vista;

import modelo.eventos.Chupacabras;
import modelo.eventos.Evento;
import modelo.eventos.Pomberito;
import modelo.eventos.Tormenta;

import java.util.List;

/**
 * Genera los dibujos ASCII utilizados para representar
 * visualmente los distintos eventos del juego.
 */
public class DibujoEvento {

    /*
     * Evento es la clase padre. Con instanceof identifico
     * que evento concreto recibimos y devuelvo su dibujo.
     */
    public static List<String> dibujar(Evento evento) {

        if (evento instanceof Pomberito) {
            return dibujarPomberito();
        }

        if (evento instanceof Chupacabras) {
            return dibujarChupacabras();
        }

        if (evento instanceof Tormenta) {
            return dibujarTormenta();
        }

        // Sirve como respaldo si en el futuro aparece otro evento.
        return dibujarGenerico();
    }


    private static List<String> dibujarPomberito() {

        return List.of(
                "      /\\                _____                 .-.                                   ",
                "     /  \\             _|_____|_              (   )                                  ",
                "    /    \\              (o o)                 `-'           +--------------------+  ",
                "   /      \\            /| ~ |\\                              |     POMBERITO      |  ",
                "  /________\\            /   \\                               +--------------------+  ",
                "      ||               _|   |_                                                      ",
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                              "
        );
    }


    private static List<String> dibujarChupacabras() {

        return List.of(
                "      .-\"\"\"-.                                                                       ",
                "     / *   * \\                               !                                      ",
                "    |    ^    |                              _              +--------------------+  ",
                "    |  \\VVV/  |       |---|---|---|---|     (o>             |    CHUPACABRAS     |  ",
                "     \\_______/        |   |   |   |   |    /(_)             +--------------------+  ",
                "      /|   |\\         |---|---|---|---|     ^ ^                                     ",
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                              "
        );
    }


    private static List<String> dibujarTormenta() {

        return List.of(
                "      .--.                    .--.                                                  ",
                "   .-(    ).               .-(    ).                                                ",
                "  (___.__)__)      /      (___.__)__)         /\\            +--------------------+  ",
                "   ' ' ' ' '      /_       ' ' ' ' '         /  \\           |      TORMENTA      |  ",
                "  ' ' ' ' '        /      ' ' ' ' '         /____\\          +--------------------+  ",
                "   ' ' ' '        /        ' ' ' '          |_[]_|                                  ",
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                              "
        );
    }


    private static List<String> dibujarGenerico() {
        return List.of("", "", "", "                                   ?", "", "", "");
    }
}