package LAB_B.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import LAB_B.Common.Interface.*;

import javax.swing.*;

public class QueryExecutorImpl {
    private static final String SELECT_OPERATORI_QUERY = "SELECT 1 FROM operatori WHERE %s = ? LIMIT 1";
    private Connection conn;

    public QueryExecutorImpl() {
        this.conn = DatabaseImpl.getConnection();
    }

    // Metodo per verificare e mantenere la connessione aperta
    public void ensureConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            System.out.println("Riconnessione al database...");
            conn = DatabaseImpl.getConnection();  // Riconnessione solo se necessario
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Impossibile stabilire la connessione al database.");
            }
        }
    }



    // Metodo per salvare i dati nella tabella centrimonitoraggio
    public boolean salvaCentroMonitoraggio(String nomeCentro, String descrizione, String id) throws SQLException {
        ensureConnection();

        String query = "INSERT INTO centrimonitoraggio (id, nomeCentro, descrizione) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, nomeCentro);
            stmt.setString(3, descrizione);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Ritorna true se almeno una riga è stata modificata
        }
    }





    // Verifica se un campo è vuoto o nullo
    public boolean isNullOrEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Verifica se un valore esiste nel database
    private boolean existsInDatabase(String field, String value) throws SQLException {
        ensureConnection();
        String query = String.format(SELECT_OPERATORI_QUERY, field);
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Metodo per salvare l'operatore nel database
    public boolean salvaOperatore(Operatore operatore) throws SQLException {
        ensureConnection();

        // Verifica che tutti i campi siano validi
        if (isNullOrEmpty(operatore.getNome(), operatore.getCognome(), operatore.getCodFiscale(),
                operatore.getEmail(), operatore.getPassword(), operatore.getCentroMonitoraggio())) {
            throw new IllegalArgumentException("Tutti i campi devono essere compilati.");
        }

        // Verifica la validità di email e codice fiscale
        if (!isValidEmail(operatore.getEmail())) {
            throw new IllegalArgumentException("Email non valida.");
        }

        if (!isValidCodiceFiscale(operatore.getCodFiscale())) {
            throw new IllegalArgumentException("Codice Fiscale non valido.");
        }

        // Verifica se l'email esiste già
        if (emailEsistente(operatore.getEmail())) {
            // Mostra un messaggio di errore con un pop-up
            JOptionPane.showMessageDialog(null, "L'email è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;  // Ferma l'esecuzione se l'email esiste già
        }

        // Verifica se il codice fiscale esiste già
        if (codiceFiscaleEsistente(operatore.getCodFiscale())) {
            // Mostra un messaggio di errore con un pop-up
            JOptionPane.showMessageDialog(null, "Il codice fiscale è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;  // Ferma l'esecuzione se il codice fiscale esiste già
        }

        // Genera lo username solo dopo aver validato tutti i dati
        String username = generateUsername(operatore.getNome(), operatore.getCognome(), operatore.getCodFiscale());
        if (isUsernameExist(username)) {
            JOptionPane.showMessageDialog(null, "Lo username è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Query per inserire l'operatore nel database
        String query = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, centro_monitoraggio, username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false); // Inizia transazione

            stmt.setString(1, operatore.getNome());
            stmt.setString(2, operatore.getCognome());
            stmt.setString(3, operatore.getCodFiscale());
            stmt.setString(4, operatore.getEmail());
            stmt.setString(5, operatore.getPassword());
            stmt.setString(6, operatore.getCentroMonitoraggio());
            stmt.setString(7, username);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return true;
            } else {
                throw new SQLException("Errore nell'inserimento dell'operatore nel database.");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Errore durante il salvataggio dell'operatore.", e);
        } finally {
            conn.setAutoCommit(true); // Ripristina auto commit
        }
    }




    // Metodo per ottenere coordinate senza filtri
    public List<Coordinate> getCoordinate() throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "SELECT * FROM citta";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String geoname_id = rs.getString("geoname_id");
                String nam = rs.getString("name");
                String ascii_name = rs.getString("ascii_name");
                String country_code = rs.getString("country_code");
                String country_name = rs.getString("country_name");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                Citta temp = new Citta(geoname_id, nam, ascii_name, country_code, country_name, lon, lat);
                coordinates.add(new Coordinate(temp));
            }
        }
        return coordinates;
    }

    // Validazione email
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    // Validazione codice fiscale
    private boolean isValidCodiceFiscale(String codiceFiscale) {
        return codiceFiscale != null && codiceFiscale.matches("[A-Z0-9]{16}");
    }

    // Verifica se l'email esiste
    public boolean emailEsistente(String email) throws SQLException {
        return existsInDatabase("email", email);
    }

    // Verifica se il codice fiscale esiste nel database
    public boolean codiceFiscaleEsistente(String codiceFiscale) throws SQLException {
        String query = "SELECT COUNT(*) FROM operatori WHERE codice_fiscale = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codiceFiscale);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // Restituisce true se il codice fiscale esiste
                }
            }
        }
        return false;  // Restituisce false se il codice fiscale non esiste
    }

    public List<Coordinate> getCoordinate(String text) throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "select geoname_id,name,ascii_name,country_code,country_name,latitude,longitude from citta where name like ? ";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            text = "%"+text+"%";
            stmt.setString(1,text);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String geoname_id = rs.getString("geoname_id");
                String nam = rs.getString("name");
                String ascii_name = rs.getString("ascii_name");
                String country_code = rs.getString("country_code");
                String country_name = rs.getString("country_name");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                Citta temp = new Citta(geoname_id, nam, ascii_name, country_code, country_name, lon, lat);
                coordinates.add(new Coordinate(temp));
            }
        }
        return coordinates;
    }

    // Metodo per ottenere coordinate con filtri di latitudine e longitudine
    public List<Coordinate> getCoordinate(double latitude, double longitude, double tolerance) throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "SELECT * FROM citta WHERE latitude BETWEEN ? AND ? AND longitude BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, latitude - tolerance);
            stmt.setDouble(2, latitude + tolerance);
            stmt.setDouble(3, longitude - tolerance);
            stmt.setDouble(4, longitude + tolerance);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String geoname_id = rs.getString("geoname_id");
                String nam = rs.getString("name");
                String ascii_name = rs.getString("ascii_name");
                String country_code = rs.getString("country_code");
                String country_name = rs.getString("country_name");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                Citta temp = new Citta(geoname_id, nam, ascii_name, country_code, country_name, lon, lat);
                coordinates.add(new Coordinate(temp));
            }
        }
        return coordinates;
    }

    // Metodo per verificare se lo username esiste nel database
    private boolean isUsernameExist(String username) throws SQLException {
        return existsInDatabase("username", username);
    }

    public String generateUsername(String nome, String cognome, String codFiscale) throws SQLException {
        // Verifica la validità dei parametri
        if (nome == null || cognome == null || codFiscale == null) {
            throw new IllegalArgumentException("Nome, cognome e codice fiscale non possono essere nulli.");
        }

        // Assicurati che il codice fiscale abbia almeno 16 caratteri
        if (codFiscale.length() != 16) {
            throw new IllegalArgumentException("Il codice fiscale deve avere esattamente 16 caratteri.");
        }

        // Estrai la parte del nome (primi 4 caratteri) e rendi la prima lettera maiuscola
        String nomeParte = nome.length() >= 4 ? nome.substring(0, 4).toUpperCase() : nome.toUpperCase();

        // Estrai la parte del cognome (primi 4 caratteri) e rendi la prima lettera maiuscola
        String cognomeParte = cognome.length() >= 4 ? cognome.substring(0, 4).toUpperCase() : cognome.toUpperCase();

        // Estrai i caratteri dal 6° al 9° del codice fiscale
        String codFiscaleParte = codFiscale.substring(5, 9);

        // Combina le parti per creare il baseUsername
        String baseUsername = nomeParte + cognomeParte + codFiscaleParte;

        // Assicurati che il nome utente generato non superi una lunghezza massima
        int maxLength = 20;  // Limite di lunghezza per lo username
        if (baseUsername.length() > maxLength) {
            baseUsername = baseUsername.substring(0, maxLength);
        }

        // Variabili per gestire il controllo dell'unicità
        String username = baseUsername;
        int counter = 1;

        // Ciclo per verificare se lo username è unico
        while (isUsernameExist(username)) {
            // Se lo username esiste già, aggiungi un numero incrementato
            username = baseUsername + counter;
            counter++;

            // Se lo username supera la lunghezza massima, interrompi
            if (username.length() > maxLength) {
                throw new SQLException("Impossibile generare uno username unico con il formato richiesto.");
            }
        }

        // Restituisci lo username unico
        return username;
    }



}
