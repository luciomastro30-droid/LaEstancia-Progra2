package vista;

import modelo.Item;
import modelo.enums.TipoItem;

import java.util.*;

public class DibujoInventario {

    private static final int ANCHO = 32;
    private static final int ANCHO_INTERIOR = 30;
    private static final int MAX_RENGLONES = 9;

    private static final int ANCHO_COLUMNA_DETALLE = 36;
    private static final int COLUMNAS_DETALLE = 3;
    private static final int ALTO_MINIMO_DETALLE = 7;

    public static List<String> dibujar(
            Map<Item, Integer> contenido
    ) {

        List<String> lineas = new ArrayList<>();

        lineas.add(
                encabezado("INVENTARIO")
        );

        if (contenido == null || contenido.isEmpty()) {

            lineas.add(
                    linea("")
            );

            lineas.add(
                    lineaCentrada("(vacio)")
            );

            lineas.add(
                    linea("")
            );

            lineas.add(
                    borde()
            );

            return lineas;
        }
        //si hay mas items de los que entran, se muestran los primeros y se avisa cuantos faltan
        int mostrados = 0;
        for (Map.Entry<Item, Integer> entrada : contenido.entrySet()) {
            if(contenido.size() > MAX_RENGLONES && mostrados == MAX_RENGLONES - 1){

                lineas.add(linea(" ... y " + (contenido.size() - mostrados) + " mas"));
                break;
            }

            lineas.add(
                    lineaItem(
                            entrada.getKey().getNombre(),
                            entrada.getValue()
                    )
            );
            mostrados++;
        }

        lineas.add(
                borde()
        );

        return lineas;
    }

    // DETALLE COMPLETO (opcion Inventario): agrupado por tipo, en columnas
    public static List<String> dibujarDetalle(Map<Item, Integer> contenido){

        if(contenido == null || contenido.isEmpty()){
            List<String> vacio = new ArrayList<>();
            vacio.add("");
            vacio.add("   No tenes nada en el inventario todavia.");
            return vacio;
        }

        //En este map la clave es un enum, recorre los tipos en el orden
        // que estan declarados

        Map<TipoItem, List<String>> porTipo = new EnumMap<>(TipoItem.class);
        for(Map.Entry<Item, Integer> entrada : contenido.entrySet()){
            Item item = entrada.getKey();
            if(!porTipo.containsKey(item.getTipo())){
                porTipo.put(item.getTipo(), new ArrayList<>());
            }
            porTipo.get(item.getTipo()).add(renglonDetalle(item,entrada.getValue()));
        }

        // cada grupo es el titulo del tipo y debajo sus items
        List<List<String>> grupos = new ArrayList<>();
        for(Map.Entry<TipoItem,List<String>> entrada : porTipo.entrySet()){
            List<String> grupo = new ArrayList<>();
            grupo.add(" [" + nombreTipo(entrada.getKey()) + "]");
            grupo.addAll(entrada.getValue());
            grupos.add(grupo);
        }

        // busco el alto mas chico en el que los grupos entran en 3 columnas sin partirse
        int alto = ALTO_MINIMO_DETALLE;
        List<List<String>> columnas = repartir(grupos,alto);
        while (columnas.size() > COLUMNAS_DETALLE){
            alto++;
            columnas = repartir(grupos,alto);
        }

        // pego las columnas una al lado de la otra
        List<String> escena = new ArrayList<>();
        for (int fila = 0; fila<alto; fila++){
            StringBuilder renglon = new StringBuilder();
            for(List<String> columna : columnas){
                String texto = (fila < columna.size()) ? columna.get(fila) : "";
                renglon.append(String.format("%-" + ANCHO_COLUMNA_DETALLE + "S", texto));
            }
            escena.add(renglon.toString());
        }
        return escena;
    }

    // pone los grupos en columnas de "alto" renglones; si un grupo no entra en la columna actual, empieza otra
    private static List<List<String>> repartir(List<List<String>> grupos, int alto){
        List<List<String>> columnas = new ArrayList<>();
        List<String> actual = new ArrayList<>();
        for(List<String> grupo : grupos){
            if(!actual.isEmpty() && actual.size() + grupo.size() > alto){
                columnas.add(actual);
                actual = new ArrayList<>();
            }
            actual.addAll(grupo);
        }
        columnas.add(actual);
        return columnas;
    }

    private static String renglonDetalle(Item item, int cantidad){
        String nombre = item.getNombre().length() > 18 ? item.getNombre().substring(0,18) :
                item.getNombre();
        return String.format("   %-18s x%-4d $%d", nombre, cantidad, item.getPrecioVenta());
    }

    private static String nombreTipo(TipoItem tipo){

        return switch (tipo){

            case SEMILLA -> "SEMILLAS";
            case COSECHA -> "COSECHAS";
            case PRODUCTO_ANIMAL -> "PRODUCTOS ANIMALES";
            case MATERIAL -> "MATERIALES";
            case PESCADO -> "PESCADOS";
            case ALIMENTO -> "ALIMENTOS";
            case OTRO -> "OTROS";
        };
    }

    private static String lineaItem(
            String nombre,
            int cantidad
    ) {

        String cantidadTexto =
                "x" + cantidad;

        /*
         * Dejamos un espacio a cada lado.
         * El nombre se corta solo si no entra en la columna.
         */
        int anchoNombre =
                ANCHO_INTERIOR
                        - cantidadTexto.length()
                        - 2;

        String nombreVisible =
                nombre.length() > anchoNombre
                        ? nombre.substring(0, anchoNombre)
                        : nombre;

        int espacios =
                ANCHO_INTERIOR
                        - nombreVisible.length()
                        - cantidadTexto.length();

        String contenido =
                nombreVisible
                        + " ".repeat(
                        Math.max(1, espacios)
                )
                        + cantidadTexto;

        return linea(contenido);
    }


    private static String encabezado(
            String titulo
    ) {

        String inicio =
                " " + titulo + " ";

        int guiones =
                ANCHO_INTERIOR
                        - inicio.length();

        return "+"
                + inicio
                + "-".repeat(
                Math.max(0, guiones)
        )
                + "+";
    }


    private static String borde() {

        return "+"
                + "-".repeat(ANCHO_INTERIOR)
                + "+";
    }


    private static String linea(
            String texto
    ) {

        return "|"
                + ajustar(texto)
                + "|";
    }


    private static String lineaCentrada(
            String texto
    ) {

        if (texto.length() >= ANCHO_INTERIOR) {
            return linea(texto);
        }

        int espacios =
                ANCHO_INTERIOR
                        - texto.length();

        int izquierda =
                espacios / 2;

        return linea(
                " ".repeat(izquierda)
                        + texto
        );
    }


    private static String ajustar(
            String texto
    ) {

        if (texto.length() > ANCHO_INTERIOR) {
            texto =
                    texto.substring(
                            0,
                            ANCHO_INTERIOR
                    );
        }

        return String.format(
                "%-" + ANCHO_INTERIOR + "s",
                texto
        );
    }
}