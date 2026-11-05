package dao;

import conexion.ConexionBD;
import modelo.Cultivo;
import modelo.Item;
import modelo.enums.TipoItem;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CultivoDao implements Dao<Cultivo>{

    /*id_cultivo INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    precio_semilla INT UNSIGNED NOT NULL,
    precio_venta INT UNSIGNED NOT NULL,
    tiempo_crecimiento_min INT UNSIGNED NOT NULL,
    experiencia_recompensa INT UNSIGNED NOT NULL DEFAULT 0,
    id_item_semilla INT UNSIGNED,
    id_item_cosecha INT UNSIGNED,
    activo BOOLEAN NOT NULL DEFAULT TRUE;*/

    private static final String SQL_INSERT = "INSERT INTO cultivo (nombre,precio_semilla,precio_venta," +
            "tiempo_crecimiento_min,experiencia_recompensa,id_item_semilla,id_item_cosecha) VALUES (?,?,?,?,?,?,?)";
    private static final String SQL_BAJA = "UPDATE cultivo SET activo = FALSE WHERE id_cultivo = ?";
    private static final String SQL_UPDATE = "UPDATE cultivo SET nombre = ?,precio_semilla = ?, precio_venta = ?, " +
            "tiempo_crecimiento_min = ?,experiencia_recompensa = ?, id_item_semilla = ?, id_item_cosecha = ? WHERE id_cultivo = ? ";
    private static final String SQL_SELECT_ACTIVOS = "SELECT id_cultivo,nombre,precio_semilla,precio_venta," +
            "tiempo_crecimiento_min,experiencia_recompensa,id_item_semilla,id_item_cosecha,activo FROM cultivo WHERE activo = TRUE";
    private static final String SQL_SELECT_ID = "SELECT id_cultivo,nombre,precio_semilla,precio_venta," +
            "tiempo_crecimiento_min,experiencia_recompensa,id_item_semilla,id_item_cosecha,activo FROM cultivo " +
            "WHERE id_cultivo = ?";



    @Override
    public boolean alta(Cultivo elemento) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pd.setString(1, elemento.getNombre());
            pd.setInt(2, elemento.getPrecioSemilla());
            pd.setInt(3, elemento.getPrecioVenta());
            pd.setInt(4, elemento.getTiempoCrecimientoMin());
            pd.setInt(5, elemento.getExperienciaRecompensa());
            pd.setObject(6, elemento.getIdItemSemilla(), Types.INTEGER);
            pd.setObject(7, elemento.getIdItemCosecha(), Types.INTEGER);

            int filasCambiadas = pd.executeUpdate();

            if (filasCambiadas > 0) {
                try (ResultSet rs = pd.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        elemento.setId(id);
                    }
                }
            }
            return filasCambiadas > 0;
        }
    }

    @Override
    public boolean bajaLogica(int idBuscado) throws SQLException {

        try(Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_BAJA)){
            pd.setInt(1,idBuscado);

            return pd.executeUpdate() > 0;
        }
    }

    @Override
    public boolean modificar(Cultivo elemento) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_UPDATE)){

            pd.setString(1,elemento.getNombre());
            pd.setInt(2,elemento.getPrecioCompra());
            pd.setInt(3,elemento.getPrecioVenta());
            pd.setInt(4,elemento.getTiempoCrecimientoMin());
            pd.setInt(5,elemento.getExperienciaRecompensa());
            pd.setObject(6,elemento.getIdItemSemilla(), Types.INTEGER);
            pd.setObject(7,elemento.getIdItemCosecha(), Types.INTEGER);
            pd.setInt(8,elemento.getId());

            return pd.executeUpdate() > 0;
        }

    }

    @Override
    public Cultivo consultarPorId(int idBuscado) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)){

            pd.setInt(1,idBuscado);

            try (ResultSet rs = pd.executeQuery()){
                if(rs.next()) {
                    Cultivo cultivo = mapearItem(rs);
                    return cultivo;
                }
            }
            return null;
        }
    }

    @Override
    public List<Cultivo> listar() throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pd = con.prepareStatement(SQL_SELECT_ACTIVOS);
            ResultSet rs = pd.executeQuery()){

            List<Cultivo> lista = new ArrayList<>();

            while (rs.next()){
                lista.add(mapearItem(rs));
            }
            
            return lista;
        }
    }

    private Cultivo mapearItem(ResultSet rs)throws SQLException{

        int id = rs.getInt(1);
        String nombre = rs.getString(2);
        int precioSemilla = rs.getInt(3);
        int precioVenta = rs.getInt(4);
        int tiempoCrecimientoMin = rs.getInt(5);
        int experienciaRecompensa = rs.getInt(6);
        int idItemSemilla = rs.getInt(7);
        int idItemCosecha = rs.getInt(8);
        boolean activo = rs.getBoolean(9);

        return new Cultivo(id, nombre, precioSemilla, precioVenta,tiempoCrecimientoMin,experienciaRecompensa,
                idItemSemilla,idItemCosecha,activo);
    }
}
