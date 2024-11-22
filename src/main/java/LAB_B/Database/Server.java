package LAB_B.Database;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

    // Metodo per avviare il server RMI
    public void start() {
        try {
            // Crea un registro RMI sulla porta 1099
            LocateRegistry.createRegistry(1099);

            // Rende l'oggetto DatabaseImpl disponibile come servizio RMI
            Naming.rebind("rmi://localhost/DatabaseService", new DatabaseImpl());

            // Stampa un messaggio di conferma che il server è stato avviato
            System.out.println("Server avviato.");
        } catch (Exception e) {
            // Se si verifica un errore durante l'avvio del server, stampa l'errore
            e.printStackTrace();
        }
    }

    // Metodo principale per avviare il server
    public static void main(String[] args) {
        // Crea un'istanza di Server e avvia il server RMI
        new Server().start();
    }
}
