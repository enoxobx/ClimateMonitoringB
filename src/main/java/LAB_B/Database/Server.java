package LAB_B.Database;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public void start() {
        try {
            // Crea o ottieni il registro RMI
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("Registro RMI creato.");
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("Registro RMI già esistente, connessione stabilita.");
            }

            // Creazione dell'oggetto remoto
            Database db = new DatabaseImpl();

            // Binding dell'oggetto al registro RMI
            registry.rebind("DatabaseImpl", db);

            System.out.println("Server avviato. Database disponibile.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
