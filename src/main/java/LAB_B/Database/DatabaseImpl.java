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
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/climate_monitoring", // URL del database
                    "postgres", // Nome utente
                    "0000"      // Password
            );
            System.out.println("Connessione al database riuscita.");
        } catch (SQLException e) {
            System.err.println("Errore nella connessione al database: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Impossibile connettersi al database.");
        }
    }

    @Override
    public boolean login(String usr, String psw) {
        System.out.println("Tentativo di login con username: " + usr);
        String query = "SELECT password FROM operatori WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usr); // Imposta il parametro della query con il nome utente fornito

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    System.out.println("Password trovata nel database per l'utente: " + usr);
                    return psw.equals(storedPassword);
                } else {
                    System.out.println("Utente non trovato: " + usr);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la query di login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registrazione(Operatore op) {
        System.out.println("Tentativo di registrazione per utente: " + op.getUserId());

        String checkQuery = "SELECT * FROM operatori WHERE user_id = ?";
        try (PreparedStatement stmtCheck = conn.prepareStatement(checkQuery)) {
            stmtCheck.setString(1, op.getUserId());

            try (ResultSet rsCheck = stmtCheck.executeQuery()) {
                if (rsCheck.next()) {
                    System.out.println("Utente già presente: " + op.getUserId());
                    return false;
                }
            }

            String insertQuery = "INSERT INTO operatori (cf, name, surname, user_id, email, password) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(insertQuery)) {
                stmtInsert.setString(1, op.getCf());
                stmtInsert.setString(2, op.getName());
                stmtInsert.setString(3, op.getSurname());
                stmtInsert.setString(4, op.getUserId());
                stmtInsert.setString(5, op.getMail());
                stmtInsert.setString(6, op.getPassword());

                int rowsInserted = stmtInsert.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Utente registrato con successo: " + op.getUserId());
                    return true;
                } else {
                    System.out.println("Errore durante la registrazione dell'utente.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la registrazione: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
