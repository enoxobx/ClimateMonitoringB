package LAB_B.Client;

import LAB_B.Common.Home;
import LAB_B.Common.LayoutStandard;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;

public class SignUp extends LayoutStandard {

    // Dichiarazione dei campi di input
    private JTextField nome = new JTextField(15);               // Campo per il nome
    private JTextField cognome = new JTextField(15);            // Campo per il cognome
    private JTextField codFiscale = new JTextField(15);         // Campo per il codice fiscale (unico obbligatorio)
    private JTextField email = new JTextField(15);              // Campo per l'email
    private JPasswordField password = new JPasswordField(15);   // Campo per la password
    private JTextField centro = new JTextField(15);             // Campo per il centro di monitoraggio

    private StringBuilder res = new StringBuilder();
    private StringBuilder err = new StringBuilder();

    public SignUp() {
        super();  // Richiama il costruttore di LayoutStandard

        Container body = getBody();
        body.setLayout(new BorderLayout());

        // Configura i pannelli
        JLabel bio = new JLabel("Registra nuovo Operatore");
        bio.setHorizontalAlignment(JLabel.CENTER);
        bio.setFont(new Font("Courier", Font.BOLD, 20));
        body.add(bio, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        addField(inputPanel, gbc, "Nome:", nome);
        addField(inputPanel, gbc, "Cognome:", cognome);
        addField(inputPanel, gbc, "Codice Fiscale:", codFiscale);
        addField(inputPanel, gbc, "Email:", email);
        addField(inputPanel, gbc, "Password:", password);
        addField(inputPanel, gbc, "Centro Monitoraggio:", centro);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        body.add(mainPanel, BorderLayout.CENTER);

        // Pulsante "Home" aggiunto a BorderLayout.WEST
        JButton butHome = new JButton("Home");  // Pulsante Home
        butHome.addActionListener(e -> {
            new Home(); // Torna alla finestra Home
            dispose();  // Chiude la finestra corrente
        });
        body.add(butHome, BorderLayout.WEST); // Pulsante posizionato a sinistra (WEST)

        // Pannello per gli altri pulsanti ("Salva" e "Torna indietro")
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton butSalva = new JButton("Salva");
        butSalva.addActionListener(e -> salvaOperatore());

        JButton butChiudi = new JButton("Torna indietro");
        butChiudi.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        // Aggiunta dei pulsanti al pannello
        buttonPanel.add(butChiudi);
        buttonPanel.add(butSalva);
        body.add(buttonPanel, BorderLayout.SOUTH); // Pulsanti posizionati in basso (SOUTH)

        setVisible(true);  // Mostra la finestra
    }

    // Metodo per aggiungere un campo di input al pannello con il relativo label
    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Funzione per generare automaticamente l'username basato su nome, cognome e codice fiscale
    private String generateUsername(String nome, String cognome, String codFiscale) {
        String nomeFormatted = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
        String cognomeFormatted = cognome.substring(0, 1).toUpperCase() + cognome.substring(1).toLowerCase();
        String yearFromCodFiscale = codFiscale.substring(6, 8);
        return nomeFormatted + cognomeFormatted + yearFromCodFiscale;
    }

    private void salvaOperatore() {
        if (validateInputs()) {
            String codiceFiscaleInserito = codFiscale.getText().trim();
            String usernameGenerato = generateUsername(nome.getText(), cognome.getText(), codiceFiscaleInserito);

            salvaDatiNelDatabase(nome.getText(), cognome.getText(), codiceFiscaleInserito, email.getText(),
                    new String(password.getPassword()), centro.getText(), usernameGenerato);

            JOptionPane.showMessageDialog(this, "Registrazione completata con successo. \n" +
                    "Username: " + usernameGenerato + "\n" + "Password: " + new String(password.getPassword()), "Successo", JOptionPane.INFORMATION_MESSAGE);

            new Login().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, err.toString(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs() {
        res.setLength(0);
        err.setLength(0);

        validateField(nome.getText(), "Il nome non è stato inserito.", 30);
        validateField(cognome.getText(), "Il cognome non è stato inserito.", 30);
        validateField(codFiscale.getText(), "Il codice fiscale deve avere 16 caratteri.", 16);
        validateEmail(email.getText());
        validatePassword(new String(password.getPassword()));
        validateField(centro.getText(), "Il centro di monitoraggio non è stato inserito.", 50);

        return err.length() == 0;
    }

    private void validateField(String value, String errorMessage, int maxLength) {
        if (value.isEmpty() || value.length() > maxLength) {
            err.append(errorMessage).append("\n");
        } else {
            res.append(value.toUpperCase()).append("; ");
        }
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, email)) {
            err.append("L'email non è valida.\n");
        } else {
            res.append(email.toUpperCase()).append("; ");
        }
    }

    private void validatePassword(String password) {
        if (password.isEmpty() || password.length() < 8) {
            err.append("La password deve avere almeno 8 caratteri.\n");
        } else if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") ||
                !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            err.append("La password deve contenere almeno una maiuscola, una minuscola, un numero e un simbolo.\n");
        } else {
            res.append(password).append("; ");
        }
    }

    private void salvaDatiNelDatabase(String nome, String cognome, String codiceFiscale, String email, String password, String centro, String username) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/climate_monitoring", "postgres", "0000");
            String sql = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, centro_monitoraggio, username) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, codiceFiscale);
            ps.setString(4, email);
            ps.setString(5, password);
            ps.setString(6, centro);
            ps.setString(7, username);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio dei dati nel database.", "Errore", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
