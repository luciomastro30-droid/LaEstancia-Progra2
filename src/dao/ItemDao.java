package dao;

import conexion.ConexionBD;
import modelo.Item;
import modelo.enums.TipoItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la persistencia de los ítems en la base de datos.
 * Permite realizar altas, modificaciones, consultas, listados y bajas lógicas.
 */
public class ItemDao implements DaoConBajaLogica<Item>{


    private static final String SQL_INSERT = "INSERT INTO item(nombre,descripcion,tipo,precio_compra,precio_venta,activo) " + "VALUES (?,?,?,?,?,?)";
    private static final String SQL_BAJA = "UPDATE item SET activo = FALSE " + "WHERE id_item = ? AND activo = TRUE";
    private static final String SQL_UPDATE = "UPDATE item SET nombre = ?, descripcion = ?, tipo = ?, " + "precio_compra = ?, precio_venta = ? " + "WHERE id_item = ? AND activo = TRUE";
    private static final String SQL_SELECT_ACTIVOS = "SELECT id_item,nombre,descripcion,tipo,precio_compra,precio_venta,activo FROM item WHERE activo = TRUE";
    private static final String SQL_SELECT_ID = "SELECT id_item,nombre,descripcion,tipo,precio_compra,precio_venta,activo FROM item WHERE id_item = ?";


    @Override
    public boolean alta(Item elemento) throws SQLException {
        try(Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)){

            pd.setString(1,elemento.getNombre());
            pd.setString(2,elemento.getDescripcion());
            pd.setString(3,elemento.getTipo().name()); //.name para pasarlo como string
            pd.setInt(4,elemento.getPrecioCompra());
            pd.setInt(5,elemento.getPrecioVenta());

            // Guardo el mismo estado activo que tiene el objeto Java.
            pd.setBoolean(6, elemento.isActivo());

            int filasCambiadas = pd.executeUpdate();

            // Si el INSERT fue exitoso, se recupera el ID generado
            // por la base de datos y se asigna al mismo objeto Item.
            if(filasCambiadas > 0){
                try (ResultSet rs = pd.getGeneratedKeys()){
                    if(rs.next()){
                        int idGenerado = rs.getInt(1);
                        elemento.setIdItem(idGenerado);
                    }
                }
            }

            return filasCambiadas > 0;
        }
    }


    @Override
    public boolean bajaLogica(int id) throws SQLException {
        try(Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_BAJA)){

            pd.setInt(1,id);
            return pd.executeUpdate() > 0;
        }

    }

    @Override
    public boolean modificar(Item elemento) throws SQLException {
        try(Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_UPDATE)){

            pd.setString(1,elemento.getNombre());
            pd.setString(2,elemento.getDescripcion());
            pd.setString(3,elemento.getTipo().name()); //.name para pasarlo como string
            pd.setInt(4,elemento.getPrecioCompra());
            pd.setInt(5,elemento.getPrecioVenta());
            pd.setInt(6, elemento.getIdItem());

            int filasCambiadas = pd.executeUpdate();
            return filasCambiadas > 0;
        }
    }

    @Override
    public Item consultarPorId(int idBuscado) throws SQLException {
        try(Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)){

            pd.setInt(1,idBuscado);

            try(ResultSet rs = pd.executeQuery()){
                if(rs.next()) {
                    Item item = mapearItem(rs);
                    return item;
                }

                return null;
            }
        }

    }

    @Override
    public List<Item> listar() throws SQLException {
        try(Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_SELECT_ACTIVOS);
            ResultSet rs = pd.executeQuery()){

            List<Item> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(mapearItem(rs));
            }

            return lista;
        }
    }

    /*
     * Convierte una fila del ResultSet en un objeto Item.
     * El tipo almacenado como texto se transforma nuevamente en TipoItem.
     */
    private Item mapearItem(ResultSet rs) throws SQLException {

        return new Item(
                rs.getInt("id_item"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                TipoItem.valueOf(rs.getString("tipo")),
                rs.getInt("precio_compra"),
                rs.getInt("precio_venta"),
                rs.getBoolean("activo")
        );
    }

}