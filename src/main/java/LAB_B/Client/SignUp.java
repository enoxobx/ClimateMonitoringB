package LAB_B.Client;

import LAB_B.Common.LayoutStandard;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class SignUp extends LayoutStandard {

    // Dichiarazione dei campi di input
    private JTextField nome = new JTextField(15);
    private JTextField cognome = new JTextField(15);
    private JTextField codFiscale = new JTextField(15);  // Codice fiscale (unico campo obbligatorio)
    private JTextField email = new JTextField(15);
    private JPasswordField password = new JPasswordField(15);
    private JTextField centro = new JTextField(15);

    // Stringhe per memorizzare i risultati e gli errori durante la validazione
    private StringBuilder res = new StringBuilder();
    private StringBuilder err = new StringBuilder();

    // Simulazione di un database con codici fiscali già registrati
    private List<String> codiciFiscaliRegistrati = new ArrayList<>();

    // Costruttore della finestra di registrazione
    public SignUp() {
        super();

        // Impostazioni iniziali della finestra
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Etichetta del titolo
        JLabel bio = new JLabel("Registra nuovo Operatore");
        bio.setHorizontalAlignment(JLabel.CENTER);
        bio.setFont(new Font("Courier", Font.BOLD, 20));
        add(bio, BorderLayout.NORTH);

        // Menu di informazioni con un item che mostra come compilare il modulo
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

        // Pannello principale per il layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello per i campi di input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Aggiungi i campi di input al pannello
        addField(inputPanel, gbc, "Nome:", nome);
        addField(inputPanel, gbc, "Cognome:", cognome);
        addField(inputPanel, gbc, "Codice Fiscale:", codFiscale);
        addField(inputPanel, gbc, "Email:", email);
        addField(inputPanel, gbc, "Password:", password);
        addField(inputPanel, gbc, "Centro Monitoraggio:", centro);

        // Aggiungi il pannello di input al pannello principale
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Pannello con i bottoni di salvataggio e chiusura
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

    // Metodo per aggiungere un campo di input al pannello con il relativo label
    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        // Aggiungi l'etichetta (label)
        gbc.gridx = 0;
        gbc.gridy++;  // Incrementa la riga per ogni nuovo campo
        panel.add(new JLabel(label), gbc);

        // Aggiungi il campo di input (field)
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Funzione per generare automaticamente l'username basato su nome, cognome e codice fiscale
    private String generateUsername(String nome, String cognome, String codFiscale) {
        // Formatta nome e cognome con la prima lettera maiuscola e il resto minuscolo
        String nomeFormatted = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
        String cognomeFormatted = cognome.substring(0, 1).toUpperCase() + cognome.substring(1).toLowerCase();

        // Estrai le ultime 2 cifre dell'anno di nascita dal Codice Fiscale
        String yearFromCodFiscale = codFiscale.substring(6, 8);  // Le cifre dell'anno sono nelle posizioni 7 e 8

        // Concatenazione di nome, cognome e anno per formare l'username
        String username = nomeFormatted + cognomeFormatted + yearFromCodFiscale;

        return username;
    }

    // Metodo che viene eseguito per salvare l'operatore (registrazione)
    private void salvaOperatore() {
        if (validateInputs()) {
            String codiceFiscaleInserito = codFiscale.getText().trim();

            // Verifica se il codice fiscale è già stato registrato
            if (isCodiceFiscaleRegistrato(codiceFiscaleInserito)) {
                JOptionPane.showMessageDialog(this, "Codice Fiscale già utilizzato. Impossibile completare la registrazione.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;  // Esce senza proseguire se il codice fiscale è già in uso
            }

            // Genera l'username
            String usernameGenerato = generateUsername(nome.getText(), cognome.getText(), codiceFiscaleInserito);

            // Aggiungi il codice fiscale alla lista simulata del database
            codiciFiscaliRegistrati.add(codiceFiscaleInserito);

            // Salva i dati nel database
            salvaDatiNelDatabase(nome.getText(), cognome.getText(), codiceFiscaleInserito, email.getText(), new String(password.getPassword()), centro.getText());

            // Mostra il messaggio di successo
            JOptionPane.showMessageDialog(this, "Registrazione completata con successo. \n" +
                    "Username: " + usernameGenerato + "\n" + "Password: " + new String(password.getPassword()), "Successo", JOptionPane.INFORMATION_MESSAGE);

            // Se la registrazione è andata a buon fine, chiudi la finestra e apri la finestra di login
            new Login().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, err.toString(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Funzione di validazione per i campi di input
    private boolean validateInputs() {
        res.setLength(0);
        err.setLength(0);

        // Validazione dei campi
        validateField(nome.getText(), "Il nome non è stato inserito.", 30);
        validateField(cognome.getText(), "Il cognome non è stato inserito.", 30);
        validateField(codFiscale.getText(), "Il codice fiscale deve avere 16 caratteri.", 16);
        validateEmail(email.getText());
        validatePassword(new String(password.getPassword()));
        validateField(centro.getText(), "Il centro di monitoraggio non è stato inserito.", 50);

        // Ritorna true se non ci sono errori
        return err.length() == 0;
    }

    // Funzione di validazione per i campi di tipo testo
    private void validateField(String value, String errorMessage, int maxLength) {
        if (value.isEmpty() || value.length() > maxLength) {
            err.append(errorMessage).append("\n");
        } else {
            res.append(value.toUpperCase()).append("; ");
        }
    }

    // Funzione di validazione per l'email
    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, email)) {
            err.append("L'email non è valida.\n");
        } else {
            res.append(email.toUpperCase()).append("; ");
        }
    }

    // Funzione di validazione per la password
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

    // Funzione per verificare se un codice fiscale è già registrato
    private boolean isCodiceFiscaleRegistrato(String codiceFiscale) {
        return codiciFiscaliRegistrati.contains(codiceFiscale);
    }

    // Funzione per salvare i dati nel database PostgreSQL
    private void salvaDatiNelDatabase(String nome, String cognome, String codiceFiscale, String email, String password, String centro) {
        // URL di connessione con il numero di porta 5432 e nome del database 'cliamte_monitoring'
        String url = "jdbc:postgresql://localhost:5432/climate_monitoring";  // URL del database con il numero di porta
        String sql = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, centro_monitoraggio) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, "postgres", "0000");  // Usa le tue credenziali
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Imposta i parametri della query
            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setString(3, codiceFiscale);
            pstmt.setString(4, email);
            pstmt.setString(5, password);
            pstmt.setString(6, centro);

            // Esegui l'inserimento nel database
            pstmt.executeUpdate();
            System.out.println("Operatore registrato con successo.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore durante la registrazione nel database.");
        }
    }

}