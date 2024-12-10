package LAB_B.Database;

import java.sql.*;

public class QueryExecutorImpl {
    private final Connection conn; // Connessione al database

    // Costruttore: inizializza l'oggetto con una connessione al database
    public QueryExecutorImpl(Connection conn) {
        this.conn = conn; // Memorizza la connessione per l'uso nelle query
    }

    // Metodo per eseguire una query di selezione (SELECT)
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        // Prepara la query SQL
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Imposta i parametri della query (se presenti)
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]); // Imposta ogni parametro nella query
            }
            // Esegui la query e restituisci il risultato
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore durante l'esecuzione della query: " + e.getMessage());
            throw e; // Rilancia l'eccezione per il trattamento in un livello superiore
        }
    }


    // Metodo per eseguire una query di modifica (INSERT, UPDATE, DELETE)
    public int executeUpdate(String query, Object... params) throws SQLException {
        // Prepara la query SQL
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Imposta i parametri della query (se presenti)
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]); // Imposta ogni parametro nella query
            }
            // Esegue la query e restituisce il numero di righe modificate (inserite, aggiornate o eliminate)
            return stmt.executeUpdate();
        }
    }
}
