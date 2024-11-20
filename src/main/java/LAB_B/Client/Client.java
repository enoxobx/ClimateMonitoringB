package LAB_B.Client;

import LAB_B.Common.Home;
import LAB_B.Database.Database;
import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private Database db;


    // Metodo per connettersi al server RMI
    public void start() {
        new Thread(() -> {
            int attempts = 0;
            while (attempts < 5) { // Tentativi limitati a 5
                try {
                    // Connessione al registro RMI sulla porta 1099
                    Registry registry = LocateRegistry.getRegistry("localhost", 1099); // Porta 1099
                    db = (Database) registry.lookup("DatabaseImpl"); // Cerca l'oggetto remoto nel registro

                    System.out.println("Connessione al server riuscita.");

                    // Se la connessione al server è riuscita, apri la finestra Home
                    SwingUtilities.invokeLater(() -> {
                        Home homeWindow = new Home();
                        homeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        homeWindow.setLocationRelativeTo(null); // Centra la finestra
                        homeWindow.setVisible(true);
                    });

                    return; // Esci dal ciclo se la connessione ha successo
                } catch (Exception e) {
                    attempts++;
                    System.err.println("Errore nella connessione al server RMI. Tentativo " + attempts + "...");
                    try {
                        Thread.sleep(2000); // Aspetta 2 secondi prima di ritentare
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            System.err.println("Impossibile connettersi al server dopo 5 tentativi.");
        }).start(); // Avvia il client in un thread separato
    }

    // Metodo per eseguire il login
    public boolean login(String usr, String psw) {
        try {
            if (db == null) {
                System.err.println("Impossibile eseguire il login: server non disponibile.");
                return false;
            }

            System.out.println("Esecuzione login per l'utente: " + usr);
            boolean isLoggedIn = db.login(usr, psw); // Chiamata al metodo remoto

            if (isLoggedIn) {
                System.out.println("Login riuscito per l'utente: " + usr);
            } else {
                System.out.println("Login fallito per l'utente: " + usr);
            }

            return isLoggedIn;
        } catch (Exception e) {
            System.err.println("Errore durante il login.");
            e.printStackTrace();
            return false;
        }
    }

    // Metodo main per avviare il client e tentare il login
    public static void main(String[] args) {
        Client client = new Client();
        client.start(); // Connessione al server RMI
    }
}
