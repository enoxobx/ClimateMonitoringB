package LAB_B.Client;

import java.sql.*;  // Importa la libreria per la gestione delle operazioni con il database (SQL)

public class Client {
    private String dbUrl;        // URL del database
    private String dbUsername;   // Nome utente per la connessione al database
    private String dbPassword;   // Password per la connessione al database
    private int maxRetries;      // Numero massimo di tentativi per la connessione al database

    // Costruttore per inizializzare i parametri di connessione al database
    public Client(String dbUrl, String dbUsername, String dbPassword, int maxRetries) {
        this.dbUrl = dbUrl;            // Inizializza l'URL del database
        this.dbUsername = dbUsername;  // Inizializza il nome utente per la connessione
        this.dbPassword = dbPassword;  // Inizializza la password per la connessione
        this.maxRetries = maxRetries;  // Imposta il numero massimo di tentativi di connessione
    }

    // Metodo per connettersi al database e verificare il login dell'utente
    public boolean login(String usernameOrCodiceFiscale, String password) {
        Connection conn = null;      // Variabile per la connessione al database
        PreparedStatement stmt = null; // Variabile per eseguire la query SQL
        ResultSet rs = null;          // Variabile per memorizzare il risultato della query

        try {
            // Tentativi di connessione al database, fino al numero massimo di retries
            for (int attempts = 0; attempts < maxRetries; attempts++) {
                try {
                    // Connessione al database PostgreSQL usando i parametri forniti
                    conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                    System.out.println("Connessione al database riuscita.");  // Conferma della connessione riuscita

                    // Debug: stampa i valori ricevuti
                    System.out.println("Login con username o codice fiscale: " + usernameOrCodiceFiscale);
                    System.out.println("Password: " + password);

                    // Esegui la query di login per verificare se l'utente esiste nel database
                    String sql;
                    // Controlla se è stato passato uno username o un codice fiscale
                    if (!usernameOrCodiceFiscale.isEmpty()) {
                        // Se è presente lo username
                        sql = "SELECT * FROM operatori WHERE username = ? AND password = ?";
                        stmt = conn.prepareStatement(sql);  // Prepara la query
                        stmt.setString(1, usernameOrCodiceFiscale);  // Imposta il parametro della query per lo username
                    } else {
                        // Se è presente il codice fiscale
                        sql = "SELECT * FROM operatori WHERE codice_fiscale = ? AND password = ?";
                        stmt = conn.prepareStatement(sql);  // Prepara la query
                        stmt.setString(1, usernameOrCodiceFiscale);  // Imposta il parametro della query per il codice fiscale
                    }
                    stmt.setString(2, password);  // Imposta il parametro della query per la password
                    rs = stmt.executeQuery();  // Esegui la query e ottieni il risultato

                    // Se il risultato della query contiene un record, significa che il login è riuscito
                    if (rs.next()) {
                        System.out.println("Login riuscito.");  // Messaggio di login riuscito
                        return true;  // Restituisci true se le credenziali sono corrette
                    } else {
                        System.out.println("Login fallito.");  // Messaggio di login fallito
                        return false;  // Restituisci false se le credenziali non sono corrette
                    }
                } catch (SQLException e) {
                    // Se c'è un errore durante la connessione o l'esecuzione della query
                    System.err.println("Errore durante la connessione o la query al database. Tentativo " + (attempts + 1) + " di " + maxRetries + "...");
                    e.printStackTrace();  // Stampa lo stack trace dell'errore per il debug
                    try {
                        Thread.sleep(2000);  // Attendere 2 secondi prima di ritentare la connessione
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();  // Ripristina lo stato di interruzione del thread
                    }
                }
            }
            System.err.println("Impossibile connettersi al database dopo " + maxRetries + " tentativi.");
            return false;  // Se la connessione non è riuscita dopo il numero massimo di tentativi
        } finally {
            // Chiude le risorse di connessione al database per evitare memory leak
            try {
                if (rs != null) rs.close();  // Chiude il ResultSet
                if (stmt != null) stmt.close();  // Chiude il PreparedStatement
                if (conn != null) conn.close();  // Chiude la connessione al database
            } catch (SQLException e) {
                // Se c'è un errore durante la chiusura delle risorse
                System.err.println("Errore nella chiusura delle risorse.");
                e.printStackTrace();
            }
        }
    }
}
