package LAB_B.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Database {
    ResultSet executeQuery(String query, Object... params) throws SQLException;
    int executeUpdate(String query, Object... params) throws SQLException;
}
