package LAB_B.Client;

import LAB_B.Database.Database;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Database db;

    public void start() {
        int attempts = 0;
        while (attempts < 5) { // Tentativi limitati a 5
            try {
                // Connessione al registro RMI sulla porta 12345
                Registry registry = LocateRegistry.getRegistry("localhost", 12345);
                db = (Database) registry.lookup("DatabaseImpl");

                System.out.println("Connessione al server riuscita.");
                return; // Esci dal ciclo se la connessione ha successo
            } catch (Exception e) {
                attempts++;
                System.err.println("Errore nella connessione al server RMI. Tentativo " + attempts + "...");
                try {
                    Thread.sleep(1000); // Aspetta 1 secondo prima di ritentare
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        System.err.println("Impossibile connettersi al server dopo 5 tentativi.");
    }

    public boolean login(String usr, String psw) {
        try {
            if (db == null) {
                System.err.println("Impossibile eseguire il login: server non disponibile.");
                return false;
            }

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
