package servicio;

import conexion.ConexionBD;
import dao.InventarioDao;
import dao.ItemDao;
import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import excepciones.StockInsuficienteException;
import modelo.Inventario;
import modelo.Item;
import modelo.Partida;
import modelo.enums.TipoItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gestiona la lógica de venta de productos en el mercado.
 * Coordina el inventario del jugador, los items vendibles y las monedas de la partida.
 */
public class MercadoServicio {

    private final InventarioDao inventarioDao;
    private final ItemDao itemDao;
    private final PartidaDao partidaDao;


    public MercadoServicio() {
        this.inventarioDao = new InventarioDao();
        this.itemDao = new ItemDao();
        this.partidaDao = new PartidaDao();
    }


    public Map<Item, Integer> productosVendibles(
            Partida partida
    ) throws SQLException {

        if (partida == null) {
            throw new IllegalArgumentException(
                    "La partida no puede ser null."
            );
        }

        /*
         * Uso LinkedHashMap para relacionar cada item vendible con su cantidad
         * disponible manteniendo el orden en que fueron cargados.
         */
        Map<Item, Integer> productos =
                new LinkedHashMap<>();

        for (Inventario fila :
                inventarioDao.listarPorPartida(
                        partida.getIdPartida()
                )) {

            Item item =
                    itemDao.consultarPorId(
                            fila.getIdItem()
                    );

            if (item != null
                    && esVendible(item)) {

                productos.put(
                        item,
                        fila.getCantidad()
                );
            }
        }

        return productos;
    }


    public int gananciaTotal(
            Item item,
            int cantidad
    ) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "El item no puede ser null."
            );
        }

        validarCantidad(cantidad);

        long total =
                (long) item.getPrecioVenta()
                        * cantidad;

        if (total > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "La cantidad solicitada es demasiado grande."
            );
        }

        return (int) total;
    }


    public void vender(
            Partida partida,
            Item item,
            int cantidad
    ) throws SQLException,
            AccionNoPermitidaException,
            StockInsuficienteException {

        if (partida == null) {
            throw new IllegalArgumentException(
                    "La partida no puede ser null."
            );
        }

        if (item == null) {
            throw new IllegalArgumentException(
                    "El item no puede ser null."
            );
        }

        validarCantidad(cantidad);

        if (!esVendible(item)) {
            throw new AccionNoPermitidaException(
                    "Ese item no se puede vender en el mercado."
            );
        }

        Inventario fila =
                inventarioDao.consultarPorPartidaEItem(
                        partida.getIdPartida(),
                        item.getIdItem()
                );

        int disponible =
                fila == null
                        ? 0
                        : fila.getCantidad();

        if (disponible < cantidad) {
            throw new StockInsuficienteException(
                    cantidad,
                    disponible
            );
        }

        int ganancia =
                gananciaTotal(
                        item,
                        cantidad
                );

        long monedasFinales =
                (long) partida.getMonedas()
                        + ganancia;

        if (monedasFinales > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "La cantidad de monedas resultante es demasiado grande."
            );
        }

        int monedasAnteriores =
                partida.getMonedas();

        int cantidadAnterior =
                fila.getCantidad();

        LocalDateTime fechaGuardadoAnterior =
                partida.getFechaGuardado();


        /*
         * La venta modifica inventario y monedas.
         * Ambas operaciones deben confirmarse juntas.
         */
        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                fila.setCantidad(
                        cantidadAnterior - cantidad
                );

                if (!inventarioDao.modificar(
                        con,
                        fila
                )) {

                    throw new SQLException(
                            "No se pudo actualizar el inventario."
                    );
                }

                partida.setMonedas(
                        (int) monedasFinales
                );

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar la venta."
                    );
                }

                con.commit();

            } catch (SQLException e) {

                con.rollback();

                /*
                 * El rollback restaura MySQL,
                 * pero tambien restauramos los objetos Java.
                 */
                fila.setCantidad(
                        cantidadAnterior
                );

                partida.setMonedas(
                        monedasAnteriores
                );

                partida.setFechaGuardado(
                        fechaGuardadoAnterior
                );

                throw e;
            }
        }
    }


    private boolean esVendible(
            Item item
    ) {

        if (item == null
                || !item.isActivo()
                || item.getPrecioVenta() <= 0) {

            return false;
        }

        return item.getTipo() == TipoItem.COSECHA
                || item.getTipo() == TipoItem.PRODUCTO_ANIMAL
                || item.getTipo() == TipoItem.MATERIAL
                || item.getTipo() == TipoItem.PESCADO;
    }


    private void validarCantidad(
            int cantidad
    ) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a 0."
            );
        }
    }
}