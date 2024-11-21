package LAB_B.Client;

import javax.swing.*;  // Importa le librerie per la gestione dell'interfaccia grafica
import java.awt.*;     // Importa le librerie per la gestione dei layout e delle dimensioni
import java.awt.event.ActionEvent;   // Importa la libreria per gli eventi dei pulsanti
import java.awt.event.ActionListener; // Importa la libreria per i listener degli eventi

public class Login extends JFrame {

    private JTextField usernameOrCodiceFiscaleField; // Campo per inserire username o codice fiscale
    private JPasswordField passwordField; // Campo per inserire la password
    private JButton loginButton; // Bottone per il login
    private JButton registerButton; // Bottone per la registrazione

    // Parametri di connessione al database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/climate_monitoring?ssl=false&connectTimeout=5000&socketTimeout=5000";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "0000";
    private static final int MAX_RETRIES = 3;

    // Costruttore della classe Login
    public Login() {
        // Impostazioni della finestra (frame)
        setTitle("Login");  // Imposta il titolo della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Imposta la chiusura della finestra
        setLocationRelativeTo(null);  // Posiziona la finestra al centro dello schermo
        setSize(400, 350);  // Imposta la dimensione della finestra
        setResizable(false);  // Disabilita il ridimensionamento della finestra

        // Pannello principale con BoxLayout per allineare gli elementi verticalmente
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));  // Layout verticale
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Aggiunge dei margini intorno al pannello

        // Campo per inserire username o codice fiscale
        usernameOrCodiceFiscaleField = new JTextField(20);  // Crea un campo di testo con una larghezza di 20 caratteri
        usernameOrCodiceFiscaleField.setBorder(BorderFactory.createTitledBorder("Username o Codice Fiscale"));  // Aggiunge un bordo con titolo
        mainPanel.add(usernameOrCodiceFiscaleField);  // Aggiunge il campo al pannello principale
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));  // Aggiunge uno spazio verticale tra gli elementi

        // Campo per la password
        passwordField = new JPasswordField(20);  // Crea un campo per la password con una larghezza di 20 caratteri
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));  // Aggiunge un bordo con titolo
        mainPanel.add(passwordField);  // Aggiunge il campo al pannello principale
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Aggiunge uno spazio verticale tra gli elementi

        // Pannello per i pulsanti (login e registrazione)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));  // Layout orizzontale per i pulsanti
        loginButton = new JButton("Login");  // Crea il bottone per il login
        registerButton = new JButton("Register");  // Crea il bottone per la registrazione
        buttonPanel.add(loginButton);  // Aggiunge il bottone login al pannello dei pulsanti
        buttonPanel.add(registerButton);  // Aggiunge il bottone register al pannello dei pulsanti

        // Aggiunge il pannello dei bottoni al pannello principale
        mainPanel.add(buttonPanel);

        // Configura le azioni dei bottoni
        configureButtons();

        // Aggiunge il pannello principale alla finestra
        add(mainPanel);
        setVisible(true);  // Rende la finestra visibile
    }

    // Metodo per configurare le azioni dei bottoni
    private void configureButtons() {
        // Azione per il bottone login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottiene i valori inseriti nei campi di testo
                String usernameOrCodiceFiscale = usernameOrCodiceFiscaleField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();  // Converte la password in stringa

                // Verifica se i campi sono vuoti
                if (usernameOrCodiceFiscale.isEmpty()) {
                    // Se il campo username/codice fiscale è vuoto, mostra un messaggio di errore
                    JOptionPane.showMessageDialog(null, "Inserisci uno username o un codice fiscale!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else if (password.isEmpty()) {
                    // Se il campo password è vuoto, mostra un messaggio di errore
                    JOptionPane.showMessageDialog(null, "Il campo password non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Crea un oggetto Client per il login e passa i parametri di connessione al database
                    Client client = new Client(DB_URL, DB_USERNAME, DB_PASSWORD, MAX_RETRIES);

                    // Verifica il login con username o codice fiscale
                    boolean success = client.login(usernameOrCodiceFiscale, password);

                    // Se il login è riuscito, mostra un messaggio di successo
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Login effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

                        // Apre la finestra di registrazione centro aree
                        new RegistraCentroAree();  // Mostra la finestra di registrazione
                        dispose();  // Chiudi la finestra di login
                    } else {
                        // Se il login fallisce, mostra un messaggio di errore
                        JOptionPane.showMessageDialog(null, "Username, codice fiscale o password errati!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Azione per il bottone registrazione
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Quando l'utente preme il bottone di registrazione, viene aperta la finestra di registrazione
                SignUp signUpWindow = new SignUp();  // Crea una nuova finestra di registrazione
                dispose();  // Chiude la finestra di login
            }
        });
    }

    // Main per eseguire il login
    public static void main(String[] args) {
        new Login();  // Avvia la finestra di login
    }
}
