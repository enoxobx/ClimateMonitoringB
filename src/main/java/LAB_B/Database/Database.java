package LAB_B.Database;

import LAB_B.Common.*;
import java.util.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

import LAB_B.Common.Operatore;

public interface Database extends Remote{
    ResultSet executeQuery(String query, Object... params)throws RemoteException ;
    int executeUpdate(String query, Object... params)throws RemoteException ;

    boolean login(String cf, String psw) throws RemoteException;
    boolean registrazione(Operatore op) throws RemoteException;
    List<Coordinate> getCoordinaResultSet()throws RemoteException ;
    List<Coordinate> getCoordinaResultSet(double latitude, double longitude, double tollerance )throws RemoteException ;
}
