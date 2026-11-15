package servicio;

import dao.InventarioDao;
import dao.ItemDao;
import modelo.Inventario;
import modelo.Item;
import modelo.Partida;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class InventarioServicio {

    private final InventarioDao inventarioDao;
    private final ItemDao itemDao;


    public InventarioServicio() {
        this.inventarioDao = new InventarioDao();
        this.itemDao = new ItemDao();
    }


    public Map<Item, Integer> listarContenido(
            Partida partida
    ) throws SQLException {

        if (partida == null) {
            throw new IllegalArgumentException(
                    "La partida no puede ser null."
            );
        }

        /*
         * LinkedHashMap conserva el orden en que el DAO
         * devuelve los items del inventario.
         */
        Map<Item, Integer> contenido =
                new LinkedHashMap<>();

        for (Inventario fila :
                inventarioDao.listarPorPartida(
                        partida.getIdPartida()
                )) {

            Item item =
                    itemDao.consultarPorId(
                            fila.getIdItem()
                    );

            if (item == null) {
                throw new SQLException(
                        "No existe el item con ID "
                                + fila.getIdItem()
                                + "."
                );
            }

            contenido.put(
                    item,
                    fila.getCantidad()
            );
        }

        return contenido;
    }


    public int cantidadDe(
            Partida partida,
            int idItem
    ) throws SQLException {

        Inventario fila =
                inventarioDao.consultarPorPartidaEItem(
                        partida.getIdPartida(),
                        idItem
                );

        return fila == null
                ? 0
                : fila.getCantidad();
    }
}