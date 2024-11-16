//Proxy (Client-side)
package LAB_B.Client;

import LAB_B.Common.Operatore;
import LAB_B.Database.Database;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client{

    private Registry registry;
    private Database db;

    public Client() {
        try {
            //Connesione al registro RMI
            registry = LocateRegistry.getRegistry("localhost",1099);

            //Connessione al db
            db = (Database) registry.lookup("DatabaseImpl");
            System.out.println("connessione riuscita");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean login(String usr, String psw){
        try {
            return db.login(usr,psw);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }



}