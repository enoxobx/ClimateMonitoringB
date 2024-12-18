package LAB_B.Database;

import java.rmi.RemoteException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import LAB_B.Common.Coordinate;

public class QueryExecutorImpl {
    private Connection conn; // Connessione al database

    // Costruttore: inizializza l'oggetto con una connessione al database
    public QueryExecutorImpl(Connection conn) {
        this.conn = conn; // Memorizza la connessione per l'uso nelle query
    }

    // Metodo per eseguire una query di selezione (SELECT)
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        // Prepara la query SQL
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Imposta i parametri della query (se presenti)
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]); // Imposta ogni parametro nella query
            }
            // Esegui la query e restituisci il risultato
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore durante l'esecuzione della query: " + e.getMessage());
            throw e; // Rilancia l'eccezione per il trattamento in un livello superiore
        }
    }

    // Metodo per eseguire una query di modifica (INSERT, UPDATE, DELETE)
    public int executeUpdate(String query, Object... params) throws SQLException {
        // Prepara la query SQL
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Imposta i parametri della query (se presenti)
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]); // Imposta ogni parametro nella query
            }
            // Esegue la query e restituisce il numero di righe modificate (inserite,
            // aggiornate o eliminate)
            return stmt.executeUpdate();
        }
    }


    //prendere da   ESEMPIO
    public List<Coordinate> getCoordinate() throws SQLException {
        List<Coordinate> temp = new ArrayList<>();
        if (this.conn == null || this.conn.isClosed()) {
            this.conn = DatabaseImpl.getConnection();
        }
        String query = "select name,longitude,latitude from citta ;";
        try (PreparedStatement stmt = conn.prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        String name = rs.getString("name");
                        Double lat = rs.getDouble("latitude");
                        Double lon = rs.getDouble("longitude");
                        temp.add(new Coordinate(name, lat, lon));
        
                    }
                    
        }
        return temp;
    }

    public List<Coordinate> getCoordinate(double latitude, double longitude, double tolerance) throws SQLException {
        List<Coordinate> coordinates = new ArrayList<>();  // Use descriptive name

        try (Connection conn = DatabaseImpl.getConnection(); // Get connection outside the if block
             PreparedStatement stmt = conn.prepareStatement("SELECT name, longitude, latitude FROM citta " +
                     "WHERE latitude >= ? AND latitude <= ? AND longitude >= ? AND longitude <= ? ;")) {

            // Set parameters for prepared statement
            stmt.setDouble(1, latitude - tolerance);
            stmt.setDouble(2, latitude + tolerance);
            stmt.setDouble(3, longitude - tolerance);
            stmt.setDouble(4, longitude + tolerance);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                coordinates.add(new Coordinate(name, lat, lon));
            }
        }

        return coordinates;
    }

    


    //TODO
    public List<Coordinate> getCoordinate(String name) throws SQLException {
        List<Coordinate> temp = new ArrayList<>();
        String query = "select name,longitude,latitude from citta where name = {0};";
        query = MessageFormat.format(query, name);
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //String name = rs.getString("name");
                Double lat = rs.getDouble("latitude");
                Double lon = rs.getDouble("longitude");
                temp.add(new Coordinate(name, lat, lon));

            }
            return temp;
        }
    }

    
}
