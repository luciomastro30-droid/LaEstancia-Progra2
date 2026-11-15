package menu;

import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import excepciones.StockInsuficienteException;
import modelo.Cultivo;
import modelo.Item;
import modelo.Jugador;
import modelo.Partida;
import modelo.Plantacion;
import servicio.GranjaServicio;
import servicio.InventarioServicio;
import servicio.RegistroServicio;
import servicio.ResultadoCosecha;
import util.Consola;
import util.Reloj;
import vista.DibujoGranja;
import vista.DibujoInventario;
import vista.Pantalla;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuJugador {

    private static final List<String> OPCIONES_GRANJA = List.of(
            "  MENU DE LA GRANJA",
            "",
            "  1. Plantar",
            "  2. Cosechar",
            "  3. Inventario",
            "  4. Animales",
            "  5. Ir al pueblo",
            "  6. Recolectar",
            "  7. Dormir",
            "  0. Guardar y salir"
    );

    private final Jugador jugador;
    private final RegistroServicio registroServicio;
    private final GranjaServicio granjaServicio;
    private final InventarioServicio inventarioServicio;

    // Resultado de la ultima accion; se muestra en la pantalla con "> ".
    private String mensaje;


    public MenuJugador(Jugador jugador) {
        this.jugador = jugador;
        this.registroServicio = new RegistroServicio();
        this.granjaServicio = new GranjaServicio();
        this.inventarioServicio = new InventarioServicio();
    }


    public void mostrar() {

        if (!jugador.tienePartidaActual()) {

            System.out.println(
                    "No tienes ninguna partida activa. Vamos a crear una nueva"
            );

            String nombreGranja =
                    Consola.leerTexto(
                            "Escriba el nombre de la granja: "
                    );

            try {

                Partida partida =
                        registroServicio.crearPartidaNueva(
                                jugador.getIdUsuario(),
                                nombreGranja
                        );

                jugador.setPartidaActual(partida);

            } catch (AccionNoPermitidaException | SQLException e) {

                System.out.println(
                        "No se pudo crear la partida: "
                                + e.getMessage()
                );

                return;
            }
        }

        int opcion;

        do {

            Partida partida =
                    jugador.getPartidaActual();

            dibujarGranja(
                    OPCIONES_GRANJA,
                    partida
            );

            opcion =
                    Consola.leerEntero(
                            "Opcion: ",
                            0,
                            7
                    );

            mensaje = null;

            switch (opcion) {

                case 1 ->
                        plantar(partida);

                case 2 ->
                        cosechar(partida);

                case 3 ->
                        verInventario(partida);

                case 4 ->
                        mensaje = "Animales: proximamente";

                case 5 ->
                        mensaje = "Pueblo: proximamente";

                case 6 ->
                        mensaje = "Recolectar: proximamente";

                case 7 ->
                        dormir(partida);

                case 0 ->
                        guardar(partida);
            }

        } while (opcion != 0);
    }


    /*
     * Dibuja la pantalla principal de la granja.
     * Ahora tambien carga el inventario real de la partida.
     */
    private void dibujarGranja(
            List<String> opciones,
            Partida partida
    ) {

        Map<Integer, Plantacion> plantaciones =
                new HashMap<>();

        Map<Integer, Cultivo> cultivos =
                new HashMap<>();

        List<String> inventario =
                DibujoInventario.dibujar(
                        Map.of()
                );

        try {

            plantaciones =
                    granjaServicio.plantacionesPorParcela(
                            partida
                    );

            cultivos =
                    granjaServicio.cultivosDe(
                            plantaciones
                    );

        } catch (SQLException e) {

            mensaje =
                    "Error al cargar la granja: "
                            + e.getMessage();
        }

        try {

            Map<Item, Integer> contenido =
                    inventarioServicio.listarContenido(
                            partida
                    );

            inventario =
                    DibujoInventario.dibujar(
                            contenido
                    );

        } catch (SQLException e) {

            mensaje =
                    "Error al cargar el inventario: "
                            + e.getMessage();
        }

        System.out.println(
                Pantalla.armar(
                        opciones,
                        DibujoGranja.dibujarInfo(
                                partida,
                                mensaje
                        ),
                        inventario,
                        DibujoGranja.dibujarEscena(
                                partida,
                                plantaciones,
                                cultivos
                        )
                )
        );
    }


    private void verInventario(
            Partida partida
    ) {

        List<String> opciones =
                List.of(
                        "  INVENTARIO",
                        "",
                        "  Tus objetos se",
                        "  muestran a la",
                        "  derecha.",
                        "",
                        "  Enter para volver"
                );

        mensaje =
                "Inventario actual de la granja.";

        dibujarGranja(
                opciones,
                partida
        );

        Consola.pausar();

        mensaje = null;
    }


    private void plantar(
            Partida partida
    ) {

        Map<Cultivo, Integer> semillas;

        try {

            semillas =
                    granjaServicio.semillasDisponibles(
                            partida
                    );

        } catch (SQLException e) {

            mensaje =
                    "Error al buscar semillas: "
                            + e.getMessage();

            return;
        }

        if (semillas.isEmpty()) {

            mensaje =
                    "No tenes semillas. Compralas en el pueblo.";

            return;
        }

        // Paso las claves a una lista para poder elegirlas por numero.
        List<Cultivo> cultivos =
                new ArrayList<>(
                        semillas.keySet()
                );

        List<String> opciones =
                new ArrayList<>();

        opciones.add("  PLANTAR");
        opciones.add("");

        for (int i = 0;
             i < cultivos.size();
             i++) {

            Cultivo cultivo =
                    cultivos.get(i);

            opciones.add(
                    String.format(
                            "  %d. %-14s x%d",
                            i + 1,
                            cultivo.getNombre(),
                            semillas.get(cultivo)
                    )
            );
        }

        opciones.add("");
        opciones.add("  0. Volver");

        mensaje =
                "Elegi una semilla y despues la parcela";

        dibujarGranja(
                opciones,
                partida
        );

        mensaje = null;

        int eleccion =
                Consola.leerEntero(
                        "Semilla: ",
                        0,
                        cultivos.size()
                );

        if (eleccion == 0) {
            return;
        }

        Cultivo elegido =
                cultivos.get(
                        eleccion - 1
                );

        int parcela =
                Consola.leerEntero(
                        "Parcela (1-"
                                + partida.getParcelasDesbloqueadas()
                                + "): ",
                        1,
                        partida.getParcelasDesbloqueadas()
                );

        try {

            granjaServicio.plantar(
                    partida,
                    elegido,
                    parcela
            );

            mensaje =
                    "Plantaste "
                            + elegido.getNombre()
                            + " en la parcela "
                            + parcela
                            + ".";

        } catch (AccionNoPermitidaException
                 | StockInsuficienteException
                 | SQLException e) {

            mensaje =
                    "No se pudo plantar: "
                            + e.getMessage();
        }
    }


    private void cosechar(
            Partida partida
    ) {

        Map<Integer, Cultivo> listas;

        try {

            listas =
                    granjaServicio.parcelasListas(
                            partida
                    );

        } catch (SQLException e) {

            mensaje =
                    "Error al buscar las parcelas: "
                            + e.getMessage();

            return;
        }

        if (listas.isEmpty()) {

            mensaje =
                    "No hay cultivos listos para cosechar.";

            return;
        }

        /*
         * Paso los numeros de parcela a una lista
         * para elegirlos por numero de opcion.
         */
        List<Integer> parcelas =
                new ArrayList<>(
                        listas.keySet()
                );

        List<String> opciones =
                new ArrayList<>();

        opciones.add("  COSECHAR");
        opciones.add("");

        for (int i = 0;
             i < parcelas.size();
             i++) {

            int numeroParcela =
                    parcelas.get(i);

            opciones.add(
                    String.format(
                            "  %d. Parcela %d: %s",
                            i + 1,
                            numeroParcela,
                            listas.get(
                                    numeroParcela
                            ).getNombre()
                    )
            );
        }

        opciones.add("");
        opciones.add("  0. Volver");

        mensaje =
                "Elegi que parcela cosechar";

        dibujarGranja(
                opciones,
                partida
        );

        mensaje = null;

        int eleccion =
                Consola.leerEntero(
                        "Opcion: ",
                        0,
                        parcelas.size()
                );

        if (eleccion == 0) {
            return;
        }

        int numeroParcela =
                parcelas.get(
                        eleccion - 1
                );

        try {

            ResultadoCosecha resultado =
                    granjaServicio.cosechar(
                            partida,
                            numeroParcela
                    );

            mensaje =
                    "Cosechaste "
                            + resultado.cantidad()
                            + " "
                            + resultado.nombreCultivo()
                            + " (+"
                            + resultado.experiencia()
                            + " XP).";

            if (resultado.subioNivel()) {

                mensaje +=
                        " Subiste a nivel "
                                + partida.getNivel()
                                + "!";
            }

        } catch (AccionNoPermitidaException
                 | SQLException e) {

            mensaje =
                    "No se pudo cosechar: "
                            + e.getMessage();
        }
    }


    private void dormir(
            Partida partida
    ) {

        try {

            granjaServicio.dormir(
                    partida
            );

            Reloj reloj =
                    new Reloj(
                            partida.getMinutoJuegoTotal()
                    );

            mensaje =
                    "Dormiste. Ahora es "
                            + reloj.getTexto()
                            + ".";

        } catch (SQLException e) {

            mensaje =
                    "No se pudo dormir: "
                            + e.getMessage();
        }
    }


    private void guardar(
            Partida partida
    ) {

        try {

            new PartidaDao().modificar(
                    partida
            );

            System.out.println(
                    "Partida guardada."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar la partida: "
                            + e.getMessage()
            );
        }
    }
}