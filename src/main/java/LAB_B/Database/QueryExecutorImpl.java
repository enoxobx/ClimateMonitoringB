package LAB_B.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import LAB_B.Common.Coordinate;

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
        String query = "SELECT name, longitude, latitude FROM citta";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
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

    public List<Coordinate> getCoordinate(String name) throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "SELECT name, longitude, latitude FROM citta WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                coordinates.add(new Coordinate(name, lat, lon));
            }
        }
        return coordinates;
    }

    public List<Coordinate> getCoordinate(double latitude, double longitude, double tolerance) throws SQLException {
        ensureConnection();
        List<Coordinate> coordinates = new ArrayList<>();
        String query = "SELECT name, longitude, latitude FROM citta WHERE latitude BETWEEN ? AND ? AND longitude BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
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
}
