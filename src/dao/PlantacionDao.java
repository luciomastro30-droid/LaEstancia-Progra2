package dao;

import conexion.ConexionBD;
import modelo.Plantacion;
import modelo.enums.EstadoPlantacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlantacionDao implements DaoConBajaLogica<Plantacion> {

    private static final String COLUMNAS = "id_plantacion, id_partida, id_cultivo, numero_parcela, " +
                    "minuto_siembra_juego, estado, activo";

    private static final String SQL_INSERT = "INSERT INTO plantacion (id_partida, id_cultivo, numero_parcela, " +
                    "minuto_siembra_juego, estado, activo) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_BAJA = "UPDATE plantacion SET activo = FALSE WHERE id_plantacion = ? AND activo = TRUE";

    // Una vez creada la plantacion no cambio la partida, el cultivo, la parcela ni el momento de siembra.
    // Esos datos representan como se origino la plantacion, por eso modificar solo actualiza su estado.
    private static final String SQL_UPDATE = "UPDATE plantacion SET estado = ? WHERE id_plantacion = ? AND activo = TRUE";
    private static final String SQL_SELECT_ID = "SELECT " + COLUMNAS + " FROM plantacion WHERE id_plantacion = ?";
    private static final String SQL_SELECT_ACTIVAS = "SELECT " + COLUMNAS + " FROM plantacion WHERE activo = TRUE ORDER BY id_partida, numero_parcela";
    private static final String SQL_SELECT_PARTIDA = "SELECT " + COLUMNAS + " FROM plantacion " +
                    "WHERE id_partida = ? AND activo = TRUE AND estado <> ? " +
                    "ORDER BY numero_parcela";
    private static final String SQL_SELECT_PARCELA = "SELECT " + COLUMNAS + " FROM plantacion " +
            "WHERE id_partida = ? AND numero_parcela = ? AND activo = TRUE AND estado <> ? " +
            "ORDER BY id_plantacion DESC LIMIT 1";

    /**
     * Doy de alta una nueva plantacion en la base de datos.
     * Guardo la partida a la que pertenece, el cultivo sembrado, la parcela,
     * el minuto del juego en que se sembro y su estado actual.
     * Si MySQL genera el registro correctamente, recupero el ID y se lo asigno
     * al mismo objeto Plantacion que recibi como parametro.
     */
    @Override
    public boolean alta(Plantacion elemento) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pd.setInt(1, elemento.getIdPartida());
            pd.setInt(2, elemento.getIdCultivo());
            pd.setInt(3, elemento.getNumeroParcela());
            pd.setLong(4, elemento.getMinutoSiembraJuego());
            // Guardo el enum como texto para que coincida con el ENUM definido en MySQL.
            pd.setString(5, elemento.getEstado().name());
            pd.setBoolean(6, elemento.isActivo());

            int filasCambiadas = pd.executeUpdate();

            if (filasCambiadas > 0) {
                try (ResultSet rs = pd.getGeneratedKeys()) {
                    if (rs.next()) {
                        elemento.setIdPlantacion(rs.getInt(1));
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
     * Modifico el estado de una plantacion existente y activa.
     * No cambio el cultivo, la partida, la parcela ni el minuto de siembra porque
     * esos datos representan el origen de esa plantacion y no deberian modificarse despues.
     * Por ejemplo, el estado puede pasar de CRECIENDO a LISTA y despues a COSECHADA.
     */
    @Override
    public boolean modificar(Plantacion elemento) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_UPDATE)) {

            pd.setString(1, elemento.getEstado().name());
            pd.setInt(2, elemento.getIdPlantacion());

            return pd.executeUpdate() > 0;
        }
    }


    @Override
    public Plantacion consultarPorId(int idBuscado) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)) {

            pd.setInt(1, idBuscado);

            try (ResultSet rs = pd.executeQuery()) {
                if (rs.next()) {
                    return mapearPlantacion(rs);
                }
            }

            return null;
        }
    }

    /**
     * Recupero todas las plantaciones activas de la base de datos.
     * Las ordeno por partida y numero de parcela para que el resultado sea mas facil de leer.
     * Si no hay plantaciones activas, devuelvo una lista vacia.
     */
    @Override
    public List<Plantacion> listar() throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ACTIVAS);
             ResultSet rs = pd.executeQuery()) {

            List<Plantacion> plantaciones = new ArrayList<>();

            while (rs.next()) {
                plantaciones.add(mapearPlantacion(rs));
            }

            return plantaciones;
        }
    }


    public List<Plantacion> listarPorPartida(int idPartida) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_PARTIDA)) {

            pd.setInt(1, idPartida);
            pd.setString(2, EstadoPlantacion.COSECHADA.name());

            try (ResultSet rs = pd.executeQuery()) {
                List<Plantacion> plantaciones = new ArrayList<>();

                while (rs.next()) {
                    plantaciones.add(mapearPlantacion(rs));
                }

                return plantaciones;
            }
        }
    }

    /**
     * Busco la plantacion activa y no cosechada de una parcela determinada
     * dentro de una partida.
     * Este metodo me va a servir para saber que hay plantado actualmente
     * antes de sembrar, cosechar o mostrar el terreno.
     * Si la parcela no tiene una plantacion activa pendiente,
     * devuelvo null.
     */
    public Plantacion consultarPorParcela(int idPartida, int numeroParcela) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_PARCELA)) {

            pd.setInt(1, idPartida);
            pd.setInt(2, numeroParcela);
            pd.setString(3, EstadoPlantacion.COSECHADA.name());


            try (ResultSet rs = pd.executeQuery()) {
                if (rs.next()) {
                    return mapearPlantacion(rs);
                }
            }

            return null;
        }
    }


    private Plantacion mapearPlantacion(ResultSet rs) throws SQLException {
        return new Plantacion(
                rs.getInt("id_plantacion"),
                rs.getInt("id_partida"),
                rs.getInt("id_cultivo"),
                rs.getInt("numero_parcela"),
                rs.getLong("minuto_siembra_juego"),
                EstadoPlantacion.valueOf(rs.getString("estado")),
                rs.getBoolean("activo")
        );
    }
}