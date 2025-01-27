package LAB_B.Database;

import LAB_B.Common.Config;
import LAB_B.Common.Interface.*;

import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.List;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.io.IOException;

public class DatabaseImpl extends UnicastRemoteObject implements Database {

    // Connessione al database
    public static Connection connection;

    // Oggetto per l'esecuzione delle query
    private QueryExecutorImpl queryExecutorImpl;

    // Blocco statico per inizializzare la connessione al database al momento del
    // caricamento della classe
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
            initializeConnection(); // Solo al primo utilizzo
        }
        this.queryExecutorImpl = new QueryExecutorImpl();
    }

    // Metodo per inizializzare la connessione al database
    private static void initializeConnection() throws SQLException, IOException {
        if (connection == null || connection.isClosed()) {
            System.out.println("Tentativo di connessione al database...");

            Config config = new Config();

            String dbUrl = config.getUrlDb();
            String dbUsername = config.getDbUsername();
            String dbPassword = config.getDbPassword();

            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                connection.setAutoCommit(false); // Disabilita autocommit per le transazioni
                System.out.println("Connessione al database riuscita!");
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

    // Metodo per chiudere la connessione (richiamato solo alla fine, quando
    // l'applicazione termina)
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

    @Override
    public boolean login(String codiceFiscale, String password) throws RemoteException, SQLException {
        try {
            if (queryExecutorImpl == null)
                queryExecutorImpl = new QueryExecutorImpl();
            return queryExecutorImpl.login(codiceFiscale, password);
        } catch (Exception e) {
            // Gestione di altre eccezioni generiche
            e.printStackTrace();
            return false;
        }
    }

    // Metodo per la registrazione di un operatore
    @Override
    public boolean registrazione(Operatore operatore) throws RemoteException {
        try {
            // Usa il metodo salvaOperatore del QueryExecutorImpl per registrare l'operatore
            // nel database
            return queryExecutorImpl.salvaOperatore(operatore);
        } catch (SQLException e) {
            System.err.println("Errore durante la registrazione dell'operatore: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Coordinate> getCoordinaResultSet(double latitude, double longitude, double tollerance)
            throws RemoteException {

        try {
            if (queryExecutorImpl == null)
                queryExecutorImpl = new QueryExecutorImpl();
            return queryExecutorImpl.getCoordinate(latitude, longitude, tollerance);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<String> getCentriPerOperatore(String username) throws RemoteException {
        if (queryExecutorImpl == null)
            queryExecutorImpl = new QueryExecutorImpl();
        return queryExecutorImpl.getCentriPerOperatore(username);
    }

    @Override
    public boolean salvaCentroMonitoraggio(String nomeCentro, String descrizione, String username) throws Exception {
        if (queryExecutorImpl == null)
            queryExecutorImpl = new QueryExecutorImpl();
        return queryExecutorImpl.salvaCentroMonitoraggio(nomeCentro, descrizione, username);
    }

    @Override
    public boolean salvaDatiClimatici(String[] parametri, ArrayList<String> valori, ArrayList<String> commenti, ArrayList<Integer> punteggi, String username, long timestamp, Coordinate citta, String centro) throws Exception {
        if (queryExecutorImpl == null) {
            queryExecutorImpl = new QueryExecutorImpl();
        }
        return queryExecutorImpl.salvaDatiClimatici(parametri, valori, commenti, punteggi, username, timestamp,citta,centro);
    }




    @Override
    public List<Coordinate> getCoordinaResultSet(String name) throws RemoteException {

        try {
            if (queryExecutorImpl == null)
                queryExecutorImpl = new QueryExecutorImpl();
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

    @Override
    public DefaultCategoryDataset getParametri(Coordinate city, TipiPlot type) throws RemoteException{
            return queryExecutorImpl.getParametri(city,type);
            
    }

}
