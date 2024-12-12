package LAB_B.Operatore;

import LAB_B.Client.Client;
import LAB_B.Common.Home;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

public class Login extends JFrame {

    private JTextField usernameOrCodiceFiscaleField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton homeButton;

    private static final int MAX_RETRIES = 3;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public Login() {
        loadDatabaseConfig(); // Carica la configurazione dal file di proprietà
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 350);
        setResizable(false);

        // Layout principale con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Pulsante "Home" posizionato a sinistra
        homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(70, 0));
        mainPanel.add(homeButton, BorderLayout.WEST);

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
        registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Configura le azioni dei bottoni
        configureButtons();

        add(mainPanel);
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
                    //TODO da sistemare
                    //Client client = new Client();
                    //boolean success = client.login(usernameOrCodiceFiscale, password);

                    // Verifica se il login è riuscito
                    /*if (success) {
                        JOptionPane.showMessageDialog(null, "Login effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        // Passa l'username come argomento a LayoutOperatore
                        new LayoutOperatore(usernameOrCodiceFiscale); // Apre il layout dell'operatore passando l'username
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Username, codice fiscale o password errati!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }*/
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Apre la finestra di registrazione
                SignUp signUpWindow = new SignUp();
                dispose();
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home(); // Crea la finestra Home
                dispose();  // Chiude la finestra corrente
            }
        });
    }

    public static void main(String[] args) {
        new Login();
    }
}
