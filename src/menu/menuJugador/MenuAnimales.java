package menu.menuJugador;

import excepciones.AccionNoPermitidaException;
import excepciones.MonedasInsuficientesException;
import modelo.Partida;
import modelo.TipoAnimal;
import modelo.enums.DefinicionMision;
import modelo.enums.TipoObjetivo;
import servicio.AnimalServicio;
import servicio.MisionServicio;
import util.Consola;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuAnimales {

    private final AnimalServicio animalServicio;
    private final MisionServicio misionServicio;
    private final PantallaJugador pantallaJugador;
    private final MenuEvento menuEvento;

    private String mensaje;


    public MenuAnimales(
            PantallaJugador pantallaJugador
    ) {

        this.animalServicio =
                new AnimalServicio();

        this.misionServicio =
                new MisionServicio();

        this.pantallaJugador =
                pantallaJugador;

        this.menuEvento =
                new MenuEvento(
                        pantallaJugador
                );
    }


    public void mostrar(
            Partida partida
    ) {

        int opcion;

        do {

            List<String> opciones =
                    List.of(
                            "  ANIMALES",
                            "",
                            "  1. Comprar animal",
                            "  2. Alimentar",
                            "  3. Recolectar",
                            "     productos",
                            "",
                            "  0. Volver"
                    );

            mensaje =
                    pantallaJugador.mostrar(
                            opciones,
                            partida,
                            mensaje
                    );

            opcion =
                    Consola.leerEntero(
                            "Opcion: ",
                            0,
                            3
                    );

            mensaje = null;

            switch (opcion) {

                case 1 ->
                        comprarAnimal(
                                partida
                        );

                case 2 ->
                        alimentarAnimales(
                                partida
                        );

                case 3 ->
                        recolectarProductosAnimales(
                                partida
                        );
            }

        } while (opcion != 0);
    }


    private void comprarAnimal(
            Partida partida
    ) {

        List<TipoAnimal> tipos;

        try {

            tipos =
                    animalServicio.tiposDisponibles();

        } catch (SQLException e) {

            mensaje =
                    "No se pudieron cargar los animales: "
                            + e.getMessage();

            return;
        }

        if (tipos.isEmpty()) {

            mensaje =
                    "No hay animales disponibles.";

            return;
        }

        List<String> opciones =
                new ArrayList<>();

        opciones.add(
                "  COMPRAR ANIMAL"
        );

        opciones.add("");

        for (int i = 0;
             i < tipos.size();
             i++) {

            TipoAnimal tipo =
                    tipos.get(i);

            opciones.add(
                    String.format(
                            "  %d. %s $%d",
                            i + 1,
                            tipo.getNombre(),
                            tipo.getPrecioCompra()
                    )
            );
        }

        opciones.add("");
        opciones.add("  0. Volver");

        mensaje =
                "Elegi el animal que queres comprar.";

        mensaje =
                pantallaJugador.mostrar(
                        opciones,
                        partida,
                        mensaje
                );

        mensaje = null;

        int eleccion =
                Consola.leerEntero(
                        "Animal: ",
                        0,
                        tipos.size()
                );

        if (eleccion == 0) {
            return;
        }

        TipoAnimal elegido =
                tipos.get(
                        eleccion - 1
                );

        String nombre =
                Consola.leerTexto(
                        "Nombre del animal: "
                );

        try {

            animalServicio.comprar(
                    partida,
                    elegido,
                    nombre
            );

            mensaje =
                    "Compraste "
                            + elegido.getNombre()
                            + " llamado "
                            + nombre
                            + ".";

        } catch (AccionNoPermitidaException
                 | MonedasInsuficientesException
                 | SQLException e) {

            mensaje =
                    "No se pudo comprar el animal: "
                            + e.getMessage();
        }
    }


    private void alimentarAnimales(
            Partida partida
    ) {

        try {

            int cantidad =
                    animalServicio.alimentar(
                            partida
                    );

            mensaje =
                    "Alimentaste "
                            + cantidad
                            + (cantidad == 1
                            ? " animal."
                            : " animales.");

            /*
             * Alimentar avanza el tiempo, por eso
             * puede aparecer un evento.
             */
            menuEvento.intentarMostrar(
                    partida
            );

        } catch (AccionNoPermitidaException
                 | SQLException e) {

            mensaje =
                    "No se pudo alimentar: "
                            + e.getMessage();
        }
    }


    private void recolectarProductosAnimales(
            Partida partida
    ) {

        try {

            Map<String, Integer> productos =
                    animalServicio.recolectarProductos(
                            partida
                    );

            StringBuilder resultado =
                    new StringBuilder(
                            "Recolectaste "
                    );

            int i = 0;

            for (Map.Entry<String, Integer> producto
                    : productos.entrySet()) {

                if (i > 0) {
                    resultado.append(", ");
                }

                resultado
                        .append(
                                producto.getKey()
                        )
                        .append(" x")
                        .append(
                                producto.getValue()
                        );

                i++;
            }

            resultado.append(".");

            mensaje =
                    resultado.toString();

            registrarProgresoProductos(
                    partida,
                    productos
            );

            /*
             * Recolectar productos avanza el tiempo,
             * por eso puede aparecer un evento.
             */
            menuEvento.intentarMostrar(
                    partida
            );

        } catch (AccionNoPermitidaException
                 | SQLException e) {

            mensaje =
                    "No se pudieron recolectar los productos: "
                            + e.getMessage();
        }
    }


    private void registrarProgresoProductos(
            Partida partida,
            Map<String, Integer> productos
    ) {

        int huevos = 0;
        int leche = 0;

        for (Map.Entry<String, Integer> producto
                : productos.entrySet()) {

            String nombre =
                    producto.getKey();

            int cantidad =
                    producto.getValue();

            if (nombre.startsWith("Huevo")) {

                huevos +=
                        cantidad;
            }

            if (nombre.startsWith("Leche")) {

                leche +=
                        cantidad;
            }
        }

        if (huevos > 0) {

            registrarProgresoMision(
                    partida,
                    TipoObjetivo.RECOLECTAR_HUEVO,
                    huevos
            );
        }

        if (leche > 0) {

            registrarProgresoMision(
                    partida,
                    TipoObjetivo.ORDENIAR,
                    leche
            );
        }
    }


    private void registrarProgresoMision(
            Partida partida,
            TipoObjetivo tipoObjetivo,
            int cantidad
    ) {

        try {

            List<DefinicionMision> completadas =
                    misionServicio.registrarProgreso(
                            partida,
                            tipoObjetivo,
                            cantidad
                    );

            mensaje +=
                    mensajeMisionesCompletadas(
                            completadas
                    );

        } catch (SQLException e) {

            mensaje +=
                    " No se pudo actualizar el progreso de las misiones.";
        }
    }


    private String mensajeMisionesCompletadas(
            List<DefinicionMision> completadas
    ) {

        StringBuilder texto =
                new StringBuilder();

        for (DefinicionMision mision
                : completadas) {

            texto.append(
                    " Completaste la mision de "
            );

            texto.append(
                    mision.getNpc()
            );

            texto.append(".");
        }

        return texto.toString();
    }
}