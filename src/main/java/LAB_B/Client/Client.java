package LAB_B.Client;

import LAB_B.Database.Database;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Database db;

    public void start() {
        try {
            // Connessione al registro RMI sulla porta corretta (11111)
            Registry registry = LocateRegistry.getRegistry("localhost", 11111);
            db = (Database) registry.lookup("DatabaseImpl");

            System.out.println("Connessione al server riuscita.");
        } catch (Exception e) {
            System.err.println("Errore nella connessione al server RMI. Assicurarsi che il server sia attivo.");
            e.printStackTrace();
        }
    }


    public boolean login(String usr, String psw) {
        try {
            // Verifica se la connessione al database è attiva
            if (db == null) {
                System.err.println("Impossibile eseguire il login: server non disponibile.");
                return false;
            }

            // Chiamata al metodo login sul server remoto
            System.out.println("Esecuzione login per l'utente: " + usr);
            boolean isLoggedIn = db.login(usr, psw);

            if (isLoggedIn) {
                System.out.println("Login riuscito per l'utente: " + usr);
            } else {
                System.out.println("Login fallito per l'utente: " + usr);
            }

            return isLoggedIn;

        } catch (Exception e) {
            System.err.println("Errore durante il login.");
            e.printStackTrace();
            return false;
        }
    }
}
