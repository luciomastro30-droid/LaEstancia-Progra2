package vista;

import java.util.List;

/**
 * Genera la representacion visual ASCII del pueblo.
 * Muestra los principales lugares que el jugador puede visitar,
 * como la tienda agricola, el herrero y el mercado.
 */
public class DibujoPueblo {

    /*
     * Devuelve la escena del pueblo completa.
     * Cada String representa un renglon de la parte inferior de Pantalla.
     */
    public static List<String> dibujar() {

        return List.of(
                "       __________________                ______________||__                __________________       ",
                "      / TIENDA AGRICOLA  \\              /     HERRERO      \\              /     MERCADO      \\      ",
                "     /____________________\\            /____________________\\            /\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\     ",
                "     |  __    [1]    __   |            |  __    [2]    __   |            |  __    [3]    __   |     ",
                "     | |__|  .----. |__|  |            | |__|  .----. |__|  |            | |__|  .----. |__|  |     ",
                "     |_______|    |_______|            |_______|    |_______|            |_______|    |_______|     ",
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        );
    }
}