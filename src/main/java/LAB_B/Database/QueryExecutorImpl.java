package LAB_B.Database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import LAB_B.Common.Interface.*;

import javax.swing.*;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;

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

    // Validazione email
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    // Validazione codice fiscale
    private boolean isValidCodiceFiscale(String codiceFiscale) {
        return codiceFiscale != null && codiceFiscale.matches("[A-Z0-9]{16}");
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

    // Metodo per verificare se lo username esiste nel database
    private boolean isUsernameExist(String username) throws SQLException {
        return existsInDatabase("username", username);
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
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Metodo per salvare l'operatore nel database
    public boolean salvaOperatore(Operatore operatore) throws SQLException {
        ensureConnection();

        // Verifica che tutti i campi siano validi
        if (isNullOrEmpty(operatore.getNome(), operatore.getCognome(), operatore.getCodFiscale(),
                operatore.getEmail(), operatore.getPassword())) {
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
            JOptionPane.showMessageDialog(null, "L'email è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verifica se il codice fiscale esiste già
        if (codiceFiscaleEsistente(operatore.getCodFiscale())) {
            JOptionPane.showMessageDialog(null, "Il codice fiscale è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Genera lo username solo dopo aver validato tutti i dati
        String username = generateUsername(operatore.getNome(), operatore.getCognome(), operatore.getCodFiscale());
        if (isUsernameExist(username)) {
            JOptionPane.showMessageDialog(null, "Lo username è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Query per inserire l'operatore nel database
        String query = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, username) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int rowsAffected = Tools.setParametri(stmt,
                    operatore.getNome(),
                    operatore.getCognome(),
                    operatore.getCodFiscale(),
                    operatore.getEmail(),
                    operatore.getPassword(),
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

    // Metodo per generare uno username unico
    public String generateUsername(String nome, String cognome, String codFiscale) throws SQLException {
        if (nome == null || cognome == null || codFiscale == null) {
            throw new IllegalArgumentException("Nome, cognome e codice fiscale non possono essere nulli.");
        }

        if (codFiscale.length() != 16) {
            throw new IllegalArgumentException("Il codice fiscale deve avere esattamente 16 caratteri.");
        }

        String nomeParte = nome.length() >= 4 ? nome.substring(0, 4).toUpperCase() : nome.toUpperCase();
        String cognomeParte = cognome.length() >= 4 ? cognome.substring(0, 4).toUpperCase() : cognome.toUpperCase();
        String codFiscaleParte = codFiscale.substring(5, 9);

        String baseUsername = nomeParte + cognomeParte + codFiscaleParte;

        int maxLength = 20;
        if (baseUsername.length() > maxLength) {
            baseUsername = baseUsername.substring(0, maxLength);
        }

        String username = baseUsername;
        int counter = 1;

        while (isUsernameExist(username)) {
            username = baseUsername + counter;
            counter++;

            if (username.length() > maxLength) {
                throw new SQLException("Impossibile generare uno username unico con il formato richiesto.");
            }
        }

        return username;
    }

    /*
     * private List<String> getIDCentri() {
     * 
     * List<String> ids = new ArrayList<String>();
     * try {
     * ensureConnection();
     * String query = "select id from centrimonitoraggio;";
     * try (PreparedStatement stmt = conn.prepareStatement(query)) {
     * ResultSet rs = stmt.executeQuery();
     * while (rs.next()) {
     * ids.add(rs.getString("id"));
     * }
     * }
     * } catch (SQLException e) {
     * // TODO
     * }
     * 
     * return ids;
     * 
     * }
     */
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
    public boolean salvaDatiClimatici(String parametro, String valore, String commento, int punteggio, String username, long timestamp) throws Exception {
        try {
            ensureConnection();  // Assicura una connessione al database

            // Query per inserire i dati nella tabella Parametro
            String queryParametro = "INSERT INTO Parametro (" +
                    "wind, humidity, pressure, temperature, precipitation, glacier_altitude, glacier_mass, " +
                    "wind_comment, humidity_comment, pressure_comment, temperature_comment, precipitation_comment, " +
                    "glacier_altitude_comment, glacier_mass_comment, " +
                    "wind_score, humidity_score, pressure_score, temperature_score, precipitation_score, " +
                    "glacier_altitude_score, glacier_mass_score) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID;";

            // Query per inserire i dati nella tabella Rilevazione
            String queryRilevazione = "INSERT INTO Rilevazione (CF, CentriMonitoraggio_ID, Geoname_ID, Par_ID, date_r) " +
                    "VALUES (?, ?, ?, ?, ?);";


            try (PreparedStatement stmtParametro = conn.prepareStatement(queryParametro, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtRilevazione = conn.prepareStatement(queryRilevazione)) {

                // Recupera le informazioni necessarie
                String cf = getCF(username);
                String centroMonitoraggioId = getCentroMonitoraggioId(username);
                String geonameId = getGeonameId(username);

                // Gestione dei valori nulli per i parametri e i commenti
                valore = (valore == null) ? "" : valore;
                commento = (commento == null) ? "" : commento;

                // Imposta i parametri per la query Parametro
                setParametroValues(stmtParametro, valore, commento, punteggio);

                // Esegui la query per Parametro e ottieni l'ID generato
                try (ResultSet rs = stmtParametro.executeQuery()) {
                    if (rs.next()) {
                        String parametroId = rs.getString("ID");  // Ottieni l'ID generato per il parametro

                        // Inserisci i dati nella tabella Rilevazione
                        stmtRilevazione.setString(1, cf);
                        stmtRilevazione.setString(2, centroMonitoraggioId);
                        stmtRilevazione.setString(3, geonameId);
                        stmtRilevazione.setString(4, parametroId);
                        stmtRilevazione.setDate(5, new java.sql.Date(timestamp));

                        // Esegui la query per Rilevazione
                        int rowsAffected = stmtRilevazione.executeUpdate();
                        conn.commit();  // Commit delle transazioni

                        return rowsAffected > 0;  // Se sono state inserite righe, il salvataggio è riuscito
                    } else {
                        throw new SQLException("Errore: ID Parametro non generato.");
                    }
                } catch (SQLException e) {
                    conn.rollback();  // Annulla in caso di errore
                    throw new Exception("Errore nel salvataggio dei dati climatici: " + e.getMessage(), e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel salvataggio dei dati nel database.", e);
        }
    }

    private void setParametroValues(PreparedStatement stmtParametro, String valore, String commento, int punteggio) throws SQLException {
        // Imposta i parametri nella query Parametro
        for (int i = 1; i <= 6; i++) {
            stmtParametro.setString(i, valore);  // Parametri Wind, Humidity, Pressure, Temperature, Precipitation, Glacier Altitude
        }

        stmtParametro.setString(7, valore);  // Glacier Mass

        // Impostazione dei commenti
        for (int i = 8; i <= 14; i++) {
            stmtParametro.setString(i, commento);  // Commenti per ogni parametro
        }

        // Impostazione dei punteggi
        for (int i = 15; i <= 21; i++) {
            stmtParametro.setInt(i, punteggio);  // Punteggi per ogni parametro
        }

    }

    private String getGeonameId(String username) {
        String query = "SELECT geoname_id FROM Citta WHERE codice_fiscale = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, getCF(username));  // Usa il CF dell'operatore
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("geoname_id");
                }
                return null;  // In caso di nessun risultato
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;  // Gestisce eventuali errori nella query
        }
    }


    private String getCF(String username) {
        // Recupera il codice fiscale dell'operatore dal database
        String query = "SELECT codice_fiscale FROM Operatori WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("codice_fiscale");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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


    public boolean isIdExist(String id) throws SQLException {
        ensureConnection();
        String query = "SELECT 1 FROM centrimonitoraggio WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = Tools.setParametri(stmt, id).executeQuery()) {
                return rs.next(); // Se esiste almeno una riga, l'ID esiste
            }
        }
    }


    private String getCentroMonitoraggioId(String username) {
        // Recupera l'ID del centro di monitoraggio per l'operatore
        String query = "SELECT centrimonitoraggio_id FROM Lavora WHERE operatore_cf = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, getCF(username));  // Usa il CF dell'operatore
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("centro_monitoraggio_id");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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

    public DefaultCategoryDataset getParametri(Coordinate city, TipiPlot type) {

        String query = "SELECT date_r, "+type.getName()+ //
                " FROM rilevazione,parametro " + //
                "where par_id = parametro.id " + //
                "and geoname_id = ? ";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ensureConnection();
            DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
            var geoname = new BigInteger(city.getCitta().getGeoname());
            try (ResultSet rs = Tools.setParametri(stmt, geoname).executeQuery()) {
                String parametro = type.getName();
                while (rs.next()) {
                    var value = rs.getString(parametro);
                    var category = rs.getDate("date_r");

                    //TODO da cambiare quando Golden implementa la sua parte
                    String velocitaNumericaStringa = value.replace(" km/h","");
                    int velocita = Integer.parseInt(velocitaNumericaStringa);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


                    dataSet.addValue(velocita, city.getCitta().toString(), dateFormat.format(category));
                }
                //dataSet.getColumnKeys().stream().forEach(System.out::println);
            } catch (Exception e) {
                e.printStackTrace();
                conn.rollback();
            }

            return dataSet;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
