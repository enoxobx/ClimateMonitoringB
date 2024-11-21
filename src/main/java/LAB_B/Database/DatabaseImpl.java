package LAB_B.Database;

import LAB_B.Common.Operatore;  // Importa la classe Operatore, presumibilmente per gestire gli operatori nel sistema

import javax.swing.*;  // Importa le librerie Swing (non utilizzate direttamente in questo codice)
import java.rmi.server.UnicastRemoteObject;  // Permette la creazione di oggetti remoti per RMI
import java.sql.*;  // Importa le librerie JDBC per interagire con il database

// La classe DatabaseImpl implementa l'interfaccia Database e fornisce le implementazioni per i metodi dichiarati
public class DatabaseImpl extends UnicastRemoteObject implements Database {
    private Connection conn;  // Variabile per memorizzare la connessione al database

    // Costruttore che estende UnicastRemoteObject e gestisce la connessione al database
    public DatabaseImpl() throws Exception {
        super();  // Chiama il costruttore della classe base UnicastRemoteObject
        connectToDatabase();  // Invoca il metodo per connettersi al database
    }

    // Metodo per stabilire la connessione al database PostgreSQL
    private void connectToDatabase() throws SQLException {
        // Parametri di connessione per il database PostgreSQL
        String dbUrl = "jdbc:postgresql://localhost:5432/climate_monitoring";  // URL del database
        String dbUsername = "postgres";  // Nome utente per il login al database
        String dbPassword = "0000";  // Password per il login al database

        // Crea la connessione utilizzando DriverManager
        conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        System.out.println("Connessione al database stabilita.");  // Messaggio di conferma
    }

    // Metodo per gestire il login degli operatori
    @Override
    public boolean login(String username, String psw) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT password FROM operatori WHERE username = ?")) {
            stmt.setString(1, username);  // Imposta il valore del parametro "username"
            ResultSet rs = stmt.executeQuery();  // Esegue la query e ottiene il risultato

            // Se viene trovato un risultato, confronta la password
            if (rs.next()) {
                return rs.getString("password").trim().equals(psw.trim());  // Confronta le password, rimuovendo eventuali spazi extra
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Stampa eventuali errori di SQL
        }
        return false;  // Restituisce false se la login fallisce o se ci sono errori
    }

    // Metodo per gestire la registrazione di un nuovo operatore
    @Override
    public boolean registrazione(Operatore op) {
        try (PreparedStatement stmtCheck = conn.prepareStatement(
                "SELECT * FROM operatori WHERE codice_fiscale = ? OR username = ?")) {
            // Controlla se esiste già un operatore con lo stesso codice fiscale o username
            stmtCheck.setString(1, op.getCodiceFiscale());
            stmtCheck.setString(2, op.getUsername());  // Imposta il parametro "username"
            ResultSet rs = stmtCheck.executeQuery();  // Esegui la query per il controllo dell'esistenza

            // Se esiste un operatore con lo stesso codice fiscale o username, la registrazione fallisce
            if (rs.next()) {
                return false;  // Restituisce false se l'operatore esiste già
            }

            // Se non esiste, inserisce i dati dell'operatore nel database
            try (PreparedStatement stmtInsert = conn.prepareStatement(
                    "INSERT INTO operatori (codice_fiscale, name, surname, email, username, password) VALUES (?, ?, ?, ?, ?, ?)")) {
                // Imposta i valori dei parametri per l'inserimento
                stmtInsert.setString(1, op.getCodiceFiscale());
                stmtInsert.setString(2, op.getName());
                stmtInsert.setString(3, op.getSurname());
                stmtInsert.setString(4, op.getEmail());  // Aggiungi il campo email
                stmtInsert.setString(5, op.getUsername());  // Aggiungi il campo username
                stmtInsert.setString(6, op.getPassword());

                // Esegui l'inserimento e restituisce true se almeno una riga è stata inserita
                return stmtInsert.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Stampa eventuali errori di SQL
        }
        return false;  // Restituisce false se l'inserimento fallisce o si verifica un errore
    }
}
