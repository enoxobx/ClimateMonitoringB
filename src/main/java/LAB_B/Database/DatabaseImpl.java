package LAB_B.Database;

import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

// Classe che implementa l'interfaccia Database e estende UnicastRemoteObject per la gestione di RMI (Remote Method Invocation).
public class DatabaseImpl extends UnicastRemoteObject implements Database {

    // Costruttore che non richiede un proxy (necessario per RMI)
    public DatabaseImpl() throws Exception {
        super(); // Chiama il costruttore della classe UnicastRemoteObject per preparare l'oggetto per RMI.
    }

    // Metodo per eseguire una query di selezione (SELECT) su un database
    @Override
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        // Ottiene la connessione al database dal DatabaseManager
        Connection conn = DatabaseManager.getConnection();

        // Verifica se la connessione è valida
        if (conn == null || conn.isClosed()) {
            // Se la connessione non è valida (null o chiusa), lancia un'eccezione SQLException
            throw new SQLException("Connessione al database non valida.");
        }

        // Se la connessione è valida, esegue la query di selezione tramite DatabaseManager
        return DatabaseManager.executeQuery(query, params);
    }

    // Metodo per eseguire una query di modifica (INSERT, UPDATE, DELETE) su un database
    @Override
    public int executeUpdate(String query, Object... params) throws SQLException {
        // Ottiene la connessione al database dal DatabaseManager
        Connection conn = DatabaseManager.getConnection();

        // Verifica se la connessione è valida
        if (conn == null || conn.isClosed()) {
            // Se la connessione non è valida (null o chiusa), lancia un'eccezione SQLException
            throw new SQLException("Connessione al database non valida.");
        }

        // .Se la connessione è valida, esegue l'operazione di aggiornamento tramite DatabaseManager
        return DatabaseManager.executeUpdate(query, params);
    }
}
