package LAB_B;

import LAB_B.Database.Server;  // Importa la classe Server che gestisce la logica del server
import LAB_B.Client.Client;    // Importa la classe Client per gestire la logica del client
import LAB_B.Common.Home;      // Importa la classe Home per la finestra principale dell'interfaccia utente

import javax.swing.*;  // Importa le librerie per la gestione delle interfacce grafiche (Swing)

public class Main {
    public static void main(String[] args) {
        try {
            // Avvia il server in un thread separato
            // Un nuovo thread è creato per eseguire il server senza bloccare l'esecuzione principale
            Thread serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    startServer();  // Chiama il metodo startServer() che avvia il server
                }
            });

            // Avvia il thread del server
            serverThread.start();  // Il server inizia ad essere eseguito nel suo thread separato

            // Avvia la finestra principale Home (la schermata di scelta tra cittadino e operatore)
            // SwingUtilities.invokeLater assicura che la GUI venga aggiornata nel thread dell'interfaccia utente
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Home();  // Crea e visualizza la finestra principale
                }
            });

            // Attendere che il thread del server finisca (l'app può continuare mentre il server è in esecuzione)
            // serverThread.join() permette di far attendere il thread principale (main) fino alla fine del server
            serverThread.join();

        } catch (Exception e) {
            // In caso di errore durante l'esecuzione, stampiamo l'eccezione
            e.printStackTrace();
        }
    }

    // Metodo per avviare il server
    // Questa funzione crea un oggetto Server e avvia il suo processo
    private static void startServer() {
        try {
            new Server().start();  // Avvia il server chiamando il metodo start della classe Server
        } catch (Exception e) {
            // Se si verifica un errore durante l'avvio del server, stampa l'errore
            System.err.println("Errore durante l'avvio del server: ");
            e.printStackTrace();
        }
    }
}
