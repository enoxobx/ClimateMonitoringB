package LAB_B.Database;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

public class Server extends Thread{

    public Server(){
        this.run();
    }

    // Metodo per avviare il server RMI
    public void run() {
        try {
            // Crea un registro RMI sulla porta 1099
            LocateRegistry.createRegistry(1099);

            DatabaseImpl obj = new DatabaseImpl();

            // Rende l'oggetto DatabaseImpl disponibile come servizio RMI
            System.out.println("Registrazione dell'oggetto DatabaseService...");
            Naming.rebind("Server", obj);

            System.out.println("oggetto RMI creato e registrato");

            // Stampa un messaggio di conferma che il server è stato avviato
            System.out.println("Server avviato.");
        } catch (Exception e) {
            // Se si verifica un errore durante l'avvio del server, stampa l'errore
            e.printStackTrace();
        }
    }

    
}
