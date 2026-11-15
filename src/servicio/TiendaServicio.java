package servicio;

import conexion.ConexionBD;
import dao.InventarioDao;
import dao.ItemDao;
import dao.PartidaDao;
import excepciones.AccionNoPermitidaException;
import excepciones.MonedasInsuficientesException;
import modelo.Inventario;
import modelo.Item;
import modelo.Partida;
import modelo.enums.TipoItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TiendaServicio {

    private final ItemDao itemDao;
    private final InventarioDao inventarioDao;
    private final PartidaDao partidaDao;


    public TiendaServicio() {
        this.itemDao = new ItemDao();
        this.inventarioDao = new InventarioDao();
        this.partidaDao = new PartidaDao();
    }


    public List<Item> productosDisponibles()
            throws SQLException {

        List<Item> disponibles =
                new ArrayList<>();

        for (Item item : itemDao.listar()) {

            if (esComprable(item)) {
                disponibles.add(item);
            }
        }

        return disponibles;
    }


    public int costoTotal(
            Item item,
            int cantidad
    ) {

        validarCantidad(cantidad);

        if (item == null) {
            throw new IllegalArgumentException(
                    "El item no puede ser null."
            );
        }

        long total =
                (long) item.getPrecioCompra()
                        * cantidad;

        if (total > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "La cantidad solicitada es demasiado grande."
            );
        }

        return (int) total;
    }


    public void comprar(
            Partida partida,
            Item item,
            int cantidad
    ) throws SQLException,
            AccionNoPermitidaException,
            MonedasInsuficientesException {

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

        /*
         * Solo semillas y alimentos se venden
         * actualmente en la tienda agricola.
         */
        if (!esComprable(item)) {
            throw new AccionNoPermitidaException(
                    "Ese item no esta disponible para comprar."
            );
        }

        int costo =
                costoTotal(
                        item,
                        cantidad
                );

        if (partida.getMonedas() < costo) {
            throw new MonedasInsuficientesException(
                    costo,
                    partida.getMonedas()
            );
        }

        Inventario fila =
                inventarioDao.consultarPorPartidaEItem(
                        partida.getIdPartida(),
                        item.getIdItem()
                );

        int monedasAnteriores =
                partida.getMonedas();

        LocalDateTime fechaGuardadoAnterior =
                partida.getFechaGuardado();

        int cantidadAnterior =
                fila == null
                        ? 0
                        : fila.getCantidad();


        /*
         * Monedas e inventario deben cambiar juntos.
         * Si una operacion falla, ninguna queda guardada.
         */
        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                partida.setMonedas(
                        partida.getMonedas()
                                - costo
                );

                if (fila == null) {

                    if (!inventarioDao.alta(
                            con,
                            new Inventario(
                                    partida.getIdPartida(),
                                    item.getIdItem(),
                                    cantidad
                            )
                    )) {

                        throw new SQLException(
                                "No se pudo guardar el producto comprado."
                        );
                    }

                } else {

                    fila.setCantidad(
                            fila.getCantidad()
                                    + cantidad
                    );

                    if (!inventarioDao.modificar(
                            con,
                            fila
                    )) {

                        throw new SQLException(
                                "No se pudo actualizar el inventario."
                        );
                    }
                }

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar la compra."
                    );
                }

                con.commit();

            } catch (SQLException e) {

                con.rollback();

                /*
                 * El rollback restaura MySQL,
                 * pero no los objetos Java.
                 */
                partida.setMonedas(
                        monedasAnteriores
                );

                partida.setFechaGuardado(
                        fechaGuardadoAnterior
                );

                if (fila != null) {
                    fila.setCantidad(
                            cantidadAnterior
                    );
                }

                throw e;
            }
        }
    }


    private boolean esComprable(
            Item item
    ) {

        if (item == null
                || !item.isActivo()
                || item.getPrecioCompra() <= 0) {

            return false;
        }

        return item.getTipo() == TipoItem.SEMILLA
                || item.getTipo() == TipoItem.ALIMENTO;
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