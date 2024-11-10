package LAB_B.Client;

import LAB_B.Common.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUp extends LayoutStandard {

    private JTextField nome = new JTextField(15);
    private JTextField cognome = new JTextField(15);
    private JTextField codFiscale = new JTextField(15);
    private JTextField email = new JTextField(15);
    private JPasswordField password = new JPasswordField(15);
    private JTextField centro = new JTextField(15);

    private String res = ""; // Per memorizzare i dati raccolti
    private String err = ""; // Per memorizzare gli errori

    private Connection conn; // Connessione al database

    public SignUp() {
        super();

        // Impostazioni finestra
        setSize(500, 400);// sistema
        setLayout(new BorderLayout());
        JLabel bio = new JLabel("Registra nuovo Operatore");
        bio.setHorizontalAlignment(JLabel.CENTER);
        bio.setFont(new Font("Courier", Font.BOLD, 20));
        add(bio, BorderLayout.NORTH);

        // Aggiunta del menu "Info"
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Info");
        JMenuItem i1 = new JMenuItem("Come compilare");
        i1.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Nome e cognome in maiuscolo.\nCodice fiscale di 16 caratteri.\n" +
                        "Email valida e password con almeno 8 caratteri, inclusi maiuscola, minuscola, numero e simbolo.\n" +
                        "Nome del centro in maiuscolo."));
        menu.add(i1);
        mb.add(menu);
        setJMenuBar(mb);

        // Pannello principale con padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello centrale per i campi di input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Spaziatura tra i campi

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(nome, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cognome, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Codice Fiscale:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(codFiscale, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Centro Monitoraggio:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(centro, gbc);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Pannello per i bottoni
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton butSalva = new JButton("Salva");
        butSalva.addActionListener(e -> salvaOperatore());

        JButton butChiudi = new JButton("Torna indietro");
        butChiudi.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        buttonPanel.add(butChiudi);
        buttonPanel.add(butSalva);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Connessione al database
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/operatori_db", "tuo_utente", "tua_password");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore di connessione al database", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvaOperatore() {
        if (regOperatore()) {
            try {
                String sql = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, centro_monitoraggio) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nome.getText().toUpperCase());
                    stmt.setString(2, cognome.getText().toUpperCase());
                    stmt.setString(3, codFiscale.getText().toUpperCase());
                    stmt.setString(4, email.getText().toUpperCase());
                    stmt.setString(5, new String(password.getPassword()));
                    stmt.setString(6, centro.getText().toUpperCase());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Operatore registrato correttamente!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new Login().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Errore durante la registrazione!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore di connessione al database", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, err, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
        return email.matches(emailRegex);
    }

    private boolean isPasswordValid(String password) {
        if (password.isEmpty() || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*");

        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }

    private boolean regOperatore() {
        res = "";
        err = "";

        if (nome.getText().isEmpty()) {
            err += "Il nome non è stato inserito.\n";
        } else {
            res += nome.getText().toUpperCase() + ";";
        }

        if (cognome.getText().isEmpty()) {
            err += "Il cognome non è stato inserito.\n";
        } else {
            res += cognome.getText().toUpperCase() + ";";
        }

        if (codFiscale.getText().isEmpty() || codFiscale.getText().length() != 16) {
            err += "Il codice fiscale deve avere 16 caratteri.\n";
        } else {
            res += codFiscale.getText().toUpperCase() + ";";
        }

        if (!validateEmail(email.getText())) {
            err += "L'email non è valida.\n";
        } else {
            res += email.getText().toUpperCase() + ";";
        }

        if (!isPasswordValid(new String(password.getPassword()))) {
            err += "La password non è valida.\n";
        } else {
            res += new String(password.getPassword()) + ";";
        }

        if (centro.getText().isEmpty()) {
            err += "Il centro di monitoraggio non è stato inserito.\n";
        } else {
            res += centro.getText().toUpperCase() + ";";
        }

        return err.isEmpty();
    }
}
