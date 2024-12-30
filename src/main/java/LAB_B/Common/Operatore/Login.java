package LAB_B.Common.Operatore;

import LAB_B.Client.Client;
import LAB_B.Common.LayoutStandard;
import LAB_B.Common.Home;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Properties;

public class Login extends LayoutStandard {

    private JTextField usernameOrCodiceFiscaleField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private static final int MAX_RETRIES = 3;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public Login() {
        super(); // Richiama il costruttore di LayoutStandard

        loadDatabaseConfig(); // Carica la configurazione dal file di proprietà
        setTitle("Login");

        // Pannello centrale per i campi di input e i bottoni
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        centerPanel.setBackground(new Color(240, 248, 255));

        usernameOrCodiceFiscaleField = new JTextField(10);
        usernameOrCodiceFiscaleField.setBorder(BorderFactory.createTitledBorder("Username o Codice Fiscale"));
        usernameOrCodiceFiscaleField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(usernameOrCodiceFiscaleField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        passwordField = new JPasswordField(10);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(passwordField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(240, 248, 255));

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(60, 179, 113)); // Verde moderno
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(100, 149, 237)); // Blu chiaro
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        centerPanel.add(buttonPanel);

        // Aggiungi il pannello centrale al corpo ereditato da LayoutStandard
        getBody().add(centerPanel, BorderLayout.CENTER);

        // Configura le azioni dei bottoni
        configureButtons();

        // Imposta il bottone Home, se vuoi cambiarne l'azione
        home.addActionListener(e -> {
            // Puoi aggiungere il comportamento del bottone Home
            // Ad esempio, qui puoi chiudere la finestra di login e aprire la home
            new Home().setVisible(true);
            dispose(); // Chiudi la finestra di login
        });

        setVisible(true);
    }

    private void loadDatabaseConfig() {
        // Carica la configurazione dal file config.properties
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            dbUrl = properties.getProperty("db.url");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento del file di configurazione: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configureButtons() {
        loginButton.addActionListener(e -> {
            String usernameOrCodiceFiscale = usernameOrCodiceFiscaleField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (usernameOrCodiceFiscale.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci uno username o un codice fiscale!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Il campo password non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                // Crea l'istanza di Client e verifica le credenziali
                Client client = new Client(dbUrl, dbUsername, dbPassword); // Passa i dettagli del database
                boolean success = client.login(usernameOrCodiceFiscale, password);

                // Verifica se il login è riuscito
                if (success) {
                    JOptionPane.showMessageDialog(this, "Login effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    new LayoutOperatore(usernameOrCodiceFiscale); // Apre il layout dell'operatore passando l'username
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Username, codice fiscale o password errati!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(e -> {
            // Apre la finestra di registrazione
            SignUp signUpWindow = new SignUp();
            dispose();
        });
    }

}
