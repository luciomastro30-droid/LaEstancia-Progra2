package dao;
import conexion.ConexionBD;
import modelo.Usuario;
import modelo.Jugador;
import modelo.Administrador;
import modelo.enums.Rol;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements DaoConBajaLogica<Usuario> {

    private static final String SQL_INSERT = "INSERT INTO usuario (nombre_usuario, email, contrasenia_hash, rol, activo, fecha_creacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_BAJA = "UPDATE usuario SET activo = FALSE WHERE id_usuario = ? AND activo = TRUE";
    private static final String SQL_UPDATE = "UPDATE usuario SET nombre_usuario = ?, email = ?, contrasenia_hash = ? " +
                    "WHERE id_usuario = ? AND activo = TRUE";
    private static final String SQL_SELECT_ACTIVOS = "SELECT id_usuario, nombre_usuario, email, contrasenia_hash, rol, activo, fecha_creacion " +
                    "FROM usuario WHERE activo = TRUE";
    private static final String SQL_SELECT_ID = "SELECT id_usuario, nombre_usuario, email, contrasenia_hash, rol, activo, fecha_creacion " +
                    "FROM usuario WHERE id_usuario = ?";
    private static final String SQL_SELECT_NOMBRE = "SELECT id_usuario, nombre_usuario, email, contrasenia_hash, rol, activo, fecha_creacion " +
                    "FROM usuario WHERE nombre_usuario = ? AND activo = TRUE";
    private static final String SQL_NOMBRE_REPETIDO = "SELECT nombre_usuario FROM usuario WHERE nombre_usuario = ?";
    private static final String SQL_EMAIL_REPETIDO = "SELECT email FROM usuario WHERE email = ?";

    @Override
    public boolean alta(Usuario elemento) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion()){
             return alta(con,elemento);
        }
    }

    //Metodo dividido para lograr hacer rollback en caso de que falle alguna operacion
    public boolean alta(Connection con, Usuario elemento) throws SQLException {
        try (PreparedStatement pd = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pd.setString(1, elemento.getNombreUsuario());
            pd.setString(2, elemento.getEmail());
            pd.setString(3, elemento.getContraseniaHash());
            pd.setString(4, elemento.getRol().name());
            pd.setBoolean(5, elemento.isActivo());
            pd.setTimestamp(6, Timestamp.valueOf(elemento.getFechaCreacion()));

            int filasCambiadas = pd.executeUpdate();

            if (filasCambiadas > 0) {
                try (ResultSet rs = pd.getGeneratedKeys()) {
                    if (rs.next()) {
                        elemento.setIdUsuario(rs.getInt(1));
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


    @Override
    public boolean modificar(Usuario elemento) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_UPDATE)) {

            pd.setString(1, elemento.getNombreUsuario());
            pd.setString(2, elemento.getEmail());
            pd.setString(3, elemento.getContraseniaHash());
            pd.setInt(4, elemento.getIdUsuario());

            return pd.executeUpdate() > 0;
        }
    }


    @Override
    public Usuario consultarPorId(int idBuscado) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)) {

            pd.setInt(1, idBuscado);

            try (ResultSet rs = pd.executeQuery()) {

                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

            return null;
        }
    }


    @Override
    public List<Usuario> listar() throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ACTIVOS);
             ResultSet rs = pd.executeQuery()) {

            List<Usuario> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }

            return lista;
        }
    }


    public Usuario consultarPorNombreUsuario(String nombre) throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_NOMBRE)) {

            pd.setString(1, nombre);

            try (ResultSet rs = pd.executeQuery()) {

                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

            return null;
        }
    }


    private Usuario mapearUsuario(ResultSet rs) throws SQLException {

        int idUsuario = rs.getInt("id_usuario");
        String nombreUsuario = rs.getString("nombre_usuario");
        String email = rs.getString("email");
        String contraseniaHash = rs.getString("contrasenia_hash");
        Rol rol = Rol.valueOf(rs.getString("rol"));
        boolean activo = rs.getBoolean("activo");

        Timestamp fecha = rs.getTimestamp("fecha_creacion");
        LocalDateTime fechaCreacion = fecha.toLocalDateTime();

        if (rol == Rol.JUGADOR) {

            return new Jugador(
                    idUsuario,
                    nombreUsuario,
                    email,
                    contraseniaHash,
                    activo,
                    fechaCreacion
            );

        } else if (rol == Rol.ADMIN) {

            return new Administrador(
                    idUsuario,
                    nombreUsuario,
                    email,
                    contraseniaHash,
                    activo,
                    fechaCreacion
            );

        }

        throw new SQLException("Rol de usuario no reconocido: " + rol);
    }

    public boolean existeNombre(String nombre)throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_NOMBRE_REPETIDO)) {
            pd.setString(1, nombre);

            try (ResultSet rs = pd.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existeEmail(String email)throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_EMAIL_REPETIDO)) {
            pd.setString(1, email);

            try (ResultSet rs = pd.executeQuery()) {
                return rs.next();
            }
        }
    }


}


