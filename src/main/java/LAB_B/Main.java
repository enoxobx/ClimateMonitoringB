package LAB_B;

import LAB_B.Common.Home;
import LAB_B.Database.DatabaseImpl;
import LAB_B.Database.Server;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Avvia il server in un thread separato
        Server serverThread;
        try {
            serverThread = new Server();


            try {
                // Avvia la finestra principale Home
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new Home();  // Crea e visualizza la finestra principale
                    }
                });
    
                // Attendere che il thread del server finisca
                serverThread.join();
    
            } catch (Exception e) {
                // Gestione delle eccezioni generali
                e.printStackTrace();
            }
    
            // Aggiungi un hook per chiudere la connessione al database e fermare il server quando l'applicazione termina
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Chiusura dell'applicazione...");
                    DatabaseImpl.closeConnection();  // Chiude la connessione al database
                    serverThread.stopServer();  // Ferma il server RMI
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }
}
