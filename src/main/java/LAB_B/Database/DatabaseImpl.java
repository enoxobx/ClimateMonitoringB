package LAB_B.Database;

import LAB_B.Common.Operatore;

import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class DatabaseImpl extends UnicastRemoteObject implements Database {
    private Connection conn; // Variabile per la connessione al database

    // Costruttore - Connessione al database PostgreSQL
    public DatabaseImpl() throws Exception {
        super();
        // Configura la connessione al database PostgreSQL con l'URL, il nome utente e la password
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/climate_monitoring", // URL del database
                "postgres", // Nome utente
                "0000"      // Password
        );
    }

    @Override
    public boolean login(String usr, String psw) {
        System.out.println("Tentativo di login con username: " + usr);
        // Query per selezionare la password memorizzata per l'utente specificato
        String query = "SELECT password FROM operatori WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usr); // Imposta il parametro della query con il nome utente fornito

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Se l'utente è trovato, recupera la password memorizzata nel database
                    String storedPassword = rs.getString("password");

                    // Confronta la password fornita con quella memorizzata nel database
                    return psw.equals(storedPassword); // Restituisce true se le password corrispondono
                } else {
                    // Nessun utente trovato con lo username specificato
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Restituisce false in caso di errore SQL
        }
    }

    @Override
    public boolean registrazione(Operatore op) {
        System.out.println("Tentativo di registrazione per utente: " + op.getUserId());

        // Verifica se l'utente esiste già nel database
        String checkQuery = "SELECT * FROM operatori WHERE user_id = ?";
        try (PreparedStatement stmtCheck = conn.prepareStatement(checkQuery)) {
            stmtCheck.setString(1, op.getUserId()); // Imposta il parametro della query con il nome utente

            try (ResultSet rsCheck = stmtCheck.executeQuery()) {
                if (rsCheck.next()) {
                    // L'utente esiste già nel database
                    System.out.println("Utente già presente: " + op.getUserId());
                    return false; // Se l'utente esiste già, restituisce false
                }
            }

            // Se l'utente non esiste, inserisce i suoi dati nel database
            String insertQuery = "INSERT INTO operatori (cf, name, surname, user_id, email, password) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(insertQuery)) {
                // Imposta i parametri della query per inserire i dati dell'operatore
                stmtInsert.setString(1, op.getCf());
                stmtInsert.setString(2, op.getName());
                stmtInsert.setString(3, op.getSurname());
                stmtInsert.setString(4, op.getUserId());
                stmtInsert.setString(5, op.getMail());
                stmtInsert.setString(6, op.getPassword()); // Memorizza la password in chiaro (considera di cifrarla)

                // Esegui l'inserimento dei dati nel database
                int rowsInserted = stmtInsert.executeUpdate();
                if (rowsInserted > 0) {
                    // Se l'inserimento è riuscito
                    System.out.println("Utente registrato con successo: " + op.getUserId());
                    return true;
                } else {
                    // Se non sono state inserite righe nel database
                    System.out.println("Errore durante la registrazione.");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Restituisce false in caso di errore SQL durante la registrazione
        }
    }
}
