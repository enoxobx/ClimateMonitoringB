package LAB_B.Client;

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

    // Stringhe per memorizzare i risultati e gli errori durante la validazione
    private StringBuilder res = new StringBuilder();
    private StringBuilder err = new StringBuilder();

    // Costruttore della finestra di registrazione
    public SignUp() {
        super();  // Costruttore della classe LayoutStandard, che potrebbe definire un layout base

        // Impostazioni iniziali della finestra
        setSize(500, 400);  // Imposta la dimensione della finestra
        setLayout(new BorderLayout());  // Usa un layout BorderLayout

        // Etichetta del titolo
        JLabel bio = new JLabel("Registra nuovo Operatore");
        bio.setHorizontalAlignment(JLabel.CENTER);  // Allinea il titolo al centro
        bio.setFont(new Font("Courier", Font.BOLD, 20));  // Imposta il font del titolo
        add(bio, BorderLayout.NORTH);  // Aggiunge l'etichetta al pannello superiore della finestra

        // Menu di informazioni con un item che mostra come compilare il modulo
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Info");
        JMenuItem i1 = new JMenuItem("Come compilare");
        // Mostra una finestra di dialogo con le informazioni quando l'utente seleziona "Come compilare"
        i1.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Nome e cognome in maiuscolo.\nCodice fiscale di 16 caratteri.\n" +
                        "Email valida e password con almeno 8 caratteri, inclusi maiuscola, minuscola, numero e simbolo.\n" +
                        "Nome del centro in maiuscolo."));
        menu.add(i1);
        mb.add(menu);
        setJMenuBar(mb);  // Aggiungi la barra dei menu alla finestra

        // Pannello principale per il layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Aggiungi margini intorno al pannello

        // Pannello per i campi di input
        JPanel inputPanel = new JPanel(new GridBagLayout());  // Usa GridBagLayout per disporre i campi
        GridBagConstraints gbc = new GridBagConstraints();  // Crea un oggetto per gestire il layout
        gbc.fill = GridBagConstraints.HORIZONTAL;  // I campi si estendono orizzontalmente
        gbc.insets = new Insets(5, 5, 5, 5);  // Distanza tra i componenti

        // Aggiungi i campi di input al pannello
        addField(inputPanel, gbc, "Nome:", nome);
        addField(inputPanel, gbc, "Cognome:", cognome);
        addField(inputPanel, gbc, "Codice Fiscale:", codFiscale);
        addField(inputPanel, gbc, "Email:", email);
        addField(inputPanel, gbc, "Password:", password);
        addField(inputPanel, gbc, "Centro Monitoraggio:", centro);

        // Aggiungi il pannello di input al pannello principale
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);  // Aggiungi il pannello principale alla finestra

        // Pannello con i bottoni di salvataggio e chiusura
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));  // Pannello per i bottoni
        JButton butSalva = new JButton("Salva");
        butSalva.addActionListener(e -> salvaOperatore());  // Azione per salvare i dati dell'operatore

        JButton butChiudi = new JButton("Torna indietro");
        butChiudi.addActionListener(e -> {
            new Login().setVisible(true);  // Mostra la finestra di login
            dispose();  // Chiudi la finestra di registrazione
        });

        buttonPanel.add(butChiudi);  // Aggiungi il bottone "Torna indietro"
        buttonPanel.add(butSalva);   // Aggiungi il bottone "Salva"
        add(buttonPanel, BorderLayout.SOUTH);  // Aggiungi il pannello dei bottoni al fondo della finestra

        setVisible(true);  // Rende visibile la finestra
    }

    // Metodo per aggiungere un campo di input al pannello con il relativo label
    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy++;  // Incrementa la riga per ogni nuovo campo
        panel.add(new JLabel(label), gbc);  // Aggiungi l'etichetta (label)

        gbc.gridx = 1;
        panel.add(field, gbc);  // Aggiungi il campo di input (field)
    }

    // Funzione per generare automaticamente l'username basato su nome, cognome e codice fiscale
    private String generateUsername(String nome, String cognome, String codFiscale) {
        // Formatta nome e cognome con la prima lettera maiuscola e il resto minuscolo
        String nomeFormatted = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
        String cognomeFormatted = cognome.substring(0, 1).toUpperCase() + cognome.substring(1).toLowerCase();

        // Estrai le ultime 2 cifre dell'anno di nascita dal Codice Fiscale
        String yearFromCodFiscale = codFiscale.substring(6, 8);  // Le cifre dell'anno sono nelle posizioni 7 e 8

        // Concatenazione di nome, cognome e anno per formare l'username
        return nomeFormatted + cognomeFormatted + yearFromCodFiscale;
    }

    // Metodo che viene eseguito per salvare l'operatore (registrazione)
    private void salvaOperatore() {
        if (validateInputs()) {  // Se la validazione va a buon fine
            String codiceFiscaleInserito = codFiscale.getText().trim();

            // Genera l'username
            String usernameGenerato = generateUsername(nome.getText(), cognome.getText(), codiceFiscaleInserito);

            // Salva i dati nel database
            salvaDatiNelDatabase(nome.getText(), cognome.getText(), codiceFiscaleInserito, email.getText(), new String(password.getPassword()), centro.getText(), usernameGenerato);

            // Mostra il messaggio di successo
            JOptionPane.showMessageDialog(this, "Registrazione completata con successo. \n" +
                    "Username: " + usernameGenerato + "\n" + "Password: " + new String(password.getPassword()), "Successo", JOptionPane.INFORMATION_MESSAGE);

            // Se la registrazione è andata a buon fine, chiudi la finestra e apri la finestra di login
            new Login().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, err.toString(), "Errore", JOptionPane.ERROR_MESSAGE);  // Mostra gli errori
        }
    }

    // Funzione di validazione per i campi di input
    private boolean validateInputs() {
        res.setLength(0);  // Pulisce i risultati
        err.setLength(0);  // Pulisce gli errori

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

    // Funzione per salvare i dati nel database PostgreSQL
    private void salvaDatiNelDatabase(String nome, String cognome, String codiceFiscale, String email, String password, String centro, String username) {
        Connection conn = null;
        try {
            // Connessione al database PostgreSQL
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/climate_monitoring", "postgres", "0000");

            String sql = "INSERT INTO operatori (nome, cognome, codice_fiscale, email, password, centro_monitoraggio, username) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, codiceFiscale);
            ps.setString(4, email);
            ps.setString(5, password);
            ps.setString(6, centro);
            ps.setString(7, username);

            ps.executeUpdate();  // Esegui l'operazione di inserimento nel database
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio dei dati nel database.", "Errore", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Chiudere la connessione nel blocco finally per garantire la chiusura
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
        new SignUp();  // Avvia la finestra di registrazione
    }
}
