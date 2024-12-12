package LAB_B;

import LAB_B.Common.Home;
import LAB_B.Database.DatabaseImpl;
import LAB_B.Database.DatabaseManager;
import LAB_B.Database.Server;

import javax.swing.*;



public class Main {
    public static void main(String[] args) {
        try {
            // Avvia il server in un thread separato
            Server serverThread = new Server();


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
        } finally {
            // Chiusura della connessione al database
            DatabaseImpl.closeConnection();
        }
    }

}
