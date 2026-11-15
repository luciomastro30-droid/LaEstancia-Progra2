package dao;

import java.sql.SQLException;

/**
 * Extiende las operaciones básicas de un DAO para entidades
 * que utilizan baja lógica en lugar de eliminar registros físicamente.
 *
 * @param <T> tipo de entidad administrada.
 */
public interface DaoConBajaLogica<T> extends Dao<T> {

    // Desactiva un registro sin eliminarlo de la base de datos.
    boolean bajaLogica(int idBuscado) throws SQLException;
}