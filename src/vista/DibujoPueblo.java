package vista;

import java.util.List;

public class DibujoPueblo {

    /*
     * Devuelve la escena del pueblo completa.
     * Cada String representa un renglon de la parte inferior de Pantalla.
     */
    public static List<String> dibujar() {

        return List.of(
                "       __________________          __________________          __________________",
                "      / TIENDA AGRICOLA \\        /     HERRERO      \\        /     MERCADO      \\",
                "     /____________________\\      /____________________\\      /____________________\\",
                "     |       [1]          |      |       [2]          |      |       [3]          |",
                "     |  |__|  .----.  |__||      |  |__|  .----.  |__||      |  |__|  .----.  |__||",
                "     |_______|      |_____|      |_______|      |_____|      |_______|      |_____|",
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        );
    }
}