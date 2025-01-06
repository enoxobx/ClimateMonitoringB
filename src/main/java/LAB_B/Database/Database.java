package LAB_B.Database;

import LAB_B.Common.*;
import LAB_B.Common.Interface.Coordinate;
import LAB_B.Common.Interface.Operatore;

import java.util.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public interface Database extends Remote{

    // Esegui una query generica con parametri variabili
    ResultSet executeQuery(String query, Object... params) throws RemoteException, SQLException;

    // Esegui un'operazione di aggiornamento (INSERT, UPDATE, DELETE) con parametri variabili
    int executeUpdate(String query, Object... params) throws RemoteException, SQLException;

    // Metodo per il login dell'operatore, dato il codice fiscale e la password
    boolean login(String codiceFiscale, String password) throws RemoteException, SQLException;

    // Metodo per la registrazione di un nuovo operatore
    boolean registrazione(Operatore operatore) throws RemoteException;

    List<Coordinate> getCoordinaResultSet()throws RemoteException ;
    List<Coordinate> getCoordinaResultSet(String name) throws RemoteException;
    List<Coordinate> getCoordinaResultSet(double latitude, double longitude, double tollerance )throws RemoteException ;

    List<String> getCentriPerOperatore(String username)throws RemoteException;
    boolean salvaCentroMonitoraggio(String nomeCentro,String descrizione,String username) throws RemoteException;
    boolean SalvaRilevazione(String currentUsername, String centroMonitoraggioID, long geonameID, String parametroID) throws RemoteException;
}
