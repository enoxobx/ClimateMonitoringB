package LAB_B.Client;

import LAB_B.Common.*;
import LAB_B.Database.Database;

import java.util.*;

import org.postgresql.core.SqlCommand;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    private static Registry registry;
    private static volatile Database db;
    
    public static  Database getDb() {
        if (db == null){
           synchronized(Client.class){
            try {
                //Connesione al registro RMI
                registry = LocateRegistry.getRegistry("localhost",1099);
    
                //Connessione al db
                db = (Database) registry.lookup("Server");
                System.out.println("connessione riuscita");
                } catch (Exception e) {
                    e.printStackTrace();
                
                } 
           }
        }
        return db;
        
    }
    
}