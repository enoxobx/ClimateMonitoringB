package LAB_B.Database;

import LAB_B.Common.Coordinate;
import LAB_B.Common.Operatore;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Database extends Remote {
    // Esegui una query generica con parametri variabili
    ResultSet executeQuery(String query, Object... params) throws RemoteException, SQLException;

    // Esegui un'operazione di aggiornamento (INSERT, UPDATE, DELETE) con parametri variabili
    int executeUpdate(String query, Object... params) throws RemoteException, SQLException;

    // Metodo per il login dell'operatore, dato il codice fiscale e la password
    boolean login(String codiceFiscale, String password) throws RemoteException, SQLException;

    // Metodo per la registrazione di un nuovo operatore
    boolean registrazione(Operatore operatore) throws RemoteException, SQLException;

    List<LAB_B.Common.Coordinate> getCoordinaResultSet()throws RemoteException ;
    List<Coordinate> getCoordinaResultSet(double latitude, double longitude, double tollerance ) throws RemoteException, SQLException;

}
