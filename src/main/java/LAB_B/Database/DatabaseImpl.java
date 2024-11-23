package LAB_B.Database;

import LAB_B.Common.Operatore;
import javax.swing.*;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.logging.Logger;

public class DatabaseImpl extends UnicastRemoteObject implements Database {

    private Connection conn;
    private static final Logger logger = Logger.getLogger(DatabaseImpl.class.getName());

    public DatabaseImpl() throws Exception {
        super();
        connectToDatabase();
    }

    // Metodo per connettersi al database
    private void connectToDatabase() throws SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/climate_monitoring";
        String dbUsername = "postgres";
        String dbPassword = "0000";
        conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        logger.info("Connessione al database stabilita.");
    }

    @Override
    public boolean login(String usernameOrCodiceFiscale, String psw) {
        try {
            // Log per vedere quale parametro è stato immesso
            logger.info("Tentativo di login con username o codice fiscale: " + usernameOrCodiceFiscale);

            // Verifica se è un codice fiscale (16 caratteri alfanumerici)
            if (isCodiceFiscale(usernameOrCodiceFiscale)) {
                logger.info("Login con codice fiscale");
                return loginWithCodiceFiscale(usernameOrCodiceFiscale, psw);
            } else {
                logger.info("Login con username");
                return loginWithUsername(usernameOrCodiceFiscale, psw);
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il login: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private boolean isCodiceFiscale(String input) {
        // Verifica che sia un codice fiscale valido (16 caratteri alfanumerici)
        return input.length() == 16 && input.matches("[a-zA-Z0-9]+");
    }

    // Login con username
    private boolean loginWithUsername(String username, String psw) throws SQLException {
        // Log per vedere il valore dell'username inserito
        logger.info("Tentativo di login con Username o Codice Fiscale: " + username.trim());

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT password FROM operatori WHERE username = ? OR codice_fiscale = ?")) {
            stmt.setString(1, username.trim());  // Usa trim per evitare problemi di spazi
            stmt.setString(2, username.trim());  // Usa trim anche per il codice fiscale
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password").trim();  // Rimuovi eventuali spazi extra dalla password
                logger.info("Password trovata nel database, ma non verrà stampata per sicurezza.");
                boolean passwordMatch = storedPassword.equals(psw.trim());  // Usa trim anche per la password
                logger.info("Password corrisponde: " + passwordMatch);
                return passwordMatch;
            } else {
                logger.warning("Nessun operatore trovato con l'username o codice fiscale: " + username.trim());
            }
        } catch (SQLException e) {
            logger.severe("Errore nella query per il login con username: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Login con codice fiscale
    private boolean loginWithCodiceFiscale(String codiceFiscale, String psw) throws SQLException {
        // Log per vedere il valore del codice fiscale inserito
        logger.info("Tentativo di login con Codice Fiscale: " + codiceFiscale.trim());

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT password FROM operatori WHERE LOWER(codice_fiscale) = LOWER(?)")) {
            stmt.setString(1, codiceFiscale.trim());  // Usa trim per rimuovere spazi extra
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password").trim();  // Rimuovi eventuali spazi extra dalla password
                logger.info("Password trovata nel database, ma non verrà stampata per sicurezza.");
                boolean passwordMatch = storedPassword.equals(psw.trim());  // Usa trim anche per la password
                logger.info("Password corrisponde: " + passwordMatch);
                return passwordMatch;
            } else {
                logger.warning("Nessun operatore trovato con il codice fiscale: " + codiceFiscale.trim());
            }
        } catch (SQLException e) {
            logger.severe("Errore nella query per il login con codice fiscale: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean registrazione(Operatore op) {
        try (PreparedStatement stmtCheck = conn.prepareStatement(
                "SELECT * FROM operatori WHERE codice_fiscale = ? OR username = ?")) {
            stmtCheck.setString(1, op.getCodiceFiscale());
            stmtCheck.setString(2, op.getUsername());
            ResultSet rs = stmtCheck.executeQuery();

            // Se l'operatore esiste già con lo stesso codice fiscale o username, la registrazione fallisce
            if (rs.next()) {
                logger.warning("Operatore già esistente con codice fiscale o username: " + op.getCodiceFiscale());
                return false;
            }

            // Se non esiste, inserisce i dati dell'operatore nel database
            try (PreparedStatement stmtInsert = conn.prepareStatement(
                    "INSERT INTO operatori (codice_fiscale, name, surname, email, username, password) VALUES (?, ?, ?, ?, ?, ?)")) {
                stmtInsert.setString(1, op.getCodiceFiscale());
                stmtInsert.setString(2, op.getName());
                stmtInsert.setString(3, op.getSurname());
                stmtInsert.setString(4, op.getEmail());
                stmtInsert.setString(5, op.getUsername());
                stmtInsert.setString(6, op.getPassword());

                boolean insertSuccess = stmtInsert.executeUpdate() > 0;
                if (insertSuccess) {
                    logger.info("Operatore registrato con successo: " + op.getUsername());
                } else {
                    logger.severe("Errore durante la registrazione dell'operatore: " + op.getUsername());
                }
                return insertSuccess;
            }
        } catch (SQLException e) {
            logger.severe("Errore durante la registrazione: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
