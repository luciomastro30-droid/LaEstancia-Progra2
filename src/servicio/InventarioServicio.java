package servicio;

import dao.InventarioDao;
import dao.ItemDao;
import modelo.Inventario;
import modelo.Item;
import modelo.Partida;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gestiona la consulta del inventario de una partida.
 * Relaciona cada registro de inventario con su Item correspondiente
 * para devolver la información lista para utilizar en el juego.
 */
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
}