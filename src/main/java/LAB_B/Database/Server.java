package LAB_B.Database;

import javax.swing.JOptionPane;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private Registry registry;

    public void start() {
        try {
            System.out.println("Avvio del server...");

            try {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("Registro RMI creato.");
            } catch (RemoteException e) {
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("Registro RMI esistente trovato.");
            }

            Database db = new DatabaseImpl();
            registry.rebind("DatabaseImpl", db);
            System.out.println("Oggetto remoto registrato con successo.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore durante l'avvio del server: " + e.getMessage(), "Errore Server", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
