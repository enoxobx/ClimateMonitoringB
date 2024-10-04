package LAB_B.Database;

import LAB_B.Common.Operatore;

import javax.xml.crypto.Data;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class DatabaseImpl extends UnicastRemoteObject implements Database {
    private String host = "localhost";
    private String port = "5433";
    private String dbName = "ClimateMonitoring";
    private String user = "postgres";
    private String psw = "123456789";
    private Connection c;

    public DatabaseImpl() throws RemoteException {
        super();
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        try {
            creaCon(url);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void creaCon(String url) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, psw);
            System.out.println("Connesione riuscita lato db");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }

    }


    @Override
    public boolean login(String usr, String psw) {

        try {
            // Esecuzione della query
            String sql = "SELECT UserId,password FROM Operatori WHERE UserId = ? and password = ? ;";
            PreparedStatement q = c.prepareStatement(sql);
            q.setString(1, usr);
            q.setString(2, psw);
            ResultSet rs = q.executeQuery();
            while (rs.next()) if (rs.getString("UserId").equals(usr)) return true;
            // Chiusura delle risorse
            rs.close();
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean registrazione(Operatore op) {
        try {
            String sql = "INSERT INTO operatori (cf,name,surname,userId,mail,password) VALUES ( ?,?,?,?,?,?);";
            PreparedStatement q = c.prepareStatement(sql);
            q.setString(1,op.getCf());
            q.setString(2,op.getName());
            q.setString(3,op.getSurname());
            q.setString(4,op.getUserId());
            q.setString(5,op.getMail());
            q.setString(6,op.getPassword());
            int rowAffected = q.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }


    }

    public boolean close() {
        if (c == null) return true;
        else {
            try {
                c.close();
                return true;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return false;
            }
        }
    }

}
