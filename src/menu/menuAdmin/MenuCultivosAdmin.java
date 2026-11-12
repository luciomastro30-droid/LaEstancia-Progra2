package menu.menuAdmin;

import dao.CultivoDao;
import dao.ItemDao;
import modelo.Cultivo;
import modelo.Item;
import modelo.enums.TipoItem;
import util.Consola;

import java.sql.SQLException;
import java.util.List;

public class MenuCultivosAdmin {

    private final CultivoDao cultivoDao;
    private final ItemDao itemDao;

    public MenuCultivosAdmin() {
        this.cultivoDao = new CultivoDao();
        this.itemDao = new ItemDao();
    }


    public void mostrar() {
        int opcion;

        do {
            System.out.println("""

                    === GESTION DE CULTIVOS ===
                    1. Alta de cultivo
                    2. Modificar cultivo
                    3. Baja logica de cultivo
                    4. Consultar cultivo por ID
                    5. Listar cultivos activos
                    0. Volver
                    """);

            opcion = Consola.leerEntero("Opcion: ", 0, 5);

            try {
                switch (opcion) {
                    case 1 -> altaCultivo();
                    case 2 -> modificarCultivo();
                    case 3 -> bajaCultivo();
                    case 4 -> consultarCultivo();
                    case 5 -> listarCultivos();
                    case 0 -> System.out.println("Volviendo al menu administrador...");
                }

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "No se pudo realizar la operacion: "
                                + e.getMessage()
                );

            } catch (SQLException e) {

                System.out.println(
                        "Ocurrio un error al acceder a la base de datos."
                );
            }

            if (opcion != 0) {
                Consola.pausar();
            }

        } while (opcion != 0);
    }



    private void altaCultivo() throws SQLException {

        System.out.println("\n--- ALTA DE CULTIVO ---");

        String nombre =
                Consola.leerTexto("Nombre: ");

        int precioSemilla =
                Consola.leerEntero(
                        "Precio de la semilla: ",
                        0,
                        Integer.MAX_VALUE
                );

        int precioVenta =
                Consola.leerEntero(
                        "Precio de venta: ",
                        0,
                        Integer.MAX_VALUE
                );

        int tiempoCrecimiento =
                Consola.leerEntero(
                        "Tiempo de crecimiento en minutos: ",
                        1,
                        Integer.MAX_VALUE
                );

        int experiencia =
                Consola.leerEntero(
                        "Experiencia de recompensa: ",
                        0,
                        Integer.MAX_VALUE
                );

        /*
         * Muestro primero las semillas disponibles para que
         * el administrador conozca los IDs que puede utilizar.
         */
        System.out.println("\nItems disponibles como semilla:");

        mostrarItemsPorTipo(TipoItem.SEMILLA);

        Integer idItemSemilla =
                leerItemOpcional(
                        "ID del item semilla (0 = sin asociar): ",
                        TipoItem.SEMILLA
                );

        /*
         * Hago lo mismo con los items de tipo COSECHA.
         */
        System.out.println("\nItems disponibles como cosecha:");

        mostrarItemsPorTipo(TipoItem.COSECHA);

        Integer idItemCosecha =
                leerItemOpcional(
                        "ID del item cosecha (0 = sin asociar): ",
                        TipoItem.COSECHA
                );


        Cultivo nuevoCultivo = new Cultivo(
                nombre,
                precioSemilla,
                precioVenta,
                tiempoCrecimiento,
                experiencia,
                idItemSemilla,
                idItemCosecha
        );

        if (cultivoDao.alta(nuevoCultivo)) {

            System.out.println(
                    "Cultivo creado correctamente. ID generado: "
                            + nuevoCultivo.getId()
            );

        } else {

            System.out.println(
                    "No se pudo crear el cultivo."
            );
        }
    }



    private void modificarCultivo() throws SQLException {

        System.out.println("\n--- MODIFICAR CULTIVO ---");

        int id =
                Consola.leerEntero(
                        "ID del cultivo: ",
                        1,
                        Integer.MAX_VALUE
                );

        Cultivo cultivoActual =
                cultivoDao.consultarPorId(id);

        if (cultivoActual == null) {

            System.out.println(
                    "No existe un cultivo con ese ID."
            );

            return;
        }

        /*
         * consultarPorId() puede recuperar tambien
         * un cultivo dado de baja.
         *
         * CultivoDao.modificar() solo modifica registros activos.
         */
        if (!cultivoActual.isActivo()) {

            System.out.println(
                    "El cultivo esta dado de baja y no puede modificarse."
            );

            return;
        }

        System.out.println("Datos actuales:");

        mostrarCultivo(cultivoActual);

        System.out.println(
                "\nIngrese los nuevos datos:"
        );

        String nombre =
                Consola.leerTexto("Nombre: ");

        int precioSemilla =
                Consola.leerEntero(
                        "Precio de la semilla: ",
                        0,
                        Integer.MAX_VALUE
                );

        int precioVenta =
                Consola.leerEntero(
                        "Precio de venta: ",
                        0,
                        Integer.MAX_VALUE
                );

        int tiempoCrecimiento =
                Consola.leerEntero(
                        "Tiempo de crecimiento en minutos: ",
                        1,
                        Integer.MAX_VALUE
                );

        int experiencia =
                Consola.leerEntero(
                        "Experiencia de recompensa: ",
                        0,
                        Integer.MAX_VALUE
                );


        System.out.println("\nItems disponibles como semilla:");

        mostrarItemsPorTipo(TipoItem.SEMILLA);

        Integer idItemSemilla =
                leerItemOpcional(
                        "ID del item semilla (0 = sin asociar): ",
                        TipoItem.SEMILLA
                );


        System.out.println("\nItems disponibles como cosecha:");

        mostrarItemsPorTipo(TipoItem.COSECHA);

        Integer idItemCosecha =
                leerItemOpcional(
                        "ID del item cosecha (0 = sin asociar): ",
                        TipoItem.COSECHA
                );


        Cultivo cultivoModificado = new Cultivo(
                cultivoActual.getId(),
                nombre,
                precioSemilla,
                precioVenta,
                tiempoCrecimiento,
                experiencia,
                idItemSemilla,
                idItemCosecha,
                cultivoActual.isActivo()
        );

        if (cultivoDao.modificar(cultivoModificado)) {

            System.out.println(
                    "Cultivo modificado correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo modificar el cultivo."
            );
        }
    }



    private void bajaCultivo() throws SQLException {

        System.out.println("\n--- BAJA DE CULTIVO ---");

        int id =
                Consola.leerEntero(
                        "ID del cultivo: ",
                        1,
                        Integer.MAX_VALUE
                );

        Cultivo cultivo =
                cultivoDao.consultarPorId(id);

        if (cultivo == null) {

            System.out.println(
                    "No existe un cultivo con ese ID."
            );

            return;
        }

        if (!cultivo.isActivo()) {

            System.out.println(
                    "El cultivo ya se encuentra dado de baja."
            );

            return;
        }

        /*
         * Muestro el cultivo antes de pedir confirmacion
         * para evitar eliminar el registro equivocado.
         */
        mostrarCultivo(cultivo);

        if (!Consola.confirmar(
                "¿Confirma la baja del cultivo?"
        )) {

            System.out.println(
                    "Operacion cancelada."
            );

            return;
        }

        if (cultivoDao.bajaLogica(id)) {

            System.out.println(
                    "Cultivo dado de baja correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo dar de baja el cultivo."
            );
        }
    }



    private void consultarCultivo() throws SQLException {

        System.out.println("\n--- CONSULTAR CULTIVO ---");

        int id =
                Consola.leerEntero(
                        "ID del cultivo: ",
                        1,
                        Integer.MAX_VALUE
                );

        Cultivo cultivo =
                cultivoDao.consultarPorId(id);

        if (cultivo == null) {

            System.out.println(
                    "No existe un cultivo con ese ID."
            );

            return;
        }

        mostrarCultivo(cultivo);
    }



    private void listarCultivos() throws SQLException {

        System.out.println("\n--- CULTIVOS ACTIVOS ---");

        List<Cultivo> cultivos =
                cultivoDao.listar();

        if (cultivos.isEmpty()) {

            System.out.println(
                    "No hay cultivos activos para mostrar."
            );

            return;
        }

        for (Cultivo cultivo : cultivos) {

            mostrarCultivo(cultivo);

            System.out.println(
                    "------------------------------"
            );
        }
    }


    /**
     * Lee el ID de un Item que puede ser opcional.
     *
     * Si el administrador ingresa 0, devuelvo null porque
     * id_item_semilla e id_item_cosecha permiten NULL.
     *
     * Si ingresa un ID real, compruebo que:
     *
     * - exista
     * - siga activo
     * - tenga el tipo correcto
     *
     * Esto evita, por ejemplo, asociar un pescado
     * como semilla de un cultivo.
     */
    private Integer leerItemOpcional(
            String mensaje,
            TipoItem tipoEsperado
    ) throws SQLException {

        while (true) {

            int idItem =
                    Consola.leerEntero(
                            mensaje,
                            0,
                            Integer.MAX_VALUE
                    );


            if (idItem == 0) {
                return null;
            }

            Item item =
                    itemDao.consultarPorId(idItem);

            if (item == null) {

                System.out.println(
                        "No existe un item con ese ID."
                );

                continue;
            }

            if (!item.isActivo()) {

                System.out.println(
                        "Ese item se encuentra dado de baja."
                );

                continue;
            }

            if (item.getTipo() != tipoEsperado) {

                System.out.println(
                        "El item elegido debe ser de tipo "
                                + tipoEsperado
                                + "."
                );

                continue;
            }

            return idItem;
        }
    }


    /**
     * Muestra los Items activos de un tipo determinado.
     *
     * Lo utilizo antes de pedir el ID para que
     * el administrador pueda ver las opciones disponibles.
     */
    private void mostrarItemsPorTipo(
            TipoItem tipo
    ) throws SQLException {

        List<Item> items =
                itemDao.listar();

        boolean encontrado = false;

        for (Item item : items) {

            if (item.getTipo() == tipo) {

                System.out.println(
                        "ID: "
                                + item.getIdItem()
                                + " | Nombre: "
                                + item.getNombre()
                );

                encontrado = true;
            }
        }

        if (!encontrado) {

            System.out.println(
                    "No hay items activos de tipo "
                            + tipo
                            + "."
            );
        }
    }


    private void mostrarCultivo(
            Cultivo cultivo
    ) {

        System.out.println(
                "ID: "
                        + cultivo.getId()
        );

        System.out.println(
                "Nombre: "
                        + cultivo.getNombre()
        );

        System.out.println(
                "Precio semilla: "
                        + cultivo.getPrecioSemilla()
        );

        System.out.println(
                "Precio venta: "
                        + cultivo.getPrecioVenta()
        );

        System.out.println(
                "Tiempo de crecimiento: "
                        + cultivo.getTiempoCrecimientoMin()
                        + " minutos"
        );

        System.out.println(
                "Experiencia de recompensa: "
                        + cultivo.getExperienciaRecompensa()
        );

        System.out.println(
                "ID item semilla: "
                        + (
                        cultivo.getIdItemSemilla() == null
                                ? "Sin asociar"
                                : cultivo.getIdItemSemilla()
                )
        );

        System.out.println(
                "ID item cosecha: "
                        + (
                        cultivo.getIdItemCosecha() == null
                                ? "Sin asociar"
                                : cultivo.getIdItemCosecha()
                )
        );

        System.out.println(
                "Activo: "
                        + (
                        cultivo.isActivo()
                                ? "Si"
                                : "No"
                )
        );
    }
}