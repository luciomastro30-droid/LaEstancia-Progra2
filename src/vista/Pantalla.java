package vista;

import java.util.List;

public class Pantalla {

    private static final int ANCHO_OPCIONES = 26;
    private static final int ANCHO_INFO = 51;
    private static final int ANCHO_ESCENA = 62;
    private static final int ANCHO_LATERAL = 15;

    private static String ajustar(String texto, int ancho){
        if(texto == null){
            texto ="";
        }
        if(texto.length() > ancho){
            return texto.substring(0,ancho);
        }
        return String.format("%-" + ancho + "s", texto);
    }

    private static String obtener(List<String> lista, int indice){
        if(lista.size() > indice) return lista.get(indice);
        return "";
    }

    private static String separador(int ancho1, int ancho2){
        return  "+" + "-".repeat(ancho1) + "+" +  "-".repeat(ancho2) + "+";
    }

    public static String armar(List<String> opciones, List<String> info,
                               List<String> escena, List<String> lateral){

        StringBuilder sb = new StringBuilder();
        sb.append(separador(ANCHO_OPCIONES,ANCHO_INFO)).append("\n");
        int altoArriba = Math.max(opciones.size(), info.size());
        for(int i = 0; i < altoArriba; i++){
            sb.append("|")
                    .append(ajustar(obtener(opciones, i),ANCHO_OPCIONES))
                    .append("|")
                    .append(ajustar(obtener(info, i), ANCHO_INFO))
                    .append("|\n");
        }


        sb.append(separador(ANCHO_ESCENA,ANCHO_LATERAL)).append("\n");
        int altoAbajo = Math.max(escena.size(), lateral.size());
        for(int i = 0; i < altoAbajo; i++){
            sb.append("|")
                    .append(ajustar(obtener(escena, i),ANCHO_ESCENA))
                    .append("|")
                    .append(ajustar(obtener(lateral, i), ANCHO_LATERAL))
                    .append("|\n");
        }

        sb.append("+").append("-".repeat(78)).append("+\n");

        return sb.toString();

    }
}



