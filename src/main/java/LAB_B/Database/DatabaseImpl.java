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
        // Decidi automaticamente l'host da usare
        String host = getDatabaseHost(); // usa il metodo per determinare l'host
        String dbUrl = "jdbc:postgresql://" + host + ":5432/climate_monitoring";
        String dbUsername = "postgres";
        String dbPassword = "0000";

        try {
            // Connessione al database
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            logger.info("Connessione al database stabilita.");
        } catch (SQLException e) {
            logger.severe("Errore nella connessione al database: " + e.getMessage());
            e.printStackTrace();
            throw e; // rilancia l'eccezione
        }
    }

    // Metodo per determinare l'host (localhost o IP remoto)
    private String getDatabaseHost() {
        // Se l'applicazione è in esecuzione su localhost (lo stesso PC)
        String host = "localhost"; // Default a localhost

        // IP remoto per esempio
        String remoteHost = "192.168.1.13";

        // Controlla se l'applicazione deve usare l'IP remoto invece di localhost
        String useRemoteDb = System.getenv("USE_REMOTE_DB"); // Leggi la variabile di ambiente

        if (useRemoteDb != null && useRemoteDb.equals("true")) {
            host = remoteHost;
            logger.info("Utilizzando database remoto: " + host);
        } else {
            logger.info("Utilizzando database locale: " + host);
        }

        return host;
    }

    @Override
    public boolean login(String usernameOrCodiceFiscale, String psw) {
        try {
            logger.info("Tentativo di login con username o codice fiscale: " + usernameOrCodiceFiscale);

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
        return input.length() == 16 && input.matches("[a-zA-Z0-9]+");
    }

    private boolean loginWithUsername(String username, String psw) throws SQLException {
        logger.info("Tentativo di login con Username o Codice Fiscale: " + username.trim());

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT password FROM operatori WHERE username = ? OR codice_fiscale = ?")) {
            stmt.setString(1, username.trim());
            stmt.setString(2, username.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password").trim();
                logger.info("Password trovata nel database, ma non verrà stampata per sicurezza.");
                boolean passwordMatch = storedPassword.equals(psw.trim());
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

    private boolean loginWithCodiceFiscale(String codiceFiscale, String psw) throws SQLException {
        logger.info("Tentativo di login con Codice Fiscale: " + codiceFiscale.trim());

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT password FROM operatori WHERE LOWER(codice_fiscale) = LOWER(?)")) {
            stmt.setString(1, codiceFiscale.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password").trim();
                logger.info("Password trovata nel database, ma non verrà stampata per sicurezza.");
                boolean passwordMatch = storedPassword.equals(psw.trim());
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

            if (rs.next()) {
                logger.warning("Operatore già esistente con codice fiscale o username: " + op.getCodiceFiscale());
                return false;
            }

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
