package menu.menuAdmin;

import dao.ItemDao;
import modelo.Item;
import modelo.enums.TipoItem;
import util.Consola;

import java.sql.SQLException;
import java.util.List;

/**
 * Submenu de administracion de items.
 *
 * Esta clase se encarga solamente de la interaccion con el administrador:
 * muestra opciones, pide datos por consola y muestra resultados.
 *
 * El acceso a MySQL queda delegado a ItemDao y las validaciones propias
 * del objeto Item siguen estando dentro de la clase Item.
 */
public class MenuItemsAdmin {

    private final ItemDao itemDao;


    public MenuItemsAdmin() {
        this.itemDao = new ItemDao();
    }


    public void mostrar() {
        int opcion;

        do {
            System.out.println("""

                    === GESTION DE ITEMS ===
                    1. Alta de item
                    2. Modificar item
                    3. Baja logica de item
                    4. Consultar item por ID
                    5. Listar items activos
                    0. Volver
                    """);

            opcion = Consola.leerEntero(
                    "Opcion: ",
                    0,
                    5
            );

            try {

                switch (opcion) {
                    case 1 -> altaItem();
                    case 2 -> modificarItem();
                    case 3 -> bajaItem();
                    case 4 -> consultarItem();
                    case 5 -> listarItems();
                    case 0 ->
                            System.out.println(
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

            /*
             * Pauso solamente si el usuario realizo alguna operacion.
             *
             * Si eligio 0, vuelve directamente al menu anterior.
             */
            if (opcion != 0) {
                Consola.pausar();
            }

        } while (opcion != 0);
    }


    /**
     * Da de alta un nuevo item.
     *
     * Primero pido todos los datos por consola,
     * despues construyo el objeto Item y finalmente
     * le pido al DAO que lo guarde en MySQL.
     */
    private void altaItem() throws SQLException {

        System.out.println("\n--- ALTA DE ITEM ---");

        String nombre =
                Consola.leerTexto("Nombre: ");

        String descripcion =
                Consola.leerTextoOpcional(
                        "Descripcion (opcional): "
                );

        TipoItem tipo = leerTipoItem();

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

        /*
         * Uso el constructor sin ID.
         *
         * El ID comienza en 0 porque todavia no existe
         * en la base de datos.
         *
         * ItemDao.alta() recupera despues el ID
         * generado por MySQL y se lo asigna al objeto.
         */
        Item nuevoItem = new Item(
                nombre,
                descripcion,
                tipo,
                precioCompra,
                precioVenta
        );

        if (itemDao.alta(nuevoItem)) {

            System.out.println(
                    "Item creado correctamente. ID generado: "
                            + nuevoItem.getIdItem()
            );

        } else {

            System.out.println(
                    "No se pudo crear el item."
            );
        }
    }


    private void modificarItem() throws SQLException {

        System.out.println("\n--- MODIFICAR ITEM ---");

        int id = Consola.leerEntero(
                "ID del item: ",
                1,
                Integer.MAX_VALUE
        );

        Item itemActual =
                itemDao.consultarPorId(id);

        if (itemActual == null) {

            System.out.println(
                    "No existe un item con ese ID."
            );

            return;
        }


        if (!itemActual.isActivo()) {

            System.out.println(
                    "El item esta dado de baja y no puede modificarse."
            );

            return;
        }

        System.out.println("Datos actuales:");

        mostrarItem(itemActual);

        System.out.println(
                "\nIngrese los nuevos datos:"
        );

        String nombre =
                Consola.leerTexto("Nombre: ");

        String descripcion =
                Consola.leerTextoOpcional(
                        "Descripcion (opcional): "
                );

        TipoItem tipo = leerTipoItem();

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


        Item itemModificado = new Item(
                itemActual.getIdItem(),
                nombre,
                descripcion,
                tipo,
                precioCompra,
                precioVenta,
                itemActual.isActivo()
        );

        if (itemDao.modificar(itemModificado)) {

            System.out.println(
                    "Item modificado correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo modificar el item."
            );
        }
    }


    private void bajaItem() throws SQLException {

        System.out.println("\n--- BAJA DE ITEM ---");

        int id = Consola.leerEntero(
                "ID del item: ",
                1,
                Integer.MAX_VALUE
        );

        Item item =
                itemDao.consultarPorId(id);

        if (item == null) {

            System.out.println(
                    "No existe un item con ese ID."
            );

            return;
        }

        if (!item.isActivo()) {

            System.out.println(
                    "El item ya se encuentra dado de baja."
            );

            return;
        }

        /*
         * Muestro el item antes de eliminarlo
         * para que el administrador pueda comprobar
         * que eligio el registro correcto.
         */
        mostrarItem(item);

        if (!Consola.confirmar(
                "¿Confirma la baja del item?"
        )) {

            System.out.println(
                    "Operacion cancelada."
            );

            return;
        }

        if (itemDao.bajaLogica(id)) {

            System.out.println(
                    "Item dado de baja correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo dar de baja el item."
            );
        }
    }


    private void consultarItem() throws SQLException {

        System.out.println("\n--- CONSULTAR ITEM ---");

        int id = Consola.leerEntero(
                "ID del item: ",
                1,
                Integer.MAX_VALUE
        );

        Item item =
                itemDao.consultarPorId(id);

        if (item == null) {

            System.out.println(
                    "No existe un item con ese ID."
            );

            return;
        }

        mostrarItem(item);
    }


    private void listarItems() throws SQLException {

        System.out.println("\n--- ITEMS ACTIVOS ---");

        List<Item> items =
                itemDao.listar();

        if (items.isEmpty()) {

            System.out.println(
                    "No hay items activos para mostrar."
            );

            return;
        }

        for (Item item : items) {

            mostrarItem(item);

            System.out.println(
                    "------------------------------"
            );
        }
    }


    private TipoItem leerTipoItem() {

        TipoItem[] tipos =
                TipoItem.values();

        System.out.println(
                "Tipo de item:"
        );

        for (int i = 0; i < tipos.length; i++) {

            System.out.println(
                    (i + 1)
                            + ". "
                            + tipos[i]
            );
        }

        int opcion =
                Consola.leerEntero(
                        "Tipo: ",
                        1,
                        tipos.length
                );

        /*
         * Las posiciones del array empiezan en 0,
         * pero las opciones que mostramos empiezan en 1.
         *
         * Por eso resto 1.
         */
        return tipos[opcion - 1];
    }

    
    private void mostrarItem(Item item) {

        System.out.println(
                "ID: " + item.getIdItem()
        );

        System.out.println(
                "Nombre: " + item.getNombre()
        );

        System.out.println(
                "Descripcion: "
                        + (
                        item.getDescripcion() == null
                                ? "Sin descripcion"
                                : item.getDescripcion()
                )
        );

        System.out.println(
                "Tipo: " + item.getTipo()
        );

        System.out.println(
                "Precio de compra: "
                        + item.getPrecioCompra()
        );

        System.out.println(
                "Precio de venta: "
                        + item.getPrecioVenta()
        );

        System.out.println(
                "Activo: "
                        + (
                        item.isActivo()
                                ? "Si"
                                : "No"
                )
        );
    }
}