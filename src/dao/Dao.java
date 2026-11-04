package dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao <T>{

    public boolean alta(T elemento) throws SQLException;
    public boolean bajaLogica(int idBuscado) throws SQLException;
    public boolean modificar(T elemento) throws SQLException;
    public T consultarPorId(int id) throws SQLException;
    public List<T> listar() throws SQLException;
}
