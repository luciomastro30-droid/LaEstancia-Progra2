package dao;

import conexion.ConexionBD;
import modelo.ProgresoMision;
import modelo.enums.EstadoMision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProgresoMisionDao implements Dao<ProgresoMision> {

    private static final String COLUMNAS = "id_progreso_mision, id_partida, codigo_mision, progreso, estado";
    private static final String SQL_INSERT = "INSERT INTO progreso_mision (id_partida, codigo_mision, progreso, estado) " +
                    "VALUES (?, ?, ?, ?)";
    // Una vez creado el progreso de una mision no cambio la partida ni el codigo.
    // Solamente actualizo cuanto avanzo el jugador y el estado de la mision.
    private static final String SQL_UPDATE = "UPDATE progreso_mision SET progreso = ?, estado = ? " +
                    "WHERE id_progreso_mision = ?";
    private static final String SQL_SELECT_ID = "SELECT " + COLUMNAS +
                    " FROM progreso_mision WHERE id_progreso_mision = ?";
    private static final String SQL_SELECT = "SELECT " + COLUMNAS +
                    " FROM progreso_mision ORDER BY id_partida, codigo_mision";
    private static final String SQL_SELECT_PARTIDA = "SELECT " + COLUMNAS +
                    " FROM progreso_mision WHERE id_partida = ? ORDER BY codigo_mision";
    private static final String SQL_SELECT_PARTIDA_CODIGO = "SELECT " + COLUMNAS +
                    " FROM progreso_mision WHERE id_partida = ? AND codigo_mision = ?";


    @Override
    public boolean alta(ProgresoMision elemento) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(
                     SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            pd.setInt(1, elemento.getIdPartida());
            pd.setString(2, elemento.getCodigoMision());
            pd.setInt(3, elemento.getProgreso());

            // Guardo el enum como texto para que coincida con el ENUM de MySQL.
            pd.setString(4, elemento.getEstado().name());

            int filasCambiadas = pd.executeUpdate();

            if (filasCambiadas > 0) {

                try (ResultSet rs = pd.getGeneratedKeys()) {

                    if (rs.next()) {
                        elemento.setIdProgresoMision(rs.getInt(1));
                    }
                }
            }

            return filasCambiadas > 0;
        }
    }


    /**
     * Modifico el progreso y el estado de una mision existente.
     * No cambio la partida ni el codigo de mision porque esos datos identifican
     * a que mision y a que partida pertenece este registro.
     *
     * Por ejemplo, la mision puede avanzar en progreso y pasar de ACTIVA
     * a COMPLETADA y finalmente a RECLAMADA.
     */
    @Override
    public boolean modificar(ProgresoMision elemento) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_UPDATE)) {

            pd.setInt(1, elemento.getProgreso());
            pd.setString(2, elemento.getEstado().name());
            pd.setInt(3, elemento.getIdProgresoMision());

            return pd.executeUpdate() > 0;
        }
    }


    @Override
    public ProgresoMision consultarPorId(int idBuscado) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)) {

            pd.setInt(1, idBuscado);

            try (ResultSet rs = pd.executeQuery()) {

                if (rs.next()) {
                    return mapearProgresoMision(rs);
                }
            }

            return null;
        }
    }


    /**
     * Recupero todos los registros de progreso de misiones.
     * Los ordeno por partida y codigo de mision para que sea mas facil
     * visualizar a que jugador y mision corresponde cada registro.
     * Si no hay ninguno, devuelvo una lista vacia.
     */
    @Override
    public List<ProgresoMision> listar() throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT);
             ResultSet rs = pd.executeQuery()) {

            List<ProgresoMision> progresos = new ArrayList<>();

            while (rs.next()) {
                progresos.add(mapearProgresoMision(rs));
            }

            return progresos;
        }
    }


    public List<ProgresoMision> listarPorPartida(int idPartida)
            throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_PARTIDA)) {

            pd.setInt(1, idPartida);

            try (ResultSet rs = pd.executeQuery()) {

                List<ProgresoMision> progresos = new ArrayList<>();

                while (rs.next()) {
                    progresos.add(mapearProgresoMision(rs));
                }

                return progresos;
            }
        }
    }


    /**
     * Busco una mision especifica dentro de una partida usando su codigo.
     * La base tiene UNIQUE(id_partida, codigo_mision), por lo que como maximo
     * puede existir un registro para esa combinacion.
     *
     * Este metodo me va a servir para comprobar si una mision ya fue iniciada,
     * completada o reclamada antes de crear un nuevo progreso.
     */
    public ProgresoMision consultarPorPartidaYCodigo(
            int idPartida,
            String codigoMision
    ) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd =
                     con.prepareStatement(SQL_SELECT_PARTIDA_CODIGO)) {

            pd.setInt(1, idPartida);
            pd.setString(2, codigoMision);

            try (ResultSet rs = pd.executeQuery()) {

                if (rs.next()) {
                    return mapearProgresoMision(rs);
                }
            }

            return null;
        }
    }


    private ProgresoMision mapearProgresoMision(ResultSet rs)
            throws SQLException {

        return new ProgresoMision(
                rs.getInt("id_progreso_mision"),
                rs.getInt("id_partida"),
                rs.getString("codigo_mision"),
                rs.getInt("progreso"),
                EstadoMision.valueOf(rs.getString("estado"))
        );
    }
}