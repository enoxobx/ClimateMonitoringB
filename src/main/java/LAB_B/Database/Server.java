package LAB_B.Database;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import LAB_B.Common.Config;

public class Server extends Thread {

    private Registry registry;

    // Costruttore che avvia il server
    public Server() throws Exception {

        this.start();

    }

    // Metodo per avviare il server RMI
    @Override
    public void run() {
        try {
            Config cf = new Config();
            // Crea un registro RMI sulla porta 1099
            registry = LocateRegistry.createRegistry(cf.getRmiPort());

            // Crea un oggetto DatabaseImpl per esporlo come servizio RMI
            DatabaseImpl obj = new DatabaseImpl();

            // Rende l'oggetto DatabaseImpl disponibile come servizio RMI
            System.out.println("Registrazione dell'oggetto DatabaseService...");
            Naming.rebind(cf.getRmiDbName(), obj);

            System.out.println("Oggetto RMI creato e registrato");
            System.out.println("Server RMI avviato.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per fermare il server RMI e liberare le risorse
    public void stopServer() {
        try {
            if (registry != null) {
                // Unbind del servizio e liberazione delle risorse RMI
                Config cf = new Config();
                registry.unbind(cf.getRmiDbName());
                System.out.println("Server RMI fermato.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
