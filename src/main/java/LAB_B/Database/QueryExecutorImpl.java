package LAB_B.Database;

import LAB_B.Common.Coordinate;
import LAB_B.Common.Operatore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    // Metodo per salvare l'operatore
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

        // Verifica che l'email non sia già in uso
        if (emailEsistente(operatore.getEmail())) {
            throw new IllegalArgumentException("L'email è già in uso.");
        }

        // Verifica che il codice fiscale non sia già in uso
        if (codiceFiscaleEsistente(operatore.getCodFiscale())) {
            throw new IllegalArgumentException("Il codice fiscale è già in uso.");
        }

        // Genera lo username
        String username = generateUsername(operatore.getNome(), operatore.getCognome(), operatore.getCodFiscale());
        if (isUsernameExist(username)) {
            throw new IllegalArgumentException("Lo username è già in uso.");
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
    public boolean codiceFiscaleEsistente(String codFisc) throws SQLException {
        ensureConnection();
        String query = "SELECT 1 FROM operatori WHERE codice_fiscale = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codFisc);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }


    // Metodo per verificare se lo username esiste nel database
    private boolean isUsernameExist(String username) throws SQLException {
        return existsInDatabase("username", username);
    }


    // Metodo per generare uno username unico
    private String generateUsername(String nome, String cognome, String codFiscale) throws SQLException {
        String nomeParte = nome.length() >= 3 ? nome.substring(0, 3) : nome;
        String cognomeParte = cognome.length() >= 3 ? cognome.substring(0, 3) : cognome;
        String codFiscaleParte = codFiscale.length() >= 4 ? codFiscale.substring(0, 4) : codFiscale;

        String baseUsername = nomeParte + cognomeParte + codFiscaleParte;
        String username = baseUsername;
        int counter = 1;

        while (isUsernameExist(username)) {
            username = baseUsername + counter;
            counter++;
        }
        return username;
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
}
