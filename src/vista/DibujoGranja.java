
package vista;

import modelo.Cultivo;
import modelo.Partida;
import modelo.Plantacion;
import util.Reloj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DibujoGranja {

    private static final int ALTO_ESCENA = 7;
    private static final int ANCHO_MENSAJE = 46;

    private static final String TIERRA = "~~~~~~~~~~~";
    private static final String VACIO = "           ";

    // ---------- ESCENA DE ABAJO: parcelas + corrales ----------

    public static List<String> dibujarEscena(Partida partida,
                                             Map<Integer, Plantacion> plantaciones,
                                             Map<Integer, Cultivo> cultivos) {

        List<String> parcelas = dibujarParcelas(partida, plantaciones, cultivos);
        List<String> corrales = dibujarCorrales(partida);

        List<String> escena = new ArrayList<>();
        for (int r = 0; r < ALTO_ESCENA; r++) {
            escena.add(parcelas.get(r) + "| " + corrales.get(r));
        }
        return escena;
    }

    private static List<String> dibujarParcelas(Partida partida,
                                                Map<Integer, Plantacion> plantaciones,
                                                Map<Integer, Cultivo> cultivos) {

        StringBuilder[] renglones = new StringBuilder[ALTO_ESCENA];
        for (int r = 0; r < ALTO_ESCENA; r++) {
            renglones[r] = new StringBuilder("  ");
        }

        for (int numero = 1; numero <= Partida.MAX_PARCELAS; numero++) {
            String[] parcela = dibujarParcela(numero, partida, plantaciones, cultivos);
            for (int r = 0; r < ALTO_ESCENA; r++) {
                renglones[r].append(parcela[r]).append(" ");
            }
        }

        List<String> resultado = new ArrayList<>();
        for (StringBuilder renglon : renglones) {
            resultado.add(renglon.toString());
        }
        return resultado;
    }

    private static String[] dibujarParcela(int numero, Partida partida,
                                           Map<Integer, Plantacion> plantaciones,
                                           Map<Integer, Cultivo> cultivos) {

        // BLOQUEADA
        if (numero > partida.getParcelasDesbloqueadas()) {
            return caja(numero, "///////////", "///////////", "/BLOQUEADA/", "///////////", "///////////");
        }

        // VACIA
        Plantacion plantacion = plantaciones.get(numero);
        if (plantacion == null) {
            return caja(numero, VACIO, VACIO, centrar("vacia", 11), VACIO, TIERRA);
        }

        Cultivo cultivo = cultivos.get(plantacion.getIdCultivo());
        long restante = plantacion.minutosRestantes(cultivo, partida.getMinutoJuegoTotal());
        String nombre = centrar(cultivo.getNombre().toUpperCase(), 11);

        // LISTA
        if (restante == 0) {
            return caja(numero, "  \\|/ \\|/  ", "   |   |   ", nombre, centrar("LISTA!", 11), TIERRA);
        }

        // CRECIENDO
        return caja(numero, VACIO, "  .  .  .  ", nombre, centrar(formatearTiempo(restante), 11), TIERRA);
    }

    // arma la cajita de 13 x 7 alrededor de los 5 renglones de adentro (de 11 caracteres)
    private static String[] caja(int numero, String r1, String r2, String r3, String r4, String r5) {
        return new String[]{
                ".-----------.",
                "|" + r1 + "|",
                "|" + r2 + "|",
                "|" + r3 + "|",
                "|" + r4 + "|",
                "|" + r5 + "|",
                "'-----" + numero + "-----'"
        };
    }

    // 45 -> "falta 45m"   150 -> "falta 2h30m"   600 -> "falta 10h"
    private static String formatearTiempo(long minutos) {
        long horas = minutos / 60;
        long resto = minutos % 60;

        String tiempo;
        if (horas == 0) {
            tiempo = resto + "m";
        } else if (resto == 0) {
            tiempo = horas + "h";
        } else {
            tiempo = horas + "h" + resto + "m";
        }

        String conFalta = "falta " + tiempo;
        if (conFalta.length() <= 11) {
            return conFalta;
        }
        return tiempo;
    }

    // por ahora solo muestra si estan construidos; los animales se agregan en esa etapa
    private static List<String> dibujarCorrales(Partida partida) {
        List<String> corrales = new ArrayList<>();
        corrales.add("GALLINERO");
        corrales.add(partida.isTieneGallinero() ? "  (sin animales)" : "  [sin construir]");
        corrales.add("");
        corrales.add("ESTABLO");
        corrales.add(partida.isTieneEstablo() ? "  (sin animales)" : "  [sin construir]");
        corrales.add("");
        corrales.add("");
        return corrales;
    }

    // ---------- COLUMNA DEL MEDIO: info del jugador ----------

    public static List<String> dibujarInfo(Partida partida, String mensaje) {

        Reloj reloj = new Reloj(partida.getMinutoJuegoTotal());
        String momento = reloj.esDeNoche() ? "(noche)" : "(dia)";
        int necesaria = partida.experienciaParaSubir();

        List<String> info = new ArrayList<>();

        info.add(String.format("  %-24s%s %s",
                partida.getNombreGranja().toUpperCase(), reloj.getTexto(), momento));
        info.add("");
        info.add(String.format("  Monedas: %-10d Nivel: %d",
                partida.getMonedas(), partida.getNivel()));
        info.add(String.format("  XP %s %d/%d",
                barraXp(partida.getExperiencia(), necesaria), partida.getExperiencia(), necesaria));
        info.add(String.format("  Hacha: %-8s Pico: %-8s Cania: %s",
                partida.getRarezaHacha(), partida.getRarezaPico(), partida.getRarezaCania()));
        info.add("");

        if (mensaje != null) {
            // si el mensaje no entra en un renglon, se parte en varios
            List<String> renglones = partirEnRenglones(mensaje, ANCHO_MENSAJE);
            for (int i = 0; i < renglones.size(); i++) {
                String prefijo = (i == 0) ? "  > " : "    ";
                info.add(prefijo + renglones.get(i));
            }
        }

        return info;
    }

    // arma renglones de como mucho "ancho" caracteres sin cortar palabras
    //va agregando palabra por palabra.
    //si la próxima no entra, cierra el renglón y empieza otro
    private static List<String> partirEnRenglones(String texto, int ancho) {
        List<String> renglones = new ArrayList<>();
        StringBuilder actual = new StringBuilder();

        for (String palabra : texto.split(" ")) {
            if (actual.length() > 0 && actual.length() + 1 + palabra.length() > ancho) {
                renglones.add(actual.toString());
                actual = new StringBuilder();
            }
            if (actual.length() > 0) {
                actual.append(" ");
            }
            actual.append(palabra);
        }
        renglones.add(actual.toString());
        return renglones;
    }

    private static String barraXp(int experiencia, int necesaria) {
        int llenos = experiencia * 10 / necesaria;
        if (llenos > 10) {
            llenos = 10;
        }
        return "[" + "#".repeat(llenos) + "-".repeat(10 - llenos) + "]";
    }

    private static String centrar(String texto, int ancho) {
        if (texto.length() >= ancho) {
            return texto.substring(0, ancho);
        }
        int izquierda = (ancho - texto.length()) / 2;
        int derecha = ancho - texto.length() - izquierda;
        return " ".repeat(izquierda) + texto + " ".repeat(derecha);
    }
}