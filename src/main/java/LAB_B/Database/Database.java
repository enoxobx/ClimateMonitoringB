package LAB_B.Database;

import LAB_B.Common.*;
import LAB_B.Common.Interface.Coordinate;
import LAB_B.Common.Interface.Operatore;

import java.util.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Database extends Remote{
    ResultSet executeQuery(String query, Object... params)throws RemoteException ;
    int executeUpdate(String query, Object... params)throws RemoteException ;

    boolean login(String codice_fiscale, String password) throws RemoteException;
    boolean registrazione(Operatore op) throws RemoteException;
    List<Coordinate> getCoordinaResultSet()throws RemoteException ;
    public List<Coordinate> getCoordinaResultSet(String name) throws RemoteException;
    List<Coordinate> getCoordinaResultSet(double latitude, double longitude, double tollerance )throws RemoteException ;
}
