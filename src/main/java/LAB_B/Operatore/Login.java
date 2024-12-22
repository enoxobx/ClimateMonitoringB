package LAB_B.Operatore;

import LAB_B.Client.Client;
import LAB_B.Common.Home;
import LAB_B.Common.LayoutStandard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

public class Login extends LayoutStandard {  // Estensione da LayoutStandard per condividere il layout e la configurazione del DB

    private final JTextField usernameOrCodiceFiscaleField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    // Costruttore
    public Login() {
        super(); // Inizializza la classe LayoutStandard (es. carica DB e altre risorse)
        loadDatabaseConfig(); // Carica la configurazione dal file di proprietà
        setTitle("Login");
        setLocationRelativeTo(null);
        setSize(400, 350);
        setResizable(false);

        // Layout principale con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Pannello centrale per i campi di input e i bottoni
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameOrCodiceFiscaleField = new JTextField(20);
        usernameOrCodiceFiscaleField.setBorder(BorderFactory.createTitledBorder("Username o Codice Fiscale"));
        centerPanel.add(usernameOrCodiceFiscaleField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        centerPanel.add(passwordField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new JButton("Login");
        registerButton = new JButton("Registrazione");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Configura le azioni dei bottoni
        configureButtons();

        add(mainPanel);
        setVisible(true);
    }

    // Carica la configurazione del database dal file properties
    private void loadDatabaseConfig() {
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

    // Configura le azioni dei bottoni (login, registrazione)
    private void configureButtons() {
        // Azione del pulsante Login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameOrCodiceFiscale = usernameOrCodiceFiscaleField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (usernameOrCodiceFiscale.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci uno username o un codice fiscale!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Il campo password non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Verifica le credenziali con il client
                    Client client = new Client(dbUrl, dbUsername, dbPassword);
                    boolean success = client.login(usernameOrCodiceFiscale, password);

                    // Verifica il login
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Login effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        new LayoutOperatore(usernameOrCodiceFiscale); // Passa l'username come parametro
                        dispose(); // Chiude la finestra di login
                    } else {
                        JOptionPane.showMessageDialog(null, "Username, codice fiscale o password errati!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Azione del pulsante Registrazione
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new SignUp();  // Apre la finestra di registrazione
                dispose(); // Chiude la finestra di login
            }
        });

        // Il bottone "Home" viene già gestito da LayoutStandard, quindi non è necessario aggiungere altre azioni
    }

    public static void main(String[] args) {
        new Login();  // Avvia l'applicazione di login
    }
}
