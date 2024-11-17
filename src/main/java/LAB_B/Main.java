package LAB_B;

import LAB_B.Client.Client;
import LAB_B.Common.Home;
import LAB_B.Database.Server;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("server")) {
                // Avvia il server
                new Server().start();
            } else if (args[0].equalsIgnoreCase("client")) {
                // Avvia il client
                Client client = new Client();
                client.start();

                // Esempio di login client
                boolean loggedIn = client.login("user", "password");
                System.out.println("Login eseguito: " + loggedIn);
            } else {
                System.err.println("Argomento non valido. Usa 'server' o 'client'.");
            }
        } else {
            // Default: avvia l'interfaccia principale
            SwingUtilities.invokeLater(() -> {
                Home home = new Home();
                home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                home.setLocationRelativeTo(null);
                home.setVisible(true);
            });
        }
    }
}
