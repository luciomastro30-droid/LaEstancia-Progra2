package dao;

import java.sql.SQLException;

public interface DaoConBajaLogica<T> extends Dao<T> {

    boolean bajaLogica(int idBuscado) throws SQLException;
}