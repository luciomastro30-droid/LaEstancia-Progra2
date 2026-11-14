package vista;

import java.util.List;

public class Pantalla {

    private static final int ANCHO_OPCIONES = 26;
    private static final int ANCHO_INFO = 50;
    private static final int ANCHO_INVENTARIO = 32;
    private static final int ANCHO_TOTAL = ANCHO_OPCIONES + ANCHO_INFO + ANCHO_INVENTARIO + 2;

    public static String armar(List<String> opciones, List<String> info,
                               List<String> inventario, List<String> escena) {

        StringBuilder sb = new StringBuilder();

        // parte de arriba: tres columnas
        sb.append(separador(ANCHO_OPCIONES, ANCHO_INFO, ANCHO_INVENTARIO)).append("\n");

        int altoArriba = Math.max(opciones.size(), Math.max(info.size(), inventario.size()));
        for (int i = 0; i < altoArriba; i++) {
            sb.append("|")
                    .append(ajustar(obtener(opciones, i), ANCHO_OPCIONES))
                    .append("|")
                    .append(ajustar(obtener(info, i), ANCHO_INFO))
                    .append("|")
                    .append(ajustar(obtener(inventario, i), ANCHO_INVENTARIO))
                    .append("|\n");
        }

        // parte de abajo: la escena ocupa todo el ancho
        sb.append(separador(ANCHO_TOTAL)).append("\n");

        for (String renglon : escena) {
            sb.append("|").append(ajustar(renglon, ANCHO_TOTAL)).append("|\n");
        }

        sb.append(separador(ANCHO_TOTAL)).append("\n");

        return sb.toString();
    }

    private static String ajustar(String texto, int ancho) {
        if (texto == null) {
            texto = "";
        }
        if (texto.length() > ancho) {
            return texto.substring(0, ancho);
        }
        return String.format("%-" + ancho + "s", texto);
    }

    private static String obtener(List<String> lista, int indice) {
        if (lista.size() > indice) return lista.get(indice);
        return "";
    }

    // recibe uno o varios anchos y arma una linea como +-----+-----+
    private static String separador(int... anchos) {
        StringBuilder linea = new StringBuilder("+");
        for (int ancho : anchos) {
            linea.append("-".repeat(ancho)).append("+");
        }
        return linea.toString();
    }
}