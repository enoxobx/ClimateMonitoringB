package LAB_B.Database;
import LAB_B.Common.Operatore;

import java.rmi.*;
import java.sql.SQLException;

public interface Database extends Remote{

    /*
    *return true se l'utente è predente della tabella operatori, false altrimenti
    */
    boolean login (String usr, String psw) throws RemoteException;


    /*
    aggiunge l'utente nel db se non è presente, altrimenti notifica che è già presente
     */
    boolean registrazione(Operatore op) throws RemoteException;
}
