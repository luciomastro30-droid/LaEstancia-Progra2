package dao;

import conexion.ConexionBD;
import modelo.Animal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la persistencia de los animales de cada partida.
 * Permite registrar, modificar, consultar, listar y realizar bajas lógicas.
 */
public class AnimalDao implements DaoConBajaLogica<Animal> {

    private static final String COLUMNAS = "id_animal, id_partida, id_tipo_animal, nombre, hambre, " +
            "minuto_ultima_alimentacion, minuto_ultima_produccion, " + "fecha_adquisicion, activo";
    private static final String SQL_INSERT = "INSERT INTO animal (id_partida, id_tipo_animal, nombre, hambre, " +
            "minuto_ultima_alimentacion, minuto_ultima_produccion, fecha_adquisicion, activo) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_BAJA = "UPDATE animal SET activo = FALSE " +
            "WHERE id_animal = ? AND activo = TRUE";
    /*
     * Una vez comprado el animal no cambio su partida,
     * tipo ni fecha de adquisicion.
     */
    private static final String SQL_UPDATE = "UPDATE animal SET nombre = ?, hambre = ?, " +
            "minuto_ultima_alimentacion = ?, minuto_ultima_produccion = ? " + "WHERE id_animal = ? AND activo = TRUE";
    private static final String SQL_SELECT_ID = "SELECT " + COLUMNAS + " FROM animal WHERE id_animal = ?";
    private static final String SQL_SELECT_ACTIVOS = "SELECT " + COLUMNAS +
            " FROM animal WHERE activo = TRUE " + "ORDER BY id_partida, id_animal";
    private static final String SQL_SELECT_PARTIDA = "SELECT " + COLUMNAS +
            " FROM animal WHERE id_partida = ? AND activo = TRUE " + "ORDER BY id_animal";


    @Override
    public boolean alta(Animal elemento) throws SQLException {

        /*
         * Esta version abre su propia conexion.
         * El SQL real queda en la sobrecarga con Connection.
         */
        try (Connection con = ConexionBD.obtenerConexion()) {
            return alta(con, elemento);
        }
    }


    public boolean alta(Connection con, Animal elemento)
            throws SQLException {

        /*
         * Esta version permite usar el alta dentro
         * de una transaccion manejada por un servicio.
         */
        try (PreparedStatement pd = con.prepareStatement(
                SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {

            pd.setInt(1, elemento.getIdPartida());
            pd.setInt(2, elemento.getIdTipoAnimal());
            pd.setString(3, elemento.getNombre());
            pd.setInt(4, elemento.getHambre());

            // Estos valores pueden ser null tanto en Java como en MySQL.
            pd.setObject(
                    5,
                    elemento.getMinutoUltimaAlimentacion(),
                    Types.BIGINT
            );

            pd.setObject(
                    6,
                    elemento.getMinutoUltimaProduccion(),
                    Types.BIGINT
            );

            pd.setTimestamp(
                    7,
                    Timestamp.valueOf(elemento.getFechaAdquisicion())
            );

            pd.setBoolean(8, elemento.isActivo());

            int filasCambiadas = pd.executeUpdate();

            if (filasCambiadas > 0) {

                try (ResultSet rs = pd.getGeneratedKeys()) {

                    if (rs.next()) {
                        elemento.setIdAnimal(rs.getInt(1));
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
    public boolean modificar(Animal elemento) throws SQLException {

        /*
         * Esta version abre la conexion y delega
         * en la sobrecarga transaccional.
         */
        try (Connection con = ConexionBD.obtenerConexion()) {
            return modificar(con, elemento);
        }
    }


    public boolean modificar(Connection con, Animal elemento)
            throws SQLException {

        /*
         * Permite guardar cambios del animal usando
         * la misma transaccion que otros DAOs.
         */
        try (PreparedStatement pd = con.prepareStatement(SQL_UPDATE)) {

            pd.setString(1, elemento.getNombre());
            pd.setInt(2, elemento.getHambre());

            pd.setObject(
                    3,
                    elemento.getMinutoUltimaAlimentacion(),
                    Types.BIGINT
            );

            pd.setObject(
                    4,
                    elemento.getMinutoUltimaProduccion(),
                    Types.BIGINT
            );

            pd.setInt(5, elemento.getIdAnimal());

            return pd.executeUpdate() > 0;
        }
    }


    @Override
    public Animal consultarPorId(int idBuscado)
            throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd = con.prepareStatement(SQL_SELECT_ID)) {

            pd.setInt(1, idBuscado);

            try (ResultSet rs = pd.executeQuery()) {

                if (rs.next()) {
                    return mapearAnimal(rs);
                }
            }

            return null;
        }
    }


    @Override
    public List<Animal> listar() throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd =
                     con.prepareStatement(SQL_SELECT_ACTIVOS);
             ResultSet rs = pd.executeQuery()) {

            List<Animal> animales = new ArrayList<>();

            while (rs.next()) {
                animales.add(mapearAnimal(rs));
            }

            return animales;
        }
    }


    /**
     * Recupera todos los animales activos que pertenecen a una partida.
     */
    public List<Animal> listarPorPartida(int idPartida)
            throws SQLException {

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pd =
                     con.prepareStatement(SQL_SELECT_PARTIDA)) {

            pd.setInt(1, idPartida);

            try (ResultSet rs = pd.executeQuery()) {

                List<Animal> animales = new ArrayList<>();

                while (rs.next()) {
                    animales.add(mapearAnimal(rs));
                }

                return animales;
            }
        }
    }


    // Convierte una fila de la tabla animal en un objeto Animal.
    private Animal mapearAnimal(ResultSet rs)
            throws SQLException {

        return new Animal(
                rs.getInt("id_animal"),
                rs.getInt("id_partida"),
                rs.getInt("id_tipo_animal"),
                rs.getString("nombre"),
                rs.getInt("hambre"),
                rs.getObject(
                        "minuto_ultima_alimentacion",
                        Long.class
                ),
                rs.getObject(
                        "minuto_ultima_produccion",
                        Long.class
                ),
                rs.getTimestamp(
                        "fecha_adquisicion"
                ).toLocalDateTime(),
                rs.getBoolean("activo")
        );
    }
}