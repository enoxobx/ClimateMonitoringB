package LAB_B;

import LAB_B.Client.Client;
import LAB_B.Common.Home;
import LAB_B.Database.Server;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Avvia il server in un thread separato
        Thread serverThread = new Thread(() -> {
            System.out.println("Avvio del server...");
            new Server().start();  // Avvia il server
        });

        // Avvia il thread del server
        serverThread.start();

        // Aggiungi un ritardo per dare il tempo al server di avviarsi
        try {
            Thread.sleep(5000); // Aspetta 5 secondi (o il tempo che preferisci)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Avvia il client subito dopo
        System.out.println("Avvio del client...");
        Client client = new Client();
        client.start();

        // Esegui il login per esempio
        boolean loggedIn = client.login("user", "password");
        System.out.println("Login eseguito: " + loggedIn);

        // Mostra la GUI (facoltativo, puoi anche farlo separatamente)
        SwingUtilities.invokeLater(() -> {
            Home home = new Home();
            home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            home.setLocationRelativeTo(null);
            home.setVisible(true);
        });
    }
}
