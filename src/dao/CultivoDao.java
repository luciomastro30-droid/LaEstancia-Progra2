package dao;

import conexion.ConexionBD;
import modelo.Cultivo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CultivoDao implements DaoConBajaLogica<Cultivo>{


    private static final String SQL_INSERT = "INSERT INTO cultivo (nombre,precio_semilla,precio_venta," +
            "tiempo_crecimiento_min,experiencia_recompensa,id_item_semilla,id_item_cosecha) VALUES (?,?,?,?,?,?,?)";
    private static final String SQL_BAJA = "UPDATE cultivo SET activo = FALSE " +
                    "WHERE id_cultivo = ? AND activo = TRUE";
    private static final String SQL_UPDATE = "UPDATE cultivo SET nombre = ?, precio_semilla = ?, precio_venta = ?, " +
                    "tiempo_crecimiento_min = ?, experiencia_recompensa = ?, " +
                    "id_item_semilla = ?, id_item_cosecha = ? " +
                    "WHERE id_cultivo = ? AND activo = TRUE";
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
            pd.setInt(2,elemento.getPrecioSemilla());
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
                    Cultivo cultivo = mapearCultivo(rs);
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
                lista.add(mapearCultivo(rs));
            }

            return lista;
        }
    }

    private Cultivo mapearCultivo(ResultSet rs) throws SQLException {

        return new Cultivo(
                rs.getInt("id_cultivo"),
                rs.getString("nombre"),
                rs.getInt("precio_semilla"),
                rs.getInt("precio_venta"),
                rs.getInt("tiempo_crecimiento_min"),
                rs.getInt("experiencia_recompensa"),
                rs.getObject("id_item_semilla", Integer.class),
                rs.getObject("id_item_cosecha", Integer.class),
                rs.getBoolean("activo")
        );
    }
}
