package LAB_B.Database;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import LAB_B.Common.Interface.*;

import javax.swing.*;

public class QueryExecutorImpl {

    private Connection conn;

    public QueryExecutorImpl() {
        this.conn = DatabaseImpl.getConnection();
    }

    // Metodo per verificare e mantenere la connessione aperta
    public void ensureConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            System.out.println("Riconnessione al database...");
            conn = DatabaseImpl.getConnection(); // Riconnessione solo se necessario
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Impossibile stabilire la connessione al database.");
            }
        }
    }

    // Metodo di login
    public boolean login(String username, String password) {
        boolean loginSuccess = false;

        try {
            ensureConnection(); // Verifica che la connessione sia attiva

            String query = "SELECT * FROM operatori WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet resultSet = Tools.setParametri(stmt, username, password).executeQuery()) {
                    if (resultSet.next()) {
                        loginSuccess = true; // Se il risultato esiste, login riuscito
                    }
                }
            }
        } catch (SQLException e) {
            // Gestione dell'eccezione SQL
            System.err.println("Errore durante l'esecuzione del login: " + e.getMessage());
            e.printStackTrace(); // Facoltativo: per debugging dettagliato
            // Puoi usare un logger per salvare il messaggio di errore in un file di log
        }

        return loginSuccess;
    }

    private List<String> getIDCentri() {

        List<String> ids = new ArrayList<String>();
        try {
            ensureConnection();
            String query = "select id from centrimonitoraggio;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ids.add(rs.getString("id"));
                }
            }
        } catch (SQLException e) {
            // TODO
        }

        return ids;

    }

    // restituisce l'id dei centri sapendo il loro nome
    private List<String> getIDCentri(String name) {

        List<String> ids = new ArrayList<String>();
        try {
            ensureConnection();
            String query = "select id from centrimonitoraggio where nomeCentro like ? ";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = Tools.setParametri(stmt, name).executeQuery();
                while (rs.next()) {
                    ids.add(rs.getString("id"));
                }
            }
        } catch (SQLException e) {
            // TODO
        }

        return ids;

    }

    public String getGeonameIdByName(String cittaSelezionata) {
        String geonameID = null; // Valore di ritorno se non trovato
        try {
            ensureConnection(); // Verifica che la connessione sia attiva

            String query = "SELECT Geoname_ID FROM Citta WHERE Nome = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                try (ResultSet rs = Tools.setParametri(stmt, cittaSelezionata).executeQuery()) {
                    if (rs.next()) {
                        geonameID = rs.getString("Geoname_ID"); // Recupera il Geoname_ID
                    }
                }
            }
        } catch (SQLException e) {
            // Gestione dell'errore
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante il recupero dell'ID Geoname.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }

        return geonameID;
    }

    private String getCF(String username) {
        String res = "";
        try {
            ensureConnection();
            String query = "select codice_fiscale from operatori where username = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = Tools.setParametri(stmt, username).executeQuery();
                if (rs.next()) { // Verifica se sono presenti risultati
                    res = rs.getString("codice_fiscale");
                }
            }
        } catch (SQLException e) {
            // TODO: gestire l'eccezione (ad esempio, log, rilancio)
            e.printStackTrace();
        }
        return res;
    }

    public boolean salvaCentroMonitoraggio(String nomeCentro, String indirizzo, String currentUsername)
            throws Exception {
        try {
            ensureConnection();
            String query = "INSERT INTO centrimonitoraggio (id, nomeCentro, indirizzo) VALUES (gen_random_uuid(), ?, ?) RETURNING id;";

            String query2 = "INSERT INTO lavora(cf,centrimonitoraggio_id) values (?, ?);";

            try (PreparedStatement stmt = conn.prepareStatement(query);
                    PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                String cf = getCF(currentUsername);
                ResultSet rs = Tools.setParametri(stmt, nomeCentro, indirizzo).executeQuery();
                rs.next();
                String id = rs.getString("id");
                int rowsAffected = Tools.setParametri(stmt2, cf, id).executeUpdate();
                conn.commit(); // Forza il salvataggio nel database
                return rowsAffected > 0;
            } catch (SQLException e) {
                conn.rollback(); // Annulla la transazione in caso di errore
                throw new Exception(e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nel salvataggio del centro monitoraggio.", e);
        }
    }

    private boolean salvaRilevazione(String currentUsername, String centroMonitoraggio, long geonameID,
            String parametroID) {
        try {
            ensureConnection();

            // Recupera il codice fiscale dell'utente
            String cf = getCF(currentUsername);
            List<String> centroMonitoraggioID = getIDCentri(centroMonitoraggio);

            String query = "INSERT INTO Rilevazione (CF, CentriMonitoraggio_ID, Geoname_ID, Par_ID, date_r) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                int rowsAffected = Tools.setParametri(stmt, cf, centroMonitoraggioID.getFirst(), geonameID, parametroID,
                        new java.sql.Date(System.currentTimeMillis())).executeUpdate();
                conn.commit(); // Conferma le modifiche

                return rowsAffected > 0; // Restituisce true se l'inserimento è stato effettuato con successo
            } catch (SQLException e) {
                conn.rollback(); // Annulla la transazione in caso di errore
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nel salvataggio della rilevazione.", e);
        }
    }

    public boolean salvaDatiClimatici(String key, String centroID, JComboBox<Integer>[] scoreDropdowns,
            JTextArea[] severitaTextAreas, String username, long geo_id) throws SQLException {
        try {
            ensureConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "INSERT INTO Parametro (" +
                "ID, wind, humidity, pressure, temperature, precipitation, " +
                "glacier_altitude, glacier_mass, wind_comment, humidity_comment, " +
                "pressure_comment, temperature_comment, precipitation_comment, " +
                "glacier_altitude_comment, glacier_mass_comment, wind_score, " +
                "humidity_score, pressure_score, temperature_score, precipitation_score, " +
                "glacier_altitude_score, glacier_mass_score) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            Tools.setParametri(stmt,
                    key, // Chiave primaria
                    "N/A", // wind (da definire)
                    "N/A", // humidity (da definire)
                    "N/A", // pressure (da definire)
                    "N/A", // temperature (da definire)
                    "N/A", // precipitation (da definire)
                    "N/A", // glacier_altitude (da definire)
                    "N/A", // glacier_mass (da definire)
                    severitaTextAreas[0].getText(), // wind_comment
                    severitaTextAreas[1].getText(), // humidity_comment
                    "N/A", // pressure_comment (da definire)
                    severitaTextAreas[2].getText(), // temperature_comment
                    severitaTextAreas[3].getText(), // precipitation_comment
                    "N/A", // glacier_altitude_comment (da definire)
                    "N/A", // glacier_mass_comment (da definire)
                    new BigDecimal(scoreDropdowns[0].getSelectedItem().toString()), // wind_score
                    new BigDecimal(scoreDropdowns[1].getSelectedItem().toString()), // humidity_score
                    0, // pressure_score (da definire)
                    new BigDecimal(scoreDropdowns[2].getSelectedItem().toString()), // temperature_score
                    new BigDecimal(scoreDropdowns[3].getSelectedItem().toString()), // precipitation_score
                    0, // glacier_altitude_score (da definire)
                    0) // glacier_mass_score (da definire)
                    .executeUpdate();

            salvaRilevazione(username, centroID, geo_id, key);

            conn.commit(); // Conferma le modifiche
            JOptionPane.showMessageDialog(null, "Dati climatici salvati con successo.");
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            conn.rollback(); // Annulla le modifiche in caso di errore
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante il salvataggio dei dati climatici: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
        return false;
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
        String SELECT_OPERATORI_QUERY = "SELECT 1 FROM operatori WHERE %s = ? LIMIT 1";
        String query = String.format(SELECT_OPERATORI_QUERY, field);
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = Tools.setParametri(stmt, value).executeQuery()) {
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
            return false; // Ferma l'esecuzione se l'email esiste già
        }

        // Verifica se il codice fiscale esiste già
        if (codiceFiscaleEsistente(operatore.getCodFiscale())) {
            // Mostra un messaggio di errore con un pop-up
            JOptionPane.showMessageDialog(null, "Il codice fiscale è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false; // Ferma l'esecuzione se il codice fiscale esiste già
        }

        // Genera lo username solo dopo aver validato tutti i dati
        String username = generateUsername(operatore.getNome(), operatore.getCognome(), operatore.getCodFiscale());
        if (isUsernameExist(username)) {
            JOptionPane.showMessageDialog(null, "Lo username è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Query per inserire l'operatore nel database
        String query = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, centro_monitoraggio, username) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int rowsAffected = Tools.setParametri(stmt,
                    operatore.getNome(),
                    operatore.getCognome(),
                    operatore.getCodFiscale(),
                    operatore.getEmail(),
                    operatore.getPassword(),
                    operatore.getCentroMonitoraggio(),
                    username).executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return true;
            } else {
                throw new SQLException("Errore nell'inserimento dell'operatore nel database.");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Errore durante il salvataggio dell'operatore.", e);
        }
    }

    public boolean isIdExist(String id) throws SQLException {

        if (isIdExist(id)) {
            throw new IllegalArgumentException("ID già esistente: " + id);
        }

        ensureConnection();
        String query = "SELECT 1 FROM centrimonitoraggio WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = Tools.setParametri(stmt, id).executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<String> getCentriMonitoraggio() throws SQLException {
        ensureConnection();
        List<String> centri = new ArrayList<>();
        String query = "SELECT nomeCentro FROM centrimonitoraggio";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    centri.add(rs.getString("nomeCentro"));
                }
            }
        }
        return centri;
    }

    // Metodo per ottenere coordinate senza filtri
    public List<Coordinate> getCoordinate() throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "SELECT * FROM citta ORDER BY name ASC ";
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
            try (ResultSet rs = Tools.setParametri(stmt, codiceFiscale).executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Restituisce true se il codice fiscale esiste
                }
            }
        }
        return false; // Restituisce false se il codice fiscale non esiste
    }

    public List<Coordinate> getCoordinate(String text) throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "select geoname_id,name,ascii_name,country_code,country_name,latitude,longitude from citta where name like ?  ORDER BY name ASC ";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            text = "%" + text + "%";
            ResultSet rs = Tools.setParametri(stmt, text).executeQuery();
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
            ResultSet rs = Tools.setParametri(stmt, latitude - tolerance, latitude + tolerance, longitude - tolerance,
                    longitude + tolerance).executeQuery();
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

    public List<String> getCentriPerOperatore(String username) {
        List<String> centrimonitoraggio = new ArrayList<>();
        String query = "SELECT nomecentro " +
                "FROM lavora, operatori, centrimonitoraggio " +
                "WHERE cf = codice_fiscale AND id = centrimonitoraggio_id " +
                "AND username = ? ";
        try {
            ensureConnection(); // Assicurati che la connessione sia attiva
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = Tools.setParametri(stmt, username).executeQuery()) {
                    while (rs.next()) {
                        centrimonitoraggio.add(rs.getString("nomecentro"));
                    }
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return centrimonitoraggio;
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

        // Estrai la parte del nome (primi 4 caratteri) e rendi la prima lettera
        // maiuscola
        String nomeParte = nome.length() >= 4 ? nome.substring(0, 4).toUpperCase() : nome.toUpperCase();

        // Estrai la parte del cognome (primi 4 caratteri) e rendi la prima lettera
        // maiuscola
        String cognomeParte = cognome.length() >= 4 ? cognome.substring(0, 4).toUpperCase() : cognome.toUpperCase();

        // Estrai i caratteri dal 6° al 9° del codice fiscale
        String codFiscaleParte = codFiscale.substring(5, 9);

        // Combina le parti per creare il baseUsername
        String baseUsername = nomeParte + cognomeParte + codFiscaleParte;

        // Assicurati che il nome utente generato non superi una lunghezza massima
        int maxLength = 20; // Limite di lunghezza per lo username
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
