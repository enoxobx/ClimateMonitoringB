package LAB_B.Database;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private Registry registry;

    public void start() {
        try {
            System.out.println("Avvio del server...");

            System.out.println("Tentativo di creare il registro RMI sulla porta 12345...");
            try {
                registry = LocateRegistry.createRegistry(12345); // Crea il registro RMI
                System.out.println("Registro RMI creato.");
            } catch (RemoteException e) {
                registry = LocateRegistry.getRegistry(12345); // Se il registro è già attivo, lo otteniamo
                System.out.println("Registro RMI esistente trovato.");
            }

            // Creazione dell'oggetto remoto
            Database db = new DatabaseImpl();
            registry.rebind("DatabaseImpl", db); // Registro l'oggetto RMI
            System.out.println("Oggetto remoto registrato con successo.");
        } catch (Exception e) {
            System.err.println("Errore durante l'avvio del server RMI.");
            e.printStackTrace();
        }
    }
}
