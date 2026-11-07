package dao;

import conexion.ConexionBD;
import modelo.Partida;
import modelo.enums.EstadoPartida;
import modelo.enums.Rareza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartidaDao implements DaoConBajaLogica<Partida> {

    private static final String COLUMNAS = "id_partida, id_usuario, nombre_granja, nivel, experiencia, monedas, " +
                    "minuto_juego_total, parcelas_desbloqueadas, nivel_terreno, " +
                    "tiene_gallinero, tiene_establo, rareza_hacha, rareza_pico, rareza_cania, " +
                    "dia_ultimo_evento, estado, fecha_creacion, fecha_guardado, activo";
    private static final String SQL_INSERT = "INSERT INTO partida (id_usuario, nombre_granja, nivel, experiencia, monedas, " +
                    "minuto_juego_total, parcelas_desbloqueadas, nivel_terreno, " +
                    "tiene_gallinero, tiene_establo, rareza_hacha, rareza_pico, rareza_cania, " +
                    "dia_ultimo_evento, estado, fecha_creacion, fecha_guardado, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_BAJA = "UPDATE partida SET activo = FALSE WHERE id_partida = ? AND activo = TRUE";
    // Actualizo el progreso de la granja, pero no cambio su dueño ni su fecha de creación.
    // Tampoco permito reactivar una partida desde este método.
    private static final String SQL_UPDATE = "UPDATE partida SET nombre_granja = ?, nivel = ?, experiencia = ?, " +
                    "monedas = ?, minuto_juego_total = ?, parcelas_desbloqueadas = ?, " +
                    "nivel_terreno = ?, tiene_gallinero = ?, tiene_establo = ?, " +
                    "rareza_hacha = ?, rareza_pico = ?, rareza_cania = ?, " +
                    "dia_ultimo_evento = ?, estado = ?, fecha_guardado = ? " +
                    "WHERE id_partida = ? AND activo = TRUE";
    private static final String SQL_SELECT_ID = "SELECT " + COLUMNAS + " FROM partida WHERE id_partida = ?";
    private static final String SQL_SELECT_ACTIVAS = "SELECT " + COLUMNAS + " FROM partida WHERE activo = TRUE";
    private static final String SQL_SELECT_USUARIO = "SELECT " + COLUMNAS + " FROM partida WHERE id_usuario = ? AND activo = TRUE";


    @Override
    public boolean alta(Partida elemento) throws SQLException {
        // Con try-with-resources cierro la conexión y el PreparedStatement automáticamente.
        try (Connection con = ConexionBD.obtenerConexion()){
            return alta(con,elemento);
        }
    }

    public boolean alta(Connection con,Partida elemento) throws SQLException {

        try (PreparedStatement pd = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pd.setInt(1, elemento.getIdUsuario());
            pd.setString(2, elemento.getNombreGranja());
            pd.setInt(3, elemento.getNivel());
            pd.setInt(4, elemento.getExperiencia());
            pd.setInt(5, elemento.getMonedas());
            pd.setLong(6, elemento.getMinutoJuegoTotal());
            pd.setInt(7, elemento.getParcelasDesbloqueadas());
            pd.setInt(8, elemento.getNivelTerreno());
            pd.setBoolean(9, elemento.isTieneGallinero());
            pd.setBoolean(10, elemento.isTieneEstablo());
            // Guardo los enums como texto, igual que los valores definidos en MySQL.
            pd.setString(11, elemento.getRarezaHacha().name());
            pd.setString(12, elemento.getRarezaPico().name());
            pd.setString(13, elemento.getRarezaCania().name());
            // Este dato puede ser null si todavía no ocurrió ningún evento.
            pd.setObject(14, elemento.getDiaUltimoEvento(), Types.INTEGER);
            pd.setString(15, elemento.getEstado().name());
            pd.setTimestamp(16, Timestamp.valueOf(elemento.getFechaCreacion()));
            // Si nunca se guardó la partida, dejo la fecha en NULL en la base.
            if (elemento.getFechaGuardado() == null) {
                pd.setNull(17, Types.TIMESTAMP);
            } else {
                pd.setTimestamp(17, Timestamp.valueOf(elemento.getFechaGuardado()));
            }
            pd.setBoolean(18, elemento.isActivo());

            int filasCambiadas = pd.executeUpdate();
            if (filasCambiadas > 0) {
                // MySQL genera el ID: lo recupero para actualizar el objeto recibido.
                try (ResultSet rs = pd.getGeneratedKeys()) {
                    if (rs.next()) {
                        elemento.setIdPartida(rs.getInt(1));
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
     * Actualizo en MySQL el progreso de una partida existente: monedas, nivel,
     * experiencia, tiempo del juego, construcciones, herramientas y estado.
     * Si la actualización sale bien, también guardo la nueva fecha en el objeto Java.
     */
    @Override
    public boolean modificar(Partida elemento) throws SQLException {
        // Uso la misma fecha en MySQL y en el objeto para que no queden diferentes.
        LocalDateTime fechaGuardado = LocalDateTime.now();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_UPDATE)) {

            pd.setString(1, elemento.getNombreGranja());
            pd.setInt(2, elemento.getNivel());
            pd.setInt(3, elemento.getExperiencia());
            pd.setInt(4, elemento.getMonedas());
            pd.setLong(5, elemento.getMinutoJuegoTotal());
            pd.setInt(6, elemento.getParcelasDesbloqueadas());
            pd.setInt(7, elemento.getNivelTerreno());
            pd.setBoolean(8, elemento.isTieneGallinero());
            pd.setBoolean(9, elemento.isTieneEstablo());
            pd.setString(10, elemento.getRarezaHacha().name());
            pd.setString(11, elemento.getRarezaPico().name());
            pd.setString(12, elemento.getRarezaCania().name());
            pd.setObject(13, elemento.getDiaUltimoEvento(), Types.INTEGER);
            pd.setString(14, elemento.getEstado().name());
            pd.setTimestamp(15, Timestamp.valueOf(fechaGuardado));
            pd.setInt(16, elemento.getIdPartida());

            boolean actualizado = pd.executeUpdate() > 0;
            if (actualizado) {
                elemento.setFechaGuardado(fechaGuardado);
            }
            return actualizado;
        }
    }


    @Override
    public Partida consultarPorId(int idBuscado) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)) {
            pd.setInt(1, idBuscado);
            try (ResultSet rs = pd.executeQuery()) {
                if (rs.next()) {
                    return mapearPartida(rs);
                }
            }
            return null;
        }
    }


    @Override
    public List<Partida> listar() throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ACTIVAS);
             ResultSet rs = pd.executeQuery()) {
            List<Partida> partidas = new ArrayList<>();
            while (rs.next()) {
                partidas.add(mapearPartida(rs));
            }
            return partidas;
        }
    }

    /**
     * Busco todas las partidas activas que pertenecen a un usuario.
     * Devuelvo una lista porque la base permite que un usuario tenga más de una partida.
     * Si no tiene ninguna, devuelvo la lista vacía en lugar de null.
     */
    public List<Partida> listarPorUsuario(int idUsuario) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_USUARIO)) {
            pd.setInt(1, idUsuario);
            try (ResultSet rs = pd.executeQuery()) {
                List<Partida> partidas = new ArrayList<>();
                while (rs.next()) {
                    partidas.add(mapearPartida(rs));
                }
                return partidas;
            }
        }
    }

    /**
     * Transformo una fila del ResultSet en un objeto Partida.
     * Convierto los textos de la base a sus enums de Java y los DATETIME a LocalDateTime.
     * También controlo los datos que pueden ser NULL, como la fecha del último guardado
     * y el día del último evento, para no perder esa información.
     */
    private Partida mapearPartida(ResultSet rs) throws SQLException {
        Timestamp guardado = rs.getTimestamp("fecha_guardado");

        return new Partida(
                rs.getInt("id_partida"),
                rs.getInt("id_usuario"),
                rs.getString("nombre_granja"),
                rs.getInt("nivel"),
                rs.getInt("experiencia"),
                rs.getInt("monedas"),
                rs.getLong("minuto_juego_total"),
                rs.getInt("parcelas_desbloqueadas"),
                rs.getInt("nivel_terreno"),
                rs.getBoolean("tiene_gallinero"),
                rs.getBoolean("tiene_establo"),
                Rareza.valueOf(rs.getString("rareza_hacha")),
                Rareza.valueOf(rs.getString("rareza_pico")),
                Rareza.valueOf(rs.getString("rareza_cania")),
                rs.getObject("dia_ultimo_evento", Integer.class),
                EstadoPartida.valueOf(rs.getString("estado")),
                rs.getTimestamp("fecha_creacion").toLocalDateTime(),

                // Uso un operador ternario para comprobar si la fecha de guardado es null.
                // Si es null, mantengo ese valor porque la partida todavía puede no haberse guardado.
                // Si tiene una fecha, la convierto de Timestamp a LocalDateTime para usarla en Java.
                // Esto también evita un NullPointerException al intentar convertir un valor null.
                guardado == null ? null : guardado.toLocalDateTime(),

                rs.getBoolean("activo")
        );
    }
}
