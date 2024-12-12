package LAB_B.Operatore;

import LAB_B.Common.Home;
import LAB_B.Common.LayoutStandard;
import LAB_B.Database.Database;
import LAB_B.Database.DatabaseImpl;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class SignUp extends LayoutStandard {

    // Campi di input
    private JTextField nome = new JTextField(15);
    private JTextField cognome = new JTextField(15);
    private JTextField codFiscale = new JTextField(15);
    private JTextField email = new JTextField(15);
    private JPasswordField password = new JPasswordField(15);
    private JTextField centro = new JTextField(15);

    private StringBuilder res = new StringBuilder();
    private StringBuilder err = new StringBuilder();

    private Database database;

    public SignUp() {
        super();

        try {
            
            database = new DatabaseImpl();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore di connessione al database: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
            dispose(); // Chiude la finestra se c'è un errore nel database
            return;
        }

        setupUI();
    }

    // Metodo per configurare l'interfaccia utente
    private void setupUI() {
        Container body = getBody();
        body.setLayout(new BorderLayout());

        // Intestazione
        JLabel bio = new JLabel("Registra nuovo Operatore");
        bio.setHorizontalAlignment(JLabel.CENTER);
        bio.setFont(new Font("Courier", Font.BOLD, 20));
        body.add(bio, BorderLayout.NORTH);

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Aggiunge i campi di input
        addField(inputPanel, gbc, "Nome:", nome);
        addField(inputPanel, gbc, "Cognome:", cognome);
        addField(inputPanel, gbc, "Codice Fiscale:", codFiscale);
        addField(inputPanel, gbc, "Email:", email);
        addField(inputPanel, gbc, "Password:", password);
        addField(inputPanel, gbc, "Centro Monitoraggio:", centro);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        body.add(mainPanel, BorderLayout.CENTER);

        // Pulsante Home
        JButton butHome = new JButton("Home");
        butHome.addActionListener(e -> {
            new Home();
            dispose();
        });
        body.add(butHome, BorderLayout.WEST);

        // Pannello dei pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton butSalva = new JButton("Salva");
        butSalva.addActionListener(e -> salvaOperatore());

        JButton butChiudi = new JButton("Torna indietro");
        butChiudi.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        buttonPanel.add(butChiudi);
        buttonPanel.add(butSalva);
        body.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Metodo per aggiungere i campi di input
    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Validazione dei campi di input
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
        try {
            checkPassword(password);
            res.append(password).append("; ");
        } catch (Exception e) {
            err.append(e.getMessage()).append("\n");
        }
    }

    private boolean checkPassword(String password) throws Exception {
        Pattern PASSWORD_PATTERN = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        );
        if (password == null || password.isEmpty()) {
            throw new Exception("La password non è stata inserita.");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new Exception("La password non è valida.");
        }
        return true;
    }

    // Metodo per salvare l'operatore
    private void salvaOperatore() {
        if (validateInputs()) {
            try {
                //le tabelle non sono uguali al file che ho messo per la crazione delle tabelle, mancano dei campi
                String query = "INSERT INTO operatori (nome, cognome, cf, email, password, centro) VALUES (?, ?, ?, ?, ?, ?)";
                database.executeUpdate(query, nome.getText(), cognome.getText(), codFiscale.getText(),
                        email.getText(), new String(password.getPassword()), centro.getText());
                JOptionPane.showMessageDialog(this, "Operatore registrato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                new Login();
                dispose();
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + e.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, err.toString(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
