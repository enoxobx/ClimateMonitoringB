package LAB_B.Database;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private Registry registry;

    public void start() {
        try {
            // Tentativo di creare il registro RMI sulla porta 11111
            System.out.println("Tentativo di creare il registro RMI sulla porta 11111...");
            try {
                registry = LocateRegistry.createRegistry(11111); // Nuova porta 11111
                System.out.println("Registro RMI creato.");
            } catch (RemoteException e) {
                System.err.println("Errore nella creazione del registro RMI.");
                e.printStackTrace();
                return; // Esci se non è possibile creare il registro
            }

            // Creazione dell'oggetto remoto
            System.out.println("Creazione dell'oggetto remoto...");
            Database db = new DatabaseImpl();

            // Binding dell'oggetto al registro
            try {
                System.out.println("Binding dell'oggetto al registro RMI...");
                registry.rebind("DatabaseImpl", db);
                System.out.println("Oggetto remoto registrato con successo.");
            } catch (RemoteException e) {
                System.err.println("Errore nel binding dell'oggetto remoto.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Errore generale durante l'avvio del server RMI.");
            e.printStackTrace();
        }
    }
}