package vista;

import java.util.List;

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