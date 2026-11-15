package menu.menuJugador;

import excepciones.AccionNoPermitidaException;
import excepciones.MonedasInsuficientesException;
import modelo.Partida;
import modelo.TipoAnimal;
import servicio.AnimalServicio;
import util.Consola;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuAnimales {

    private final AnimalServicio animalServicio;
    private final PantallaJugador pantallaJugador;

    private String mensaje;


    public MenuAnimales(PantallaJugador pantallaJugador) {
        this.animalServicio = new AnimalServicio();
        this.pantallaJugador = pantallaJugador;
    }


    public void mostrar(Partida partida) {

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
                        comprarAnimal(partida);

                case 2 ->
                        alimentarAnimales(partida);

                case 3 ->
                        recolectarProductosAnimales(partida);
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

        opciones.add("  COMPRAR ANIMAL");
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

        } catch (AccionNoPermitidaException
                 | SQLException e) {

            mensaje =
                    "No se pudieron recolectar los productos: "
                            + e.getMessage();
        }
    }
}