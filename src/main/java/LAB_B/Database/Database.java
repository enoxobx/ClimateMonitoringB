package LAB_B.Database;

import LAB_B.Common.Interface.Coordinate;
import LAB_B.Common.Interface.Operatore;
import LAB_B.Common.Interface.TipiPlot;

import javax.swing.*;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
public interface Database extends Remote{

    // Metodo per il login dell'operatore, dato il codice fiscale e la password
    boolean login(String codiceFiscale, String password) throws RemoteException, SQLException;

    // Metodo per la registrazione di un nuovo operatore
    boolean registrazione(Operatore operatore) throws RemoteException;

    List<Coordinate> getCoordinaResultSet()throws RemoteException ;


    boolean salvaDatiClimatici(String parametro, String valore, String commento, int punteggio, String username, long timestamp) throws Exception;

    List<Coordinate> getCoordinaResultSet(String name) throws RemoteException;
    
    List<Coordinate> getCoordinaResultSet(double latitude, double longitude, double tollerance )throws RemoteException ;

    List<String> getCentriPerOperatore(String username)throws RemoteException;
    
    boolean salvaCentroMonitoraggio(String nomeCentro,String descrizione,String username) throws Exception;


    DefaultCategoryDataset getParametri(Coordinate city, TipiPlot type) throws RemoteException;
}
