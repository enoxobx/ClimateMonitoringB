package LAB_B.Client;

import java.sql.*;

// Aggiungi la classe Database (presumibilmente definita altrove)
import LAB_B.Database.Database;
import LAB_B.Database.DatabaseImpl;

public class Client {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;

    // Aggiungi un campo per il Database
    private static Database db;

    // Costruttore per inizializzare la connessione al database
    public Client(String dbUrl, String dbUsername, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    // Metodo per ottenere la connessione al database (esistente)
    public Connection getDbConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


        //private static Database db;

        public static Database getDb() {
            if (db == null) {
                try {
                    // Inizializza db come un'istanza concreta di DatabaseImpl
                    db = new DatabaseImpl();  // Usa la classe concreta
                    // Puoi anche chiamare un metodo per la connessione, se necessario
                    // db.connect();  // Questo dipende dal tuo caso specifico
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return db;
        }
    }

