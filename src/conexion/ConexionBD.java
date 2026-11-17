package conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Se encarga de abrir la conexion con la base de datos MySQL.
 *
 * Los datos de conexion (URL, usuario y contrasenia) no estan escritos en el codigo:
 * se leen del archivo "db.properties", ubicado en la raiz del proyecto.
 * Ese archivo no se sube al repositorio. Para crearlo, copiar
 * "db.properties.example" y completar los datos propios.
 */
public class ConexionBD {

    private static final String ARCHIVO_CONFIG = "db.properties";

    private static Properties config;

    public static Connection obtenerConexion() throws SQLException {

        Properties datos = cargarConfiguracion();

        return DriverManager.getConnection(
                datos.getProperty("db.url"),
                datos.getProperty("db.usuario"),
                datos.getProperty("db.contrasenia")
        );
    }

    // Lee el archivo de configuracion una sola vez y reutiliza los datos en las siguientes conexiones.
    private static synchronized Properties cargarConfiguracion() throws SQLException {

        if (config != null) {
            return config;
        }

        Properties datos = new Properties();

        try (InputStream entrada = new FileInputStream(ARCHIVO_CONFIG)) {
            datos.load(entrada);
        } catch (IOException e) {
            throw new SQLException(
                    "No se encontro el archivo " + ARCHIVO_CONFIG + " en la raiz del proyecto. "
                            + "Copia db.properties.example, renombralo a " + ARCHIVO_CONFIG
                            + " y completa tus datos de MySQL.", e);
        }

        config = datos;
        return config;
    }
}
