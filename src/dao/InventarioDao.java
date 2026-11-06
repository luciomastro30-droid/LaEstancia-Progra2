package dao;

import conexion.ConexionBD;
import modelo.Inventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InventarioDao implements Dao<Inventario> {

    private static final String COLUMNAS = "id_inventario, id_partida, id_item, cantidad";
    private static final String SQL_INSERT = "INSERT INTO inventario (id_partida, id_item, cantidad) VALUES (?, ?, ?)";
     // Una fila de inventario representa la relacion entre una partida y un item.
    // Por eso no cambio id_partida ni id_item: solamente actualizo la cantidad.
    private static final String SQL_UPDATE = "UPDATE inventario SET cantidad = ? WHERE id_inventario = ?";
    private static final String SQL_SELECT_ID = "SELECT " + COLUMNAS + " FROM inventario WHERE id_inventario = ?";
    private static final String SQL_SELECT = "SELECT " + COLUMNAS +
                    " FROM inventario WHERE cantidad > 0 ORDER BY id_partida, id_item";
    private static final String SQL_SELECT_PARTIDA = "SELECT " + COLUMNAS + " FROM inventario " +
                    "WHERE id_partida = ? AND cantidad > 0 ORDER BY id_item";
    private static final String SQL_SELECT_PARTIDA_ITEM = "SELECT " + COLUMNAS + " FROM inventario " +
                    "WHERE id_partida = ? AND id_item = ?";



    @Override
    public boolean alta(Inventario elemento) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(
                     SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            pd.setInt(1, elemento.getIdPartida());
            pd.setInt(2, elemento.getIdItem());
            pd.setInt(3, elemento.getCantidad());

            int filasCambiadas = pd.executeUpdate();

            if (filasCambiadas > 0) {

                try (ResultSet rs = pd.getGeneratedKeys()) {

                    if (rs.next()) {
                        elemento.setIdInventario(rs.getInt(1));
                    }
                }
            }

            return filasCambiadas > 0;
        }
    }


    /**
     * Modifico solamente la cantidad de un registro del inventario.
     * No cambio la partida ni el item porque esos dos datos identifican que recurso
     * representa la fila y la base no permite repetir la misma combinacion.
     */
    @Override
    public boolean modificar(Inventario elemento) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_UPDATE)) {

            pd.setInt(1, elemento.getCantidad());
            pd.setInt(2, elemento.getIdInventario());

            return pd.executeUpdate() > 0;
        }
    }


    @Override
    public Inventario consultarPorId(int idBuscado) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)) {

            pd.setInt(1, idBuscado);

            try (ResultSet rs = pd.executeQuery()) {

                if (rs.next()) {
                    return mapearInventario(rs);
                }
            }

            return null;
        }
    }


    @Override
    public List<Inventario> listar() throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT);
             ResultSet rs = pd.executeQuery()) {

            List<Inventario> inventario = new ArrayList<>();

            while (rs.next()) {
                inventario.add(mapearInventario(rs));
            }

            return inventario;
        }
    }

    /**
     * Recupero todos los items con cantidad mayor a 0 que pertenecen a una partida.
     * Este metodo me va a servir para mostrar el inventario del jugador actual
     * sin traer recursos pertenecientes a otras partidas.
     */
    public List<Inventario> listarPorPartida(int idPartida) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_PARTIDA)) {

            pd.setInt(1, idPartida);

            try (ResultSet rs = pd.executeQuery()) {

                List<Inventario> inventario = new ArrayList<>();

                while (rs.next()) {
                    inventario.add(mapearInventario(rs));
                }

                return inventario;
            }
        }
    }


    /**
     * Busco la fila que relaciona una partida con un item determinado.
     * No filtro por cantidad porque incluso una fila con cantidad 0 me sirve para saber
     * que esa relacion ya existe y asi evitar intentar insertar un registro duplicado.
     * La base tiene UNIQUE(id_partida, id_item), por lo que como maximo puede existir una fila.
     */
    public Inventario consultarPorPartidaEItem(int idPartida, int idItem)
            throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_PARTIDA_ITEM)) {

            pd.setInt(1, idPartida);
            pd.setInt(2, idItem);

            try (ResultSet rs = pd.executeQuery()) {

                if (rs.next()) {
                    return mapearInventario(rs);
                }
            }

            return null;
        }
    }


    private Inventario mapearInventario(ResultSet rs) throws SQLException {

        return new Inventario(
                rs.getInt("id_inventario"),
                rs.getInt("id_partida"),
                rs.getInt("id_item"),
                rs.getInt("cantidad")
        );
    }
}