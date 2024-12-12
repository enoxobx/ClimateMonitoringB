package LAB_B.Database;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
//TODO questa classe si può eliminares
public class DatabaseManager {
    private static Connection connection;
    private static QueryExecutorImpl queryExecutor;

    // Carica la configurazione dal file properties e inizializza la connessione
    static {
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Impossibile trovare il file di configurazione.");
            } else {
                Properties config = new Properties();
                config.load(input);
                String dbUrl = config.getProperty("db.url");
                String dbUsername = config.getProperty("db.username");
                String dbPassword = config.getProperty("db.password");

                // Crea la connessione al database
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                System.out.println("Connessione al database riuscita.");

                // Crea l'istanza di QueryExecutorImpl
                queryExecutor = new QueryExecutorImpl(connection);
            }
        } catch (IOException | SQLException e) {
            System.err.println("Errore durante il caricamento della configurazione o connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo per ottenere la connessione (esistente o nuova)
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Tentativo di connessione al database...");
                try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("config.properties")) {
                    if (input == null) {
                        System.err.println("Impossibile trovare il file di configurazione.");
                    } else {
                        Properties config = new Properties();
                        config.load(input);
                        String dbUrl = config.getProperty("db.url");
                        String dbUsername = config.getProperty("db.username");
                        String dbPassword = config.getProperty("db.password");

                        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                        System.out.println("Connessione al database riuscita!");
                    }
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println("Errore durante la connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }


    // Metodo per eseguire una query di selezione (SELECT)
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        if (queryExecutor != null) {
            return queryExecutor.executeQuery(query, params);
        }
        throw new SQLException("QueryExecutorImpl non è stato inizializzato.");
    }

    // Metodo per eseguire modifiche al database (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String query, Object... params) throws SQLException {
        if (queryExecutor != null) {
            return queryExecutor.executeUpdate(query, params);
        }
        throw new SQLException("QueryExecutorImpl non è stato inizializzato.");
    }

    // Metodo per chiudere la connessione al database
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
