package LAB_B.Database;

import LAB_B.Common.Coordinate;
import LAB_B.Common.Operatore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryExecutorImpl {
    private Connection conn;

    public QueryExecutorImpl(Connection conn) {
        this.conn = conn;
    }

    private void ensureConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DatabaseImpl.getConnection();  // Assuming DatabaseImpl provides a static method to get the connection
        }
    }

    // Metodo per eseguire una query generica
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        ensureConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore esecuzione query: " + e.getMessage());
            System.err.println("Query: " + query);
            throw e;
        }
    }

    // Metodo per eseguire una query di aggiornamento
    public int executeUpdate(String query, Object... params) throws SQLException {
        ensureConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore esecuzione query: " + e.getMessage());
            System.err.println("Query: " + query);
            throw e;
        }
    }

    // Metodo per ottenere coordinate senza filtri
    public List<Coordinate> getCoordinate() throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "SELECT name, longitude, latitude FROM citta";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                coordinates.add(new Coordinate(name, lat, lon));
            }
        }
        return coordinates;
    }

    // Metodo per ottenere coordinate con filtri di latitudine e longitudine
    public List<Coordinate> getCoordinate(double latitude, double longitude, double tolerance) throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "SELECT name, longitude, latitude FROM citta WHERE latitude BETWEEN ? AND ? AND longitude BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, latitude - tolerance);
            stmt.setDouble(2, latitude + tolerance);
            stmt.setDouble(3, longitude - tolerance);
            stmt.setDouble(4, longitude + tolerance);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    double lat = rs.getDouble("latitude");
                    double lon = rs.getDouble("longitude");
                    coordinates.add(new Coordinate(name, lat, lon));
                }
            }
        }
        return coordinates;
    }

    // Metodo per ottenere tutti gli operatori
    public List<Operatore> getOperatori() throws SQLException {
        String query = "SELECT * FROM operatori";
        try (ResultSet rs = executeQuery(query)) {
            List<Operatore> operatoriList = new ArrayList<>();
            while (rs.next()) {
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String codFiscale = rs.getString("codice_fiscale");
                String email = rs.getString("email");
                // Crea un oggetto Operatore con i dati ottenuti
                Operatore operatore = new Operatore(nome, cognome, codFiscale, email);
                operatoriList.add(operatore);
            }
            return operatoriList;
        }
    }

    // Metodo per salvare un operatore nel database con validazione migliorata
    public boolean salvaOperatore(String nome, String cognome, String codiceFiscale, String email,
                                  String password, String centroMonitoraggio, String username) throws SQLException {
        ensureConnection();

        // Eseguiamo alcune validazioni sui parametri prima di eseguire la query
        if (isNullOrEmpty(nome, cognome, codiceFiscale, email, password, centroMonitoraggio, username)) {
            throw new IllegalArgumentException("Tutti i campi devono essere compilati.");
        }

        // Aggiungere eventuali validazioni come quella dell'email
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email non valida.");
        }

        // Validazione del codice fiscale
        if (!isValidCodiceFiscale(codiceFiscale)) {
            throw new IllegalArgumentException("Codice Fiscale non valido.");
        }

        String query = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, centro_monitoraggio, username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Imposta i parametri della query
            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            stmt.setString(3, codiceFiscale);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, centroMonitoraggio);
            stmt.setString(7, username);

            // Esegui l'aggiornamento
            int rowsAffected = stmt.executeUpdate();

            // Se l'aggiornamento è stato eseguito correttamente, ritorniamo true
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio dell'operatore: " + e.getMessage());
            throw e;  // Rilancia l'eccezione per una gestione successiva
        }
    }

    // Metodo per validare l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    // Metodo per validare il codice fiscale (semplificato per esempio)
    private boolean isValidCodiceFiscale(String codiceFiscale) {
        return codiceFiscale != null && codiceFiscale.matches("[A-Z0-9]{16}");
    }

    // Metodo per verificare se un campo è nullo o vuoto
    private boolean isNullOrEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Metodo per verificare se un codice fiscale esiste già nel database
    public boolean codiceFiscaleEsistente(String codiceFiscale) throws SQLException {
        ensureConnection();
        String query = "SELECT COUNT(*) FROM operatori WHERE codice_fiscale = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codiceFiscale);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Se count è maggiore di 0, significa che il codice fiscale esiste
                }
            }
        }
        return false; // Se il codice fiscale non esiste, ritorniamo false
    }

    // Metodo per verificare se un'email esiste già nel database
    public boolean emailEsistente(String email) throws SQLException {
        ensureConnection();
        String query = "SELECT COUNT(*) FROM operatori WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // Se il conteggio è maggiore di 0, significa che l'email esiste già
                }
            }
        }
        return false;
    }
    // Metodo per verificare se un username esiste già nel database
    public boolean isUsernameExist(String username) throws SQLException {
        ensureConnection();
        String query = "SELECT COUNT(*) FROM operatori WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Se count è maggiore di 0, significa che l'username esiste
                }
            }
        }
        return false; // Se l'username non esiste, ritorniamo false
    }

}
