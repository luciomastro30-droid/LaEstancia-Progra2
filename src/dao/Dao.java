package dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

    boolean alta(T elemento) throws SQLException;

    boolean modificar(T elemento) throws SQLException;

    T consultarPorId(int idBuscado) throws SQLException;

    List<T> listar() throws SQLException;
}
