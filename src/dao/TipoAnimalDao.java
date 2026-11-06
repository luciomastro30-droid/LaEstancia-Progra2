
package dao;

import conexion.ConexionBD;
import modelo.TipoAnimal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoAnimalDao implements DaoConBajaLogica<TipoAnimal> {

    // Guardo las consultas en constantes para no repetir SQL en cada metodo.
    private static final String COLUMNAS = "id_tipo_animal, nombre, descripcion, precio_compra, precio_venta, " +
                    "id_item_producto, cantidad_producto, tiempo_produccion_min, activo";
    private static final String SQL_INSERT = "INSERT INTO tipo_animal (nombre, descripcion, precio_compra, precio_venta, " +
                    "id_item_producto, cantidad_producto, tiempo_produccion_min, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_BAJA = "UPDATE tipo_animal SET activo = FALSE WHERE id_tipo_animal = ? AND activo = TRUE";
    // No cambio el ID ni el estado activo desde modificar: para desactivar uso bajaLogica.
    private static final String SQL_UPDATE = "UPDATE tipo_animal SET nombre = ?, descripcion = ?, precio_compra = ?, " +
                    "precio_venta = ?, id_item_producto = ?, cantidad_producto = ?, " +
                    "tiempo_produccion_min = ? WHERE id_tipo_animal = ? AND activo = TRUE";
    private static final String SQL_SELECT_ID = "SELECT " + COLUMNAS + " FROM tipo_animal WHERE id_tipo_animal = ?";
    private static final String SQL_SELECT_ACTIVOS = "SELECT " + COLUMNAS + " FROM tipo_animal WHERE activo = TRUE";
    private static final String SQL_SELECT_NOMBRE =
            "SELECT " + COLUMNAS + " FROM tipo_animal WHERE nombre = ? AND activo = TRUE";


    @Override
    public boolean alta(TipoAnimal elemento) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pd.setString(1, elemento.getNombre());
            pd.setString(2, elemento.getDescripcion());
            pd.setInt(3, elemento.getPrecioCompra());
            pd.setInt(4, elemento.getPrecioVenta());

            // El item producido puede ser null cuando este tipo de animal no tiene producto asociado.
            pd.setObject(5, elemento.getIdItemProducto(), Types.INTEGER);

            pd.setInt(6, elemento.getCantidadProducto());
            pd.setInt(7, elemento.getTiempoProduccionMin());
            pd.setBoolean(8, elemento.isActivo());

            int filasCambiadas = pd.executeUpdate();

            if (filasCambiadas > 0) {
                try (ResultSet rs = pd.getGeneratedKeys()) {
                    if (rs.next()) {
                        elemento.setId(rs.getInt(1));
                    }
                }
            }

            return filasCambiadas > 0;
        }
    }


    @Override
    public boolean bajaLogica(int idBuscado) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_BAJA)) {

            pd.setInt(1, idBuscado);
            return pd.executeUpdate() > 0;
        }
    }

    /**
     * Modifico los datos de un tipo de animal que ya existe y esta activo.
     * No permito cambiar el ID ni reactivarlo desde este metodo.
     */
    @Override
    public boolean modificar(TipoAnimal elemento) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_UPDATE)) {

            pd.setString(1, elemento.getNombre());
            pd.setString(2, elemento.getDescripcion());
            pd.setInt(3, elemento.getPrecioCompra());
            pd.setInt(4, elemento.getPrecioVenta());
            pd.setObject(5, elemento.getIdItemProducto(), Types.INTEGER);
            pd.setInt(6, elemento.getCantidadProducto());
            pd.setInt(7, elemento.getTiempoProduccionMin());
            pd.setInt(8, elemento.getId());

            return pd.executeUpdate() > 0;
        }
    }


    @Override
    public TipoAnimal consultarPorId(int idBuscado) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)) {

            pd.setInt(1, idBuscado);

            try (ResultSet rs = pd.executeQuery()) {
                if (rs.next()) {
                    return mapearTipoAnimal(rs);
                }
            }

            return null;
        }
    }


    @Override
    public List<TipoAnimal> listar() throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ACTIVOS);
             ResultSet rs = pd.executeQuery()) {

            List<TipoAnimal> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(mapearTipoAnimal(rs));
            }

            return lista;
        }
    }

    /**
     * Busco un tipo de animal activo por su nombre. Me sirve para localizarlo
     * desde el catalogo sin conocer su ID, por ejemplo buscando "Gallina".
     */
    public TipoAnimal consultarPorNombre(String nombre) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_NOMBRE)) {

            pd.setString(1, nombre);

            try (ResultSet rs = pd.executeQuery()) {
                if (rs.next()) {
                    return mapearTipoAnimal(rs);
                }
            }

            return null;
        }
    }


    private TipoAnimal mapearTipoAnimal(ResultSet rs) throws SQLException {
        return new TipoAnimal(
                rs.getInt("id_tipo_animal"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getInt("precio_compra"),
                rs.getInt("precio_venta"),
                rs.getObject("id_item_producto", Integer.class),
                rs.getInt("cantidad_producto"),
                rs.getInt("tiempo_produccion_min"),
                rs.getBoolean("activo")
        );
    }
}
