package LAB_B;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class TestLogin {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/climate_monitoring";
        String user = "postgres";
        String password = "0000"; // Usa la password che hai configurato

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connessione al database riuscita!");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nBenvenuto nell'applicazione!");
                System.out.println("1. Operatore");
                System.out.println("2. Cittadino (non implementato)");
                System.out.println("0. Esci");
                System.out.print("Scegli un'opzione: ");
                int scelta = scanner.nextInt();
                scanner.nextLine(); // Consuma la nuova riga

                switch (scelta) {
                    case 1:
                        gestisciOperatore(conn, scanner);
                        break;
                    case 2:
                        System.out.println("Funzionalità per i cittadini non ancora implementata.");
                        break;
                    case 0:
                        System.out.println("Chiusura dell'applicazione. Arrivederci!");
                        return;
                    default:
                        System.out.println("Scelta non valida. Riprova.");
                }
            }
        } catch (Exception e) {
            System.out.println("Errore durante la connessione al database:");
            e.printStackTrace();
        }
    }

    public static void gestisciOperatore(Connection conn, Scanner scanner) {
        System.out.println("\nSei un operatore. Cosa vuoi fare?");
        System.out.println("1. Registrati");
        System.out.println("2. Login");
        System.out.print("Scegli un'opzione: ");
        int scelta = scanner.nextInt();
        scanner.nextLine(); // Consuma la nuova riga

        switch (scelta) {
            case 1:
                registraOperatore(conn, scanner);
                break;
            case 2:
                loginOperatore(conn, scanner);
                break;
            default:
                System.out.println("Scelta non valida. Torno al menu principale.");
        }
    }

    public static void registraOperatore(Connection conn, Scanner scanner) {
        System.out.println("\nRegistrazione di un nuovo operatore.");
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Cognome: ");
            String cognome = scanner.nextLine();
            System.out.print("Codice Fiscale: ");
            String codiceFiscale = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Centro Monitoraggio: ");
            String centro = scanner.nextLine();

            String insertQuery = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, username, centro_monitoraggio) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, nome);
                stmt.setString(2, cognome);
                stmt.setString(3, codiceFiscale);
                stmt.setString(4, email);
                stmt.setString(5, password);  // Salvataggio della password in chiaro (sconsigliato in produzione)
                stmt.setString(6, centro);
                stmt.executeUpdate();
                System.out.println("Registrazione completata con successo!");
            }
        } catch (Exception e) {
            System.out.println("Errore durante la registrazione:");
            e.printStackTrace();
        }
    }

    public static void loginOperatore(Connection conn, Scanner scanner) {
        System.out.println("\nLogin operatore.");
        try {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            String query = "SELECT password FROM operatori WHERE mail = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        if (storedPassword.equals(password)) {
                            System.out.println("Login riuscito! Benvenuto!");
                        } else {
                            System.out.println("Password errata.");
                        }
                    } else {
                        System.out.println("Email non trovata.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Errore durante il login:");
            e.printStackTrace();
        }
    }
}
