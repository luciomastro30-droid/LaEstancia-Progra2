package menu.menuJugador;

import modelo.Cultivo;
import modelo.Item;
import modelo.Partida;
import modelo.Plantacion;
import modelo.enums.Alojamiento;
import servicio.AnimalServicio;
import servicio.GranjaServicio;
import servicio.InventarioServicio;
import vista.DibujoGranja;
import vista.DibujoInventario;
import vista.Pantalla;
import util.Consola;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PantallaJugador {

    private final GranjaServicio granjaServicio;
    private final InventarioServicio inventarioServicio;
    private final AnimalServicio animalServicio;


    public PantallaJugador() {
        this.granjaServicio = new GranjaServicio();
        this.inventarioServicio = new InventarioServicio();
        this.animalServicio = new AnimalServicio();
    }


    public String mostrar(
            List<String> opciones,
            Partida partida,
            String mensaje
    ) {

        return mostrar(
                opciones,
                partida,
                mensaje,
                null
        );
    }


    /*
     * Permite usar una escena distinta a la granja,
     * por ejemplo el dibujo del pueblo.
     */
    public String mostrar(
            List<String> opciones,
            Partida partida,
            String mensaje,
            List<String> escenaPersonalizada
    ) {

        Map<Integer, Plantacion> plantaciones =
                new HashMap<>();

        Map<Integer, Cultivo> cultivos =
                new HashMap<>();

        Map<String, Integer> gallinero =
                new HashMap<>();

        Map<String, Integer> establo =
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

        try {

            gallinero =
                    animalServicio.cantidadPorTipo(
                            partida,
                            Alojamiento.GALLINERO
                    );

            establo =
                    animalServicio.cantidadPorTipo(
                            partida,
                            Alojamiento.ESTABLO
                    );

        } catch (SQLException e) {

            mensaje =
                    "Error al cargar los animales: "
                            + e.getMessage();
        }

        List<String> escena;

        if (escenaPersonalizada == null) {

            escena =
                    DibujoGranja.dibujarEscena(
                            partida,
                            plantaciones,
                            cultivos,
                            gallinero,
                            establo
                    );

        } else {

            escena =
                    escenaPersonalizada;
        }

        Consola.limpiarPantalla();

        System.out.println(
                Pantalla.armar(
                        opciones,
                        DibujoGranja.dibujarInfo(
                                partida,
                                mensaje
                        ),
                        inventario,
                        escena
                )
        );

        return mensaje;
    }
}