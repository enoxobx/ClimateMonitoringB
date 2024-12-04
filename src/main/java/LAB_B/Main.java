package LAB_B;

import LAB_B.Common.Home;
import LAB_B.Database.DatabaseManager;
import LAB_B.Database.Server;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Avvia il server in un thread separato
            Thread serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    startServer();  // Avvia il server
                }
            });

            // Avvia il thread del server
            serverThread.start();

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
            DatabaseManager.closeConnection();
        }
    }

    // Metodo per avviare il server
    private static void startServer() {
        try {
            // Avvia il server senza ripetere il test di connessione al database
            new Server().start();  // Avvia il server
        } catch (Exception e) {
            // Se si verifica un errore durante l'avvio del server, stampa l'errore
            System.err.println("Errore durante l'avvio del server: ");
            e.printStackTrace();
        }
    }
}
