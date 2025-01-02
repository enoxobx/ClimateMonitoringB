package LAB_B.Common.Operatore;

import LAB_B.Client.Client;
import LAB_B.Common.LayoutStandard;
import LAB_B.Common.Home;
import LAB_B.Database.QueryExecutorImpl;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Properties;

public class Login extends LayoutStandard {

    private static final int MAX_RETRIES = 3;
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color BUTTON_LOGIN_COLOR = new Color(60, 179, 113);
    private static final Color BUTTON_REGISTER_COLOR = new Color(100, 149, 237);
    private static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private int loginAttempts = 0;

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public Login() {
        super(); // Richiama il costruttore di LayoutStandard

        loadDatabaseConfig();
        setTitle("Login");

        setupUI();
        configureButtons();

        setVisible(true);
    }

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

    private void setupUI() {
        // Pannello centrale per input e bottoni
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        centerPanel.setBackground(BACKGROUND_COLOR);

        usernameField = createTextField("Username");
        centerPanel.add(usernameField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        passwordField = createPasswordField("Password");
        centerPanel.add(passwordField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = createButtonPanel();
        centerPanel.add(buttonPanel);

        getBody().add(centerPanel, BorderLayout.CENTER);
    }

    private JTextField createTextField(String title) {
        JTextField field = new JTextField(10);
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setFont(FIELD_FONT);
        return field;
    }

    private JPasswordField createPasswordField(String title) {
        JPasswordField field = new JPasswordField(10);
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setFont(FIELD_FONT);
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        loginButton = createButton("Login", BUTTON_LOGIN_COLOR);
        registerButton = createButton("Register", BUTTON_REGISTER_COLOR);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        return buttonPanel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        return button;
    }

    private void configureButtons() {
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegistration());
        home.addActionListener(e -> navigateToHome());
    }

    private void handleLogin() {
        String usernameOrCodiceFiscale = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (usernameOrCodiceFiscale.isEmpty()) {
            showErrorMessage("Inserisci uno username o codice fiscale!");
        } else if (password.isEmpty()) {
            showErrorMessage("Il campo password non può essere vuoto!");
        } else {
            performLogin(usernameOrCodiceFiscale, password);
        }
    }

    private void performLogin(String username, String password) {
        try {
            QueryExecutorImpl queryExecutor = new QueryExecutorImpl();
            boolean success = queryExecutor.login(username, password);

            if (success) {
                showSuccessMessage("Login effettuato con successo!");
                new LayoutOperatore(username);
                dispose();
            } else {
                loginAttempts++;
                if (loginAttempts >= MAX_RETRIES) {
                    showErrorMessage("Troppi tentativi falliti. Contatta l'amministratore.");
                    loginButton.setEnabled(false);
                } else {
                    showErrorMessage("Username, codice fiscale o password errati!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorMessage("Si è verificato un errore durante il login: " + ex.getMessage());
        }
    }

    private void handleRegistration() {
        new SignUp();
        dispose();
    }

    private void navigateToHome() {
        new Home().setVisible(true);
        dispose();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }
}
