package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Define las operaciones básicas de persistencia que deben implementar
 * los DAO del sistema.
 *
 * @param <T> tipo de entidad administrada.
 */
public interface Dao<T> {

    // Registra un nuevo elemento en la base de datos.
    boolean alta(T elemento) throws SQLException;

    // Actualiza los datos de un elemento existente.
    boolean modificar(T elemento) throws SQLException;

    // Busca un elemento por su identificador.
    T consultarPorId(int idBuscado) throws SQLException;

    // Devuelve todos los elementos disponibles.
    List<T> listar() throws SQLException;
}