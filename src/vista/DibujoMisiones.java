package vista;

import modelo.ProgresoMision;
import modelo.enums.DefinicionMision;
import modelo.enums.EstadoMision;

import java.util.ArrayList;
import java.util.List;

public class DibujoMisiones {

    private static final int ANCHO_CARTEL = 34;
    private static final int ANCHO_TEXTO = 30;
    private static final int ALTO_CARTEL = 7;

    private static final int MAX_POR_FILA = 3;
    private static final int SEPARACION = 2;


    public static List<String> dibujarEnCurso(
            List<ProgresoMision> enCurso
    ) {

        if (enCurso == null || enCurso.isEmpty()) {
            return escenaVacia(
                    "No tenes misiones en curso."
            );
        }

        List<List<String>> carteles =
                new ArrayList<>();

        for (ProgresoMision progreso : enCurso) {

            DefinicionMision mision =
                    DefinicionMision.valueOf(
                            progreso.getCodigoMision()
                    );

            carteles.add(
                    cartelEnCurso(
                            progreso,
                            mision
                    )
            );
        }

        return acomodarCarteles(
                carteles
        );
    }


    public static List<String> dibujarDisponibles(
            List<DefinicionMision> disponibles
    ) {

        if (disponibles == null
                || disponibles.isEmpty()) {

            return escenaVacia(
                    "No hay misiones nuevas."
            );
        }

        List<List<String>> carteles =
                new ArrayList<>();

        int opcion = 1;

        for (DefinicionMision mision :
                disponibles) {

            carteles.add(
                    cartelDisponible(
                            mision,
                            opcion
                    )
            );

            opcion++;
        }

        return acomodarCarteles(
                carteles
        );
    }


    private static List<String> cartelEnCurso(
            ProgresoMision progreso,
            DefinicionMision mision
    ) {

        List<String> descripcion =
                partirDescripcion(
                        mision.getDescripcion()
                );

        List<String> cartel =
                new ArrayList<>();

        cartel.add(borde());

        cartel.add(
                linea(
                        mision.getNpc().toUpperCase()
                )
        );

        cartel.add(
                linea(
                        descripcion.get(0)
                )
        );

        cartel.add(
                linea(
                        descripcion.get(1)
                )
        );

        if (progreso.getEstado()
                == EstadoMision.COMPLETADA) {

            cartel.add(
                    linea(
                            "COMPLETADA! Reclamala"
                    )
            );

        } else {

            cartel.add(
                    linea(
                            "Progreso: ["
                                    + barraProgreso(
                                    progreso.getProgreso(),
                                    mision.getObjetivo()
                            )
                                    + "] "
                                    + progreso.getProgreso()
                                    + "/"
                                    + mision.getObjetivo()
                    )
            );
        }

        cartel.add(
                linea(
                        "Premio: $"
                                + mision.getRecompensaMonedas()
                                + " y "
                                + mision.getRecompensaExperiencia()
                                + " XP"
                )
        );

        cartel.add(borde());

        return cartel;
    }


    private static List<String> cartelDisponible(
            DefinicionMision mision,
            int opcion
    ) {

        List<String> descripcion =
                partirDescripcion(
                        mision.getDescripcion()
                );

        List<String> cartel =
                new ArrayList<>();

        cartel.add(borde());

        cartel.add(
                linea(
                        opcion
                                + ". "
                                + mision.getNpc().toUpperCase()
                )
        );

        cartel.add(
                linea(
                        descripcion.get(0)
                )
        );

        cartel.add(
                linea(
                        descripcion.get(1)
                )
        );

        /*
         * Las disponibles no tienen progreso todavia,
         * por eso dejamos este renglon libre.
         */
        cartel.add(
                linea("")
        );

        cartel.add(
                linea(
                        "Objetivo: "
                                + mision.getObjetivo()
                )
        );

        cartel.add(borde());

        return cartel;
    }


