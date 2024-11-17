package LAB_B.Client;

import LAB_B.Database.Database;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Database db;

    public void start() {
        try {
            // Connessione al registro RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Ricerca dell'oggetto remoto
            db = (Database) registry.lookup("DatabaseImpl");

            System.out.println("Connessione al server riuscita.");
        } catch (Exception e) {
            System.err.println("Errore nella connessione al server. Assicurarsi che il server sia attivo.");
            e.printStackTrace();
        }
    }

    public boolean login(String usr, String psw) {
        try {
            // Verifica se la connessione al database è attiva
            if (db == null) {
                // Se il database non è disponibile, mostra un errore nel terminale
                System.err.println("Impossibile eseguire il login: server non disponibile.");
                return false; // Ritorna false indicando che il login non può essere eseguito
            }

            // Se la connessione al database è attiva, chiama il metodo login per verificare le credenziali
            return db.login(usr, psw);

        } catch (Exception e) {
            // In caso di eccezione (errore durante il login), mostra un messaggio di errore
            System.err.println("Errore durante il login.");
            // Stampa lo stack trace dell'eccezione per il debug
            e.printStackTrace();
            return false; // Ritorna false indicando che il login non è riuscito
        }
    }

}
