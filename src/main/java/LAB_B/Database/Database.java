package LAB_B.Database;

import LAB_B.Common.Operatore;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Database extends Remote {
    boolean login(String cf, String psw) throws RemoteException;
    boolean registrazione(Operatore op) throws RemoteException;
}
