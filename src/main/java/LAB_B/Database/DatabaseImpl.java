package LAB_B.Database;

import LAB_B.Common.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Logger;

// Classe che implementa l'interfaccia Database e estende UnicastRemoteObject per la gestione di RMI (Remote Method Invocation).
public class DatabaseImpl extends UnicastRemoteObject implements Database {
    private static Connection connection;
    private static final Logger logger = Logger.getLogger(DatabaseImpl.class.getName());
    private static QueryExecutorImpl queryExecutorImpl;

    static {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Tentativo di connessione al database...");
                try (InputStream input = DatabaseImpl.class.getClassLoader()
                        .getResourceAsStream("config.properties")) {
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

                        queryExecutorImpl = new QueryExecutorImpl(connection);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println("Errore durante la connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Costruttore che non richiede un proxy (necessario per RMI)
    public DatabaseImpl() throws RemoteException {
        super(); // Chiama il costruttore della classe UnicastRemoteObject per preparare
                 
    }

    // Metodo per eseguire una query di selezione (SELECT) su un database

    public ResultSet executeQuery(String query, Object... params) throws RemoteException {
        try {
            if (queryExecutorImpl != null) {
                return queryExecutorImpl.executeQuery(query, params);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Errore eseguzione query: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("QueryExecutorImpl non è stato inizializzato.");

        }
    }

    // Metodo per eseguire una query di modifica (INSERT, UPDATE, DELETE) su un
    // database

    public int executeUpdate(String query, Object... params) throws RemoteException {

        try {
            if (queryExecutorImpl != null) {
                return queryExecutorImpl.executeUpdate(query, params);
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Errore eseguzione query: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("QueryExecutorImpl non è stato inizializzato.");

        }

    }

    // Metodo per ottenere la connessione (esistente o nuova)
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Tentativo di connessione al database...");
                try (InputStream input = DatabaseImpl.class.getClassLoader().getResourceAsStream("config.properties")) {
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

    @Override
    public boolean registrazione(Operatore op) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrazione'");
    }

    @Override
    public boolean login(String cf, String psw) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    private String getDatabaseHost() {
        // TODO
        String host = "localhost";

        return host;
    }


    //prendere da   ESEMPIO
    @Override
    public List<Coordinate> getCoordinaResultSet(double latitude, double longitude, double tollerance) throws RemoteException {

        try {
            if(queryExecutorImpl == null)queryExecutorImpl = new QueryExecutorImpl(connection);
            return queryExecutorImpl.getCoordinate(latitude,longitude,tollerance);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Coordinate> getCoordinaResultSet() throws RemoteException {
        
        try {
            if(queryExecutorImpl == null)queryExecutorImpl = new QueryExecutorImpl(connection);
            return queryExecutorImpl.getCoordinate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
