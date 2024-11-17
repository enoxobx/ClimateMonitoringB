package LAB_B.Client;

import LAB_B.Common.LayoutStandard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends LayoutStandard {

    private JTextField usernameField; // Campo per inserire lo username
    private JPasswordField passwordField; // Campo per inserire la password
    private JButton loginButton; // Bottone per il login
    private JButton registerButton; // Bottone per la registrazione

    public Login() {
        super();
        setTitle("Login"); // Imposta il titolo della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Imposta la chiusura della finestra
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setSize(400, 300); // Imposta la dimensione della finestra
        setResizable(false); // Impedisce il ridimensionamento della finestra

        // Pannello principale con BoxLayout per allineare gli elementi verticalmente
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Organizza gli elementi verticalmente
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Aggiunge margini attorno al pannello

        // Campo per lo username
        usernameField = new JTextField(20); // Imposta la larghezza del campo a 20 caratteri
        usernameField.setBorder(BorderFactory.createTitledBorder("Username")); // Aggiunge il bordo con il titolo "Username"
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Imposta la larghezza massima del campo
        mainPanel.add(usernameField); // Aggiunge il campo al pannello principale
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Aggiunge uno spazio verticale tra gli elementi

        // Campo per la password
        passwordField = new JPasswordField(20); // Imposta la larghezza del campo a 20 caratteri
        passwordField.setBorder(BorderFactory.createTitledBorder("Password")); // Aggiunge il bordo con il titolo "Password"
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Imposta la larghezza massima del campo
        mainPanel.add(passwordField); // Aggiunge il campo al pannello principale
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Aggiunge uno spazio verticale tra gli elementi

        // Pannello per i pulsanti (login e registrazione) con FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // I bottoni sono allineati al centro
        loginButton = new JButton("Login"); // Crea il bottone di login
        registerButton = new JButton("Register"); // Crea il bottone di registrazione
        buttonPanel.add(loginButton); // Aggiunge il bottone di login
        buttonPanel.add(registerButton); // Aggiunge il bottone di registrazione

        // Aggiunge il pannello dei bottoni al pannello principale
        mainPanel.add(buttonPanel);

        // Configura le azioni per i bottoni
        configureButtons();

        // Aggiunge il pannello principale alla finestra
        add(mainPanel);
        setVisible(true); // Rende visibile la finestra
    }

    private void configureButtons() {
        // Azione per il bottone login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim(); // Prende lo username inserito
                String password = new String(passwordField.getPassword()).trim(); // Prende la password inserita

                // Verifica se i campi sono vuoti
                if (username.isEmpty() && password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Entrambi i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Il campo username non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Il campo password non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Simuliamo la funzionalità di login
                    Client client = new Client(); // Crea un oggetto Client per eseguire il login

                    // Verifica se l'username e la password sono corretti
                    boolean success = client.login(username, password);  // Verifica il login con il client (funzione simulata)

                    // Se il login ha avuto successo
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Login effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        // Procedi con l'apertura della finestra successiva (ad esempio la schermata principale dell'app)
                    } else {
                        // Mostra un messaggio di errore in caso di credenziali errate
                        JOptionPane.showMessageDialog(null, "Username o password errati!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Azione per il bottone registrazione
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Quando l'utente preme il bottone di registrazione, viene aperta la finestra di registrazione
                SignUp sign = new SignUp(); // Crea una nuova finestra di registrazione
                dispose(); // Chiude la finestra di login
            }
        });
    }
}
