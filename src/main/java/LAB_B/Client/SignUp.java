package LAB_B.Client;

import LAB_B.Common.*;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;
//import java.sql.*; // Import per database disabilitato temporaneamente

public class SignUp extends LayoutStandard {

    private JTextField nome = new JTextField(15);
    private JTextField cognome = new JTextField(15);
    private JTextField codFiscale = new JTextField(15);
    private JTextField email = new JTextField(15);
    private JPasswordField password = new JPasswordField(15);
    private JTextField centro = new JTextField(15);

    private StringBuilder res = new StringBuilder();
    private StringBuilder err = new StringBuilder();

    public SignUp() {
        super();

        setSize(500, 400);
        setLayout(new BorderLayout());

        JLabel bio = new JLabel("Registra nuovo Operatore");
        bio.setHorizontalAlignment(JLabel.CENTER);
        bio.setFont(new Font("Courier", Font.BOLD, 20));
        add(bio, BorderLayout.NORTH);

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

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Aggiungi i campi in righe separate
        addField(inputPanel, gbc, "Nome:", nome);
        addField(inputPanel, gbc, "Cognome:", cognome);
        addField(inputPanel, gbc, "Codice Fiscale:", codFiscale);
        addField(inputPanel, gbc, "Email:", email);
        addField(inputPanel, gbc, "Password:", password);
        addField(inputPanel, gbc, "Centro Monitoraggio:", centro);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

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
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        // Aggiungi l'etichetta
        gbc.gridx = 0;
        gbc.gridy++;  // Incrementa la riga per ogni nuovo campo
        panel.add(new JLabel(label), gbc);

        // Aggiungi il campo di input
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void salvaOperatore() {
        if (validateInputs()) {
            JOptionPane.showMessageDialog(this, "Registrazione simulata: \n" + res, "Successo", JOptionPane.INFORMATION_MESSAGE);
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
            res.append(value.toUpperCase()).append(";");
        }
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, email)) {
            err.append("L'email non è valida.\n");
        } else {
            res.append(email.toUpperCase()).append(";");
        }
    }

    private void validatePassword(String password) {
        if (password.isEmpty() || password.length() < 8) {
            err.append("La password deve avere almeno 8 caratteri.\n");
        } else if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") ||
                !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*")) {
            err.append("La password non è valida.\n");
        } else {
            res.append(password).append(";");
        }
    }
}