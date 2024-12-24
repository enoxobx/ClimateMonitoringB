package LAB_B.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import LAB_B.Common.Interface.Citta;
import LAB_B.Common.Interface.Coordinate;

public class QueryExecutorImpl {
    private Connection conn;

    public QueryExecutorImpl(Connection conn) {
        this.conn = conn;
    }

    private void ensureConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DatabaseImpl.getConnection();
        }
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        ensureConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
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
                coordinates.add(new Coordinate(temp));;
            }
        }
        return coordinates;
    }
}
