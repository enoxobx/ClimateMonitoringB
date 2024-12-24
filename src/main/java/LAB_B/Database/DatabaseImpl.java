package LAB_B.Database;

import LAB_B.Common.*;
import LAB_B.Common.Interface.Coordinate;
import LAB_B.Common.Interface.Operatore;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class DatabaseImpl extends UnicastRemoteObject implements Database {

    // Connessione al database
    private static Connection connection;

    // Oggetto per l'esecuzione delle query
    private QueryExecutorImpl queryExecutorImpl;

    // Blocco statico per inizializzare la connessione al database al momento del caricamento della classe
    static {
        try {
            initializeConnection();
        } catch (SQLException | IOException e) {
            System.err.println("Errore durante l'inizializzazione della connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /// Costruttore per inizializzare la connessione
    public DatabaseImpl() throws IOException, SQLException {
        super();
        if (connection == null || connection.isClosed()) {
            initializeConnection();  // Solo al primo utilizzo
        }
        this.queryExecutorImpl = new QueryExecutorImpl();
    }


    // Metodo per inizializzare la connessione al database
    private static void initializeConnection() throws SQLException, IOException {
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

                    if (connection == null || connection.isClosed()) {
                        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                        connection.setAutoCommit(false); // Disabilita autocommit per le transazioni
                        System.out.println("Connessione al database riuscita!");
                    }
                }
            }
        }
    }

    // Metodo per ottenere la connessione
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection(); // Riavvia la connessione se è chiusa
            }
        } catch (SQLException | IOException e) {
            System.err.println("Errore durante la connessione al database: " + e.getMessage());
        }
        return connection;
    }

    // Metodo per chiudere la connessione (richiamato solo alla fine, quando l'applicazione termina)
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connessione al database chiusa.");
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Implementazione dei metodi dell'interfaccia Database

    @Override
    public ResultSet executeQuery(String query, Object... params) throws RemoteException, SQLException {
        // Implementazione di executeQuery, se necessario
        return null; // Placeholder
    }

    @Override
    public int executeUpdate(String query, Object... params) throws RemoteException, SQLException {
        // Implementazione di executeUpdate, se necessario
        return 0; // Placeholder
    }

    @Override
    public boolean login(String codiceFiscale, String password) throws RemoteException, SQLException {
        // Implementazione di login, se necessario
        return false; // Placeholder
    }

    // Metodo per la registrazione di un operatore
    @Override
    public boolean registrazione(Operatore operatore) throws RemoteException, SQLException {
        try {
            // Usa il metodo salvaOperatore del QueryExecutorImpl per registrare l'operatore nel database
            return queryExecutorImpl.salvaOperatore(operatore);
        } catch (SQLException e) {
            System.err.println("Errore durante la registrazione dell'operatore: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
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
    public List<Coordinate> getCoordinaResultSet(String name) throws RemoteException {

        try {
            if(queryExecutorImpl == null)queryExecutorImpl = new QueryExecutorImpl(connection);
            return queryExecutorImpl.getCoordinate(name);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Coordinate> getCoordinaResultSet() throws RemoteException {
        try {
            return queryExecutorImpl.getCoordinate();
        } catch (SQLException e) {
            System.err.println("Errore durante l'ottenimento delle coordinate: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
