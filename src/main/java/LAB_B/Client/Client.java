package LAB_B.Client;

import java.sql.*;
import LAB_B.Database.DatabaseManager;

public class Client {
    private final int maxRetries;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    // Costruttore per inizializzare i parametri di connessione al database e il numero di tentativi
    public Client(String dbUrl, String dbUsername, String dbPassword, int maxRetries) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.maxRetries = maxRetries;
    }

    // Metodo per connettersi al database e verificare il login dell'utente
    public boolean login(String usernameOrCodiceFiscale, String password) {
        Connection conn = null;        // Variabile per la connessione al database
        PreparedStatement stmt = null; // Variabile per eseguire la query SQL
        ResultSet rs = null;           // Variabile per memorizzare il risultato della query

        try {
            // Ottieni la connessione dal DatabaseManager
            conn = DatabaseManager.getConnection();  // Ottieni la connessione
            if (conn == null || conn.isClosed()) {
                System.err.println("Impossibile ottenere una connessione valida al database.");
                return false;  // Restituisci false se la connessione non è riuscita o è stata chiusa
            }

            // Debug: stampa i valori ricevuti (attenzione a non stampare la password in chiaro)
            System.out.println("Tentativo di login con username o codice fiscale: " + usernameOrCodiceFiscale);
            System.out.println("Password: " + (password != null ? "******" : "null"));

            // Prepara la query per il login basata su username o codice fiscale
            String sql = "SELECT * FROM operatori WHERE (username = ? OR codice_fiscale = ?) AND password = ?";
            stmt = conn.prepareStatement(sql);  // Prepara la query

            // Imposta i parametri della query
            stmt.setString(1, usernameOrCodiceFiscale);  // Imposta il parametro per lo username o codice fiscale
            stmt.setString(2, usernameOrCodiceFiscale);  // Imposta il parametro per lo username o codice fiscale
            stmt.setString(3, password);  // Imposta il parametro per la password

            // Esegui la query e ottieni il risultato
            rs = stmt.executeQuery();

            // Se il risultato della query contiene un record, significa che il login è riuscito
            if (rs.next()) {
                System.out.println("Login riuscito.");  // Messaggio di login riuscito
                return true;  // Restituisci true se le credenziali sono corrette
            } else {
                System.out.println("Login fallito. Nessun operatore trovato.");  // Messaggio di login fallito
                return false;  // Restituisci false se le credenziali non sono corrette
            }
        } catch (SQLException e) {
            // Gestione degli errori in caso di problemi con la connessione o la query
            System.err.println("Errore durante il login: " + e.getMessage());
            e.printStackTrace();
            return false;  // Restituisci false se c'è stato un errore
        } finally {
            // Chiudi le risorse in modo sicuro
            closeResources(rs, stmt, conn);  // Chiudi ResultSet, PreparedStatement e Connection
        }
    }

    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("Errore nella chiusura delle risorse: " + e.getMessage());
        }
    }
}
