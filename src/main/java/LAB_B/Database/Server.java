package LAB_B.Database;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private Registry registry;

    // Metodo per avviare il server
    public void start() {
        try {
            System.out.println("Avvio del server...");

            // Tentativo di creare un nuovo registro RMI sulla porta 1099
            try {
                registry = LocateRegistry.createRegistry(1099); // Porta 1099
                System.out.println("Registro RMI creato.");
            } catch (RemoteException e) {
                registry = LocateRegistry.getRegistry(1099); // Se il registro è già attivo
                System.out.println("Registro RMI esistente trovato.");
            }

            // Creazione e registrazione dell'oggetto remoto
            Database db = new DatabaseImpl();  // Creazione dell'oggetto remoto
            registry.rebind("DatabaseImpl", db);  // Registriamo l'oggetto remoto nel registro RMI
            System.out.println("Oggetto remoto registrato con successo.");

        } catch (Exception e) {
            System.err.println("Errore durante l'avvio del server RMI.");
            e.printStackTrace();
        }
    }

    // Metodo main per avviare il server
    public static void main(String[] args) {
        Server server = new Server();
        server.start();  // Avvio del server
    }
}
