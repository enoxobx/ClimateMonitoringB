package LAB_B.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseProxy implements Database {
    private final DatabaseImpl databaseImpl;

    public DatabaseProxy() throws Exception {
        // Carica le configurazioni da un file o variabile di ambiente
        Properties config = new Properties();
        config.load(getClass().getResourceAsStream("/config.properties")); // File di configurazione

        String dbUrl = config.getProperty("db.url");
        String dbUser = config.getProperty("db.user");
        String dbPassword = config.getProperty("db.password");

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            throw new IllegalArgumentException("Configurazioni database mancanti.");
        }

        // Inizializza il database con le configurazioni lette
        databaseImpl = new DatabaseImpl(dbUrl, dbUser, dbPassword);
    }

    @Override
    public int executeUpdate(String query, Object... params) throws SQLException {
        databaseImpl.executeUpdate(query, params);
        return 0;
    }

    @Override
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        return databaseImpl.executeQuery(query, params);
    }
}
