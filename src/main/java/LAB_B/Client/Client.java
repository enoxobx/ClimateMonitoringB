package LAB_B.Client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import LAB_B.Common.Config;
import LAB_B.Database.Database;

public class Client {
    



    // private static Database db;

    public static Database getDb() throws RemoteException {

        try {

            Config cf = new Config();

            Registry registry = LocateRegistry.getRegistry(cf.getHost(), cf.getRmiPort());
            return (Database) registry.lookup(cf.getRmiDbName()); // Ottieni un nuovo stub ogni volta

            
        } catch (java.rmi.NotBoundException nbe) {
            System.err.println("Errore: oggetto remoto non trovato nel registry: " + nbe.getMessage());
            throw new RemoteException("Oggetto remoto non trovato", nbe); // Rilancia un'eccezione RemoteException
        } catch (Exception e) {
            System.err.println("Errore durante la connessione RMI: " + e.getMessage());
            throw new RemoteException("Errore RMI", e); // Rilancia un'eccezione RemoteException
        }

    }
}
