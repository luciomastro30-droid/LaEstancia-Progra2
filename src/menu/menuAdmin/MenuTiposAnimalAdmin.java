package menu.menuAdmin;

import dao.ItemDao;
import dao.TipoAnimalDao;
import modelo.Item;
import modelo.TipoAnimal;
import modelo.enums.TipoItem;
import util.Consola;

import java.sql.SQLException;
import java.util.List;


public class MenuTiposAnimalAdmin {

    private final TipoAnimalDao tipoAnimalDao;
    private final ItemDao itemDao;


    public MenuTiposAnimalAdmin() {
        this.tipoAnimalDao = new TipoAnimalDao();
        this.itemDao = new ItemDao();
    }


    public void mostrar() {
        int opcion;

        do {
            System.out.println("""

                    === GESTION DE TIPOS DE ANIMALES ===
                    1. Alta de tipo de animal
                    2. Modificar tipo de animal
                    3. Baja logica de tipo de animal
                    4. Consultar tipo de animal por ID
                    5. Listar tipos de animales activos
                    0. Volver
                    """);

            opcion = Consola.leerEntero("Opcion: ", 0, 5);

            try {

                switch (opcion) {
                    case 1 -> altaTipoAnimal();
                    case 2 -> modificarTipoAnimal();
                    case 3 -> bajaTipoAnimal();
                    case 4 -> consultarTipoAnimal();
                    case 5 -> listarTiposAnimal();
                    case 0 -> System.out.println(
                            "Volviendo al menu administrador..."
                    );
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


    /**
     * Da de alta un nuevo tipo de animal.
     *
     * El item producido es opcional porque
     * id_item_producto permite NULL en MySQL.
     */
    private void altaTipoAnimal() throws SQLException {

        System.out.println("\n--- ALTA DE TIPO DE ANIMAL ---");

        String nombre =
                Consola.leerTexto("Nombre: ");

        String descripcion =
                Consola.leerTextoOpcional(
                        "Descripcion (opcional): "
                );

        int precioCompra =
                Consola.leerEntero(
                        "Precio de compra: ",
                        0,
                        Integer.MAX_VALUE
                );

        int precioVenta =
                Consola.leerEntero(
                        "Precio de venta: ",
                        0,
                        Integer.MAX_VALUE
                );


        System.out.println(
                "\nItems disponibles como producto animal:"
        );

        mostrarItemsProductoAnimal();

        Integer idItemProducto =
                leerItemProductoOpcional(
                        "ID del item producto (0 = sin asociar): "
                );


        int cantidadProducto =
                Consola.leerEntero(
                        "Cantidad de producto generada: ",
                        1,
                        Integer.MAX_VALUE
                );

        int tiempoProduccion =
                Consola.leerEntero(
                        "Tiempo de produccion en minutos: ",
                        1,
                        Integer.MAX_VALUE
                );


        TipoAnimal nuevoTipoAnimal = new TipoAnimal(
                nombre,
                descripcion,
                precioCompra,
                precioVenta,
                idItemProducto,
                cantidadProducto,
                tiempoProduccion
        );

        if (tipoAnimalDao.alta(nuevoTipoAnimal)) {

            System.out.println(
                    "Tipo de animal creado correctamente. ID generado: "
                            + nuevoTipoAnimal.getId()
            );

        } else {

            System.out.println(
                    "No se pudo crear el tipo de animal."
            );
        }
    }


    private void modificarTipoAnimal() throws SQLException {

        System.out.println(
                "\n--- MODIFICAR TIPO DE ANIMAL ---"
        );

        int id =
                Consola.leerEntero(
                        "ID del tipo de animal: ",
                        1,
                        Integer.MAX_VALUE
                );

        TipoAnimal tipoActual =
                tipoAnimalDao.consultarPorId(id);

        if (tipoActual == null) {

            System.out.println(
                    "No existe un tipo de animal con ese ID."
            );

            return;
        }

        /*
         * consultarPorId() puede recuperar tambien
         * registros dados de baja.
         *
         * TipoAnimalDao.modificar() solamente modifica
         * registros activos.
         */
        if (!tipoActual.isActivo()) {

            System.out.println(
                    "El tipo de animal esta dado de baja "
                            + "y no puede modificarse."
            );

            return;
        }

        System.out.println("Datos actuales:");

        mostrarTipoAnimal(tipoActual);

        System.out.println(
                "\nIngrese los nuevos datos:"
        );

        String nombre =
                Consola.leerTexto("Nombre: ");

        String descripcion =
                Consola.leerTextoOpcional(
                        "Descripcion (opcional): "
                );

        int precioCompra =
                Consola.leerEntero(
                        "Precio de compra: ",
                        0,
                        Integer.MAX_VALUE
                );

        int precioVenta =
                Consola.leerEntero(
                        "Precio de venta: ",
                        0,
                        Integer.MAX_VALUE
                );

        System.out.println(
                "\nItems disponibles como producto animal:"
        );

        mostrarItemsProductoAnimal();

        Integer idItemProducto =
                leerItemProductoOpcional(
                        "ID del item producto (0 = sin asociar): "
                );

        int cantidadProducto =
                Consola.leerEntero(
                        "Cantidad de producto generada: ",
                        1,
                        Integer.MAX_VALUE
                );

        int tiempoProduccion =
                Consola.leerEntero(
                        "Tiempo de produccion en minutos: ",
                        1,
                        Integer.MAX_VALUE
                );


        TipoAnimal tipoModificado = new TipoAnimal(
                tipoActual.getId(),
                nombre,
                descripcion,
                precioCompra,
                precioVenta,
                idItemProducto,
                cantidadProducto,
                tiempoProduccion,
                tipoActual.isActivo()
        );

        if (tipoAnimalDao.modificar(tipoModificado)) {

            System.out.println(
                    "Tipo de animal modificado correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo modificar el tipo de animal."
            );
        }
    }



    private void bajaTipoAnimal() throws SQLException {

        System.out.println(
                "\n--- BAJA DE TIPO DE ANIMAL ---"
        );

        int id =
                Consola.leerEntero(
                        "ID del tipo de animal: ",
                        1,
                        Integer.MAX_VALUE
                );

        TipoAnimal tipoAnimal =
                tipoAnimalDao.consultarPorId(id);

        if (tipoAnimal == null) {

            System.out.println(
                    "No existe un tipo de animal con ese ID."
            );

            return;
        }

        if (!tipoAnimal.isActivo()) {

            System.out.println(
                    "El tipo de animal ya se encuentra dado de baja."
            );

            return;
        }

        mostrarTipoAnimal(tipoAnimal);

        if (!Consola.confirmar(
                "¿Confirma la baja del tipo de animal?"
        )) {

            System.out.println(
                    "Operacion cancelada."
            );

            return;
        }

        if (tipoAnimalDao.bajaLogica(id)) {

            System.out.println(
                    "Tipo de animal dado de baja correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo dar de baja el tipo de animal."
            );
        }
    }


    private void consultarTipoAnimal() throws SQLException {

        System.out.println(
                "\n--- CONSULTAR TIPO DE ANIMAL ---"
        );

        int id =
                Consola.leerEntero(
                        "ID del tipo de animal: ",
                        1,
                        Integer.MAX_VALUE
                );

        TipoAnimal tipoAnimal =
                tipoAnimalDao.consultarPorId(id);

        if (tipoAnimal == null) {

            System.out.println(
                    "No existe un tipo de animal con ese ID."
            );

            return;
        }

        mostrarTipoAnimal(tipoAnimal);
    }


    private void listarTiposAnimal() throws SQLException {

        System.out.println(
                "\n--- TIPOS DE ANIMALES ACTIVOS ---"
        );

        List<TipoAnimal> tipos =
                tipoAnimalDao.listar();

        if (tipos.isEmpty()) {

            System.out.println(
                    "No hay tipos de animales activos para mostrar."
            );

            return;
        }

        for (TipoAnimal tipoAnimal : tipos) {

            mostrarTipoAnimal(tipoAnimal);

            System.out.println(
                    "------------------------------"
            );
        }
    }


    /**
     * Lee el ID del Item producido por el animal.
     *
     * Si el administrador ingresa 0:
     *
     * 0 -> null
     *
     * porque id_item_producto permite NULL.
     *
     * Si ingresa un ID real, compruebo que:
     * - exista
     * - siga activo
     * - sea de tipo PRODUCTO_ANIMAL
     */
    private Integer leerItemProductoOpcional(
            String mensaje
    ) throws SQLException {

        while (true) {

            int idItem =
                    Consola.leerEntero(
                            mensaje,
                            0,
                            Integer.MAX_VALUE
                    );

            /*
             * El 0 solo representa la opcion
             * "sin producto asociado".
             *
             * Nunca se guarda 0 como FK.
             */
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

            /*
             * La FK solamente comprueba que el Item exista.
             *
             * Esta validacion evita asociar por error
             * una SEMILLA, PESCADO, MATERIAL, etc.
             */
            if (item.getTipo() != TipoItem.PRODUCTO_ANIMAL) {

                System.out.println(
                        "El item elegido debe ser de tipo PRODUCTO_ANIMAL."
                );

                continue;
            }

            return idItem;
        }
    }


    private void mostrarItemsProductoAnimal()
            throws SQLException {

        List<Item> items =
                itemDao.listar();

        boolean encontrado = false;

        for (Item item : items) {

            if (item.getTipo()
                    == TipoItem.PRODUCTO_ANIMAL) {

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
                    "No hay items activos de tipo PRODUCTO_ANIMAL."
            );
        }
    }



    private void mostrarTipoAnimal(
            TipoAnimal tipoAnimal
    ) {

        System.out.println(
                "ID: "
                        + tipoAnimal.getId()
        );

        System.out.println(
                "Nombre: "
                        + tipoAnimal.getNombre()
        );

        System.out.println(
                "Descripcion: "
                        + (
                        tipoAnimal.getDescripcion() == null
                                ? "Sin descripcion"
                                : tipoAnimal.getDescripcion()
                )
        );

        System.out.println(
                "Precio de compra: "
                        + tipoAnimal.getPrecioCompra()
        );

        System.out.println(
                "Precio de venta: "
                        + tipoAnimal.getPrecioVenta()
        );

        System.out.println(
                "ID item producto: "
                        + (
                        tipoAnimal.getIdItemProducto() == null
                                ? "Sin asociar"
                                : tipoAnimal.getIdItemProducto()
                )
        );

        System.out.println(
                "Cantidad de producto: "
                        + tipoAnimal.getCantidadProducto()
        );

        System.out.println(
                "Tiempo de produccion: "
                        + tipoAnimal.getTiempoProduccionMin()
                        + " minutos"
        );

        System.out.println(
                "Activo: "
                        + (
                        tipoAnimal.isActivo()
                                ? "Si"
                                : "No"
                )
        );
    }
}