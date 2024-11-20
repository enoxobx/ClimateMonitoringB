package LAB_B.Database;

import LAB_B.Common.Operatore;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.security.MessageDigest; // Per generare l'hash MD5
import java.security.NoSuchAlgorithmException;

public class DatabaseImpl extends UnicastRemoteObject implements Database {
    private Connection conn;

    // Costruttore della classe che stabilisce la connessione al database
    public DatabaseImpl() throws Exception {
        super();
        try {
            // Carica il driver JDBC per PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Crea la connessione al database PostgreSQL
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/climate_monitoring", // URL del database
                    "postgres",  // Username per il database
                    "0000"       // Password per il database
            );
            System.out.println("Connessione al database riuscita.");
        } catch (SQLException | ClassNotFoundException e) {
            // Gestisce eventuali errori nella connessione al database
            System.err.println("Errore nella connessione al database: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Impossibile connettersi al database.");
        }
    }

    // Metodo per eseguire l'hash della password con MD5
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        // Utilizza il digest MD5 per generare l'hash della password
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(password.getBytes());

        // Converte il byte array in una stringa esadecimale
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    @Override
    public boolean login(String usr, String psw) {
        // Verifica che l'utente sia 'postgres' (è una limitazione di questo esempio)
        if (!usr.equals("postgres")) {
            System.out.println("Login non valido. Usa l'utente 'postgres'.");
            return false;
        }

        System.out.println("Tentativo di login con username: " + usr);
        String query = "SELECT password FROM operatori WHERE user_id = ?"; // Query per ottenere la password dall'utente

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usr);  // Imposta il parametro per la query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password").trim(); // Password memorizzata nel DB
                    System.out.println("Password trovata nel DB.");

                    // Verifica la password memorizzata con l'hash della password fornita
                    if (storedPassword.equals(hashPassword(psw.trim()))) {
                        return true; // Login riuscito
                    } else {
                        System.out.println("Password errata.");
                        return false; // Password errata
                    }
                } else {
                    System.out.println("Utente non trovato: " + usr);
                    return false; // Utente non trovato
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            // Gestisce eventuali errori durante la query di login
            System.err.println("Errore durante la query di login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registrazione(Operatore op) {
        System.out.println("Tentativo di registrazione per utente: " + op.getUserId());

        // Controlla se l'utente esiste già nel database
        String checkQuery = "SELECT * FROM operatori WHERE user_id = ?";
        try (PreparedStatement stmtCheck = conn.prepareStatement(checkQuery)) {
            stmtCheck.setString(1, op.getUserId());

            try (ResultSet rsCheck = stmtCheck.executeQuery()) {
                if (rsCheck.next()) {
                    System.out.println("Utente già presente: " + op.getUserId());
                    return false; // L'utente esiste già
                }
            }

            // Hash della password prima di salvarla nel DB
            String hashedPassword = hashPassword(op.getPassword());

            // Query per inserire un nuovo operatore nel database
            String insertQuery = "INSERT INTO operatori (cf, name, surname, user_id, email, password) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(insertQuery)) {
                // Imposta i parametri per l'inserimento
                stmtInsert.setString(1, op.getCf());
                stmtInsert.setString(2, op.getName());
                stmtInsert.setString(3, op.getSurname());
                stmtInsert.setString(4, op.getUserId());
                stmtInsert.setString(5, op.getMail());
                stmtInsert.setString(6, hashedPassword);

                // Esegui l'inserimento nel database
                int rowsInserted = stmtInsert.executeUpdate();
                return rowsInserted > 0; // Se sono stati inseriti record, ritorna true
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            // Gestisce eventuali errori durante la registrazione
            System.err.println("Errore durante la registrazione: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
