package LAB_B.Database;

import LAB_B.Common.Operatore;

import javax.swing.JOptionPane;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.io.FileInputStream;

public class DatabaseImpl extends UnicastRemoteObject implements Database {
    private Connection conn;
    private final Properties dbConfig;

    // Costruttore: carica la configurazione e stabilisce la connessione
    public DatabaseImpl() throws Exception {
        super();
        dbConfig = new Properties();

        // Caricamento del file di configurazione
        try (FileInputStream fis = new FileInputStream("dbconfig.properties")) {
            dbConfig.load(fis);
            System.out.println("Configurazione del database caricata con successo.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore durante il caricamento della configurazione: " + e.getMessage(), "Errore Configurazione", JOptionPane.ERROR_MESSAGE);
            throw new Exception("Impossibile caricare il file di configurazione.");
        }

        // Tentativo di connessione al database
        try {
            connectToDatabase();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore di connessione al database: " + e.getMessage(), "Errore Connessione", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    // Metodo per stabilire la connessione al database
    private void connectToDatabase() throws SQLException {
        int maxRetries = Integer.parseInt(dbConfig.getProperty("db.maxRetries", "3"));
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }

                // Connessione al database
                conn = DriverManager.getConnection(
                        dbConfig.getProperty("db.url"),
                        dbConfig.getProperty("db.username"),
                        dbConfig.getProperty("db.password")
                );
                System.out.println("Connessione al database stabilita con successo.");
                return;
            } catch (SQLException e) {
                attempts++;
                System.err.println("Tentativo di connessione fallito (" + attempts + "/" + maxRetries + "): " + e.getMessage());

                if (attempts == maxRetries) {
                    JOptionPane.showMessageDialog(null, "Impossibile connettersi al database dopo " + maxRetries + " tentativi.", "Errore Connessione", JOptionPane.ERROR_MESSAGE);
                    throw e;
                }

                // Aspetta prima di ritentare
                try {
                    Thread.sleep(2000); // 2 secondi di attesa
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Metodo per generare l'hash MD5 della password
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    @Override
    public boolean login(String usr, String psw) {
        System.out.println("Tentativo di login con username: " + usr);

        // Query per verificare le credenziali
        String query = "SELECT password FROM operatori WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usr);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password").trim();
                    if (storedPassword.equals(hashPassword(psw.trim()))) {
                        JOptionPane.showMessageDialog(null, "Login effettuato con successo!", "Login", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Login riuscito.");
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Password errata.", "Errore Login", JOptionPane.WARNING_MESSAGE);
                        System.out.println("Password errata.");
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Utente non trovato.", "Errore Login", JOptionPane.WARNING_MESSAGE);
                    System.out.println("Utente non trovato.");
                    return false;
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Errore durante il login: " + e.getMessage(), "Errore Login", JOptionPane.ERROR_MESSAGE);
            System.err.println("Errore durante il login: " + e.getMessage());
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
                    JOptionPane.showMessageDialog(null, "Utente già presente.", "Errore Registrazione", JOptionPane.WARNING_MESSAGE);
                    System.out.println("Utente già presente.");
                    return false;
                }
            }

            String hashedPassword = hashPassword(op.getPassword());
            String insertQuery = "INSERT INTO operatori (cf, name, surname, user_id, email, password) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(insertQuery)) {
                stmtInsert.setString(1, op.getCf());
                stmtInsert.setString(2, op.getName());
                stmtInsert.setString(3, op.getSurname());
                stmtInsert.setString(4, op.getUserId());
                stmtInsert.setString(5, op.getMail());
                stmtInsert.setString(6, hashedPassword);

                int rowsInserted = stmtInsert.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "Registrazione completata con successo!", "Registrazione", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Registrazione completata con successo.");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Errore durante la registrazione.", "Errore Registrazione", JOptionPane.ERROR_MESSAGE);
                    System.err.println("Errore durante la registrazione.");
                    return false;
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Errore durante la registrazione: " + e.getMessage(), "Errore Registrazione", JOptionPane.ERROR_MESSAGE);
            System.err.println("Errore durante la registrazione: " + e.getMessage());
            return false;
        }
    }
}