    private static String barraProgreso(
            int progreso,
            int objetivo
    ) {

        int llenos =
                progreso * 10 / objetivo;

        llenos =
                Math.max(
                        0,
                        Math.min(
                                10,
                                llenos
                        )
                );

        return "#".repeat(llenos)
                + "-".repeat(10 - llenos);
    }


    private static List<String> partirDescripcion(
            String descripcion
    ) {

        List<String> lineas =
                new ArrayList<>();

        StringBuilder actual =
                new StringBuilder();

        for (String palabra :
                descripcion.split(" ")) {

            /*
             * Agrego palabras completas mientras entren.
             * Nunca se corta una palabra por la mitad.
             */
            int largoNuevo =
                    actual.isEmpty()
                            ? palabra.length()
                            : actual.length()
                            + 1
                            + palabra.length();

            if (largoNuevo <= ANCHO_TEXTO) {

                if (!actual.isEmpty()) {
                    actual.append(" ");
                }

                actual.append(palabra);

            } else {

                lineas.add(
                        actual.toString()
                );

                actual =
                        new StringBuilder(
                                palabra
                        );
            }
        }

        if (!actual.isEmpty()) {
            lineas.add(
                    actual.toString()
            );
        }

        /*
         * Los carteles tienen exactamente
         * dos renglones de descripcion.
         */
        while (lineas.size() < 2) {
            lineas.add("");
        }

        return List.of(
                lineas.get(0),
                lineas.get(1)
        );
    }


    private static List<String> acomodarCarteles(
            List<List<String>> carteles
    ) {

        List<String> escena =
                new ArrayList<>();

        /*
         * El ancho final se calcula con la fila
         * que tenga mas carteles.
         */
        int cantidadMaximaFila =
                Math.min(
                        MAX_POR_FILA,
                        carteles.size()
                );

        int anchoEscena =
                cantidadMaximaFila
                        * ANCHO_CARTEL
                        + (cantidadMaximaFila - 1)
                        * SEPARACION;

        for (int inicio = 0;
             inicio < carteles.size();
             inicio += MAX_POR_FILA) {

            int fin =
                    Math.min(
                            inicio + MAX_POR_FILA,
                            carteles.size()
                    );

            for (int renglon = 0;
                 renglon < ALTO_CARTEL;
                 renglon++) {

                StringBuilder linea =
                        new StringBuilder();

                for (int i = inicio;
                     i < fin;
                     i++) {

                    if (i > inicio) {
                        linea.append(" ".repeat(SEPARACION));
                    }

                    linea.append(
                            carteles
                                    .get(i)
                                    .get(renglon)
                    );
                }

                escena.add(
                        completarHasta(
                                linea.toString(),
                                anchoEscena
                        )
                );
            }
        }

        return escena;
    }


    private static List<String> escenaVacia(
            String mensaje
    ) {

        List<String> lineas =
                new ArrayList<>();

        lineas.add(borde());
        lineas.add(linea(""));
        lineas.add(linea(""));

        lineas.add(
                lineaCentrada(
                        mensaje
                )
        );

        lineas.add(linea(""));
        lineas.add(linea(""));
        lineas.add(borde());

        return lineas;
    }


    private static String borde() {

        return "+"
                + "-".repeat(32)
                + "+";
    }


    private static String linea(
            String texto
    ) {

        String contenido =
                completarHasta(
                        texto,
                        ANCHO_TEXTO
                );

        if (contenido.length() > ANCHO_TEXTO) {
            contenido =
                    contenido.substring(
                            0,
                            ANCHO_TEXTO
                    );
        }

        return "| "
                + contenido
                + " |";
    }


    private static String lineaCentrada(
            String texto
    ) {

        if (texto.length() >= ANCHO_TEXTO) {
            return linea(texto);
        }

        int espacios =
                ANCHO_TEXTO
                        - texto.length();

        int izquierda =
                espacios / 2;

        return linea(
                " ".repeat(izquierda)
                        + texto
        );
    }


    private static String completarHasta(
            String texto,
            int ancho
    ) {

        if (texto.length() >= ancho) {
            return texto;
        }

        return texto
                + " ".repeat(
                ancho - texto.length()
        );
    }
}