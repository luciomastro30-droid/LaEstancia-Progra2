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

/**
 * Gestiona la persistencia del progreso de las misiones de cada partida.
 * Guarda tanto el avance alcanzado como el estado actual de cada misión.
 */
public class ProgresoMisionDao implements Dao<ProgresoMision> {

    private static final String COLUMNAS =
            "id_progreso_mision, id_partida, codigo_mision, progreso, estado";

    private static final String SQL_INSERT =
            "INSERT INTO progreso_mision (id_partida, codigo_mision, progreso, estado) " +
                    "VALUES (?, ?, ?, ?)";

    /*
     * Una vez creado el progreso de una mision no cambio
     * la partida ni el codigo. Solo progreso y estado.
     */
    private static final String SQL_UPDATE =
            "UPDATE progreso_mision SET progreso = ?, estado = ? " +
                    "WHERE id_progreso_mision = ?";

    private static final String SQL_SELECT_ID =
            "SELECT " + COLUMNAS +
                    " FROM progreso_mision WHERE id_progreso_mision = ?";

    private static final String SQL_SELECT =
            "SELECT " + COLUMNAS +
                    " FROM progreso_mision ORDER BY id_partida, codigo_mision";

    private static final String SQL_SELECT_PARTIDA =
            "SELECT " + COLUMNAS +
                    " FROM progreso_mision WHERE id_partida = ? ORDER BY codigo_mision";

    private static final String SQL_SELECT_PARTIDA_CODIGO =
            "SELECT " + COLUMNAS +
                    " FROM progreso_mision WHERE id_partida = ? AND codigo_mision = ?";


    @Override
    public boolean alta(ProgresoMision elemento) throws SQLException {

        /*
         * Esta version abre su propia conexion.
         * La logica SQL real queda en la sobrecarga con Connection.
         */
        try (Connection con = ConexionBD.obtenerConexion()) {
            return alta(con, elemento);
        }
    }


    public boolean alta(Connection con, ProgresoMision elemento)
            throws SQLException {

        /*
         * Esta sobrecarga permite reutilizar una conexion ya abierta,
         * por ejemplo dentro de una transaccion de MisionServicio.
         */
        try (PreparedStatement pd = con.prepareStatement(
                SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {

            pd.setInt(1, elemento.getIdPartida());
            pd.setString(2, elemento.getCodigoMision());
            pd.setInt(3, elemento.getProgreso());

            // Guardo el enum como texto para que coincida con MySQL.
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


    @Override
    public boolean modificar(ProgresoMision elemento) throws SQLException {

        /*
         * Igual que en alta, esta version abre la conexion
         * y delega en la sobrecarga que recibe Connection.
         */
        try (Connection con = ConexionBD.obtenerConexion()) {
            return modificar(con, elemento);
        }
    }


    public boolean modificar(Connection con, ProgresoMision elemento)
            throws SQLException {

        /*
         * Esta version sirve para modificar una mision
         * dentro de una transaccion compartida.
         */
        try (PreparedStatement pd = con.prepareStatement(SQL_UPDATE)) {

            pd.setInt(1, elemento.getProgreso());
            pd.setString(2, elemento.getEstado().name());
            pd.setInt(3, elemento.getIdProgresoMision());

            return pd.executeUpdate() > 0;
        }
    }


    @Override
    public ProgresoMision consultarPorId(int idBuscado)
            throws SQLException {

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


    /**
     * Recupera todos los progresos de misión asociados a una partida.
     */
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
     * Busca el progreso de una misión específica dentro de una partida.
     * Si todavía no existe un registro para esa misión, devuelve null.
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


    // Convierte una fila de progreso_mision en un objeto ProgresoMision.
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