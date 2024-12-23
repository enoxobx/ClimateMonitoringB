package LAB_B.Operatore;

import LAB_B.Common.LayoutStandard;
import LAB_B.Common.Operatore;
import LAB_B.Database.QueryExecutorImpl;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Random;

public class SignUp extends LayoutStandard {

    // Campi di input per la registrazione
    private final JTextField nomeField = new JTextField(15);
    private final JTextField cognomeField = new JTextField(15);
    private final JTextField codiceFiscaleField = new JTextField(15);
    private final JTextField emailField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confermaPasswordField = new JPasswordField(15);
    private final JTextField centroField = new JTextField(15);

    private final JButton helpButton = new JButton("?");

    // Costruttore
    public SignUp() {
        super(); // Inizializza LayoutStandard
        setTitle("Registrazione Operatore");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false); // Disabilita il ridimensionamento

        initializeUI(); // Configura l'interfaccia utente
        setVisible(true); // Mostra la finestra
    }

    // Metodo per inizializzare l'interfaccia utente
    private void initializeUI() {
        Container body = getBody(); // Ottieni il corpo principale
        body.setLayout(new BorderLayout(10, 10)); // Layout con margini

        // Aggiungi il pannello con il titolo
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Registra un nuovo Operatore", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(createHelpPanel(), BorderLayout.EAST); // Aggiungi il pannello di aiuto a destra
        body.add(titlePanel, BorderLayout.NORTH);

        // Aggiungi il pannello con i campi di input
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addField(inputPanel, "Nome:", nomeField);
        addField(inputPanel, "Cognome:", cognomeField);
        addField(inputPanel, "Codice Fiscale:", codiceFiscaleField);
        addField(inputPanel, "Email:", emailField);
        addField(inputPanel, "Password:", passwordField);
        addField(inputPanel, "Conferma Password:", confermaPasswordField);
        addField(inputPanel, "Centro di Monitoraggio:", centroField);
        body.add(inputPanel, BorderLayout.CENTER);

        // Aggiungi il bottone "Home" che è già stato creato in LayoutStandard
        body.add(home, BorderLayout.WEST);

        // Aggiungi i pulsanti in basso
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Salva");
        saveButton.addActionListener(e -> handleRegistration()); // Gestisce la registrazione
        JButton backButton = new JButton("Torna indietro");
        backButton.addActionListener(e -> {
            new Login().setVisible(true); // Torna alla schermata di login
            dispose(); // Chiudi la finestra di registrazione
        });
        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);
        body.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Metodo per gestire la registrazione
    private void handleRegistration() {
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String codiceFiscale = codiceFiscaleField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confermaPassword = new String(confermaPasswordField.getPassword()).trim();
        String centro = centroField.getText().trim();

        Operatore operatore = new Operatore(nome, cognome, codiceFiscale, email, password, centro, generateUsername());

        // Verifica che i dati siano validi
        if (!operatore.validate()) {
            JOptionPane.showMessageDialog(this, operatore.getErrorMessages(), "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Controlla che le password coincidano
        if (!password.equals(confermaPassword)) {
            JOptionPane.showMessageDialog(this, "Le password non coincidono", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Controlla che la password soddisfi i requisiti
        if (!operatore.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "La password deve contenere almeno una lettera maiuscola, una minuscola, un numero e un simbolo.", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica che l'email e il codice fiscale non siano già in uso
        try {
            QueryExecutorImpl queryExecutor = new QueryExecutorImpl();
            if (queryExecutor.emailEsistente(email)) {
                JOptionPane.showMessageDialog(this, "L'email è già in uso.", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (queryExecutor.codiceFiscaleEsistente(codiceFiscale)) {
                JOptionPane.showMessageDialog(this, "Il codice fiscale è già in uso.", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Salva l'operatore nel database
            if (queryExecutor.salvaOperatore(operatore)) {
                String usernameGenerato = operatore.getUsername();
                JOptionPane.showMessageDialog(this, "Operatore registrato con successo!\n" + "Email: " + email + "\n" + "Username: " + usernameGenerato, "Registrazione completata", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Chiudi la finestra di registrazione
                new Login().setVisible(true); // Mostra la finestra di login
            } else {
                JOptionPane.showMessageDialog(this, "Errore nella registrazione dell'operatore. Riprova.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            logUnexpectedError(ex, "Errore nella connessione al database durante la registrazione.");
            JOptionPane.showMessageDialog(this, "Errore nel database: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per generare lo username
    public String generateUsername() {
        String nomePart = nomeField.getText().length() >= 3 ? nomeField.getText().substring(0, 3) : nomeField.getText();
        String cognomePart = cognomeField.getText().length() >= 3 ? cognomeField.getText().substring(0, 3) : cognomeField.getText();
        int randomNum = new Random().nextInt(1000);
        return nomePart.toLowerCase() + "_" + cognomePart.toLowerCase() + randomNum;
    }

    // Metodo per loggare gli errori imprevisti
    private void logUnexpectedError(Exception e, String context) {
        System.err.println("Errore imprevisto: " + e.getMessage());
        System.err.println("Contesto: " + context);
        e.printStackTrace();

        try (FileWriter writer = new FileWriter("logs/error.log", true);
             PrintWriter logWriter = new PrintWriter(writer)) {
            logWriter.println("Errore imprevisto: " + e.getMessage());
            logWriter.println("Contesto: " + context);
            for (StackTraceElement element : e.getStackTrace()) {
                logWriter.println("\t" + element.toString());
            }
        } catch (IOException ioException) {
            System.err.println("Errore nel salvataggio del log: " + ioException.getMessage());
        }
    }

    // Metodo per creare il pannello di aiuto
    private JPanel createHelpPanel() {
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BorderLayout());
        helpPanel.add(helpButton, BorderLayout.CENTER);
        helpButton.addActionListener(e -> showHelpDialog());
        return helpPanel;
    }

    // Metodo per mostrare la finestra di aiuto
    private void showHelpDialog() {
        String helpMessage = "<html><h3>Guida alla compilazione dei campi:</h3>" +
                "<ul>" +
                "<li><strong>Nome:</strong> Inserisci il tuo nome (max 30 caratteri).</li>" +
                "<li><strong>Cognome:</strong> Inserisci il tuo cognome (max 30 caratteri).</li>" +
                "<li><strong>Codice Fiscale:</strong> Inserisci il tuo codice fiscale.</li>" +
                "<li><strong>Email:</strong> Inserisci una email valida.</li>" +
                "<li><strong>Password:</strong> La password deve contenere almeno una lettera maiuscola, una minuscola, un numero e un simbolo.</li>" +
                "<li><strong>Centro di Monitoraggio:</strong> Inserisci il centro di monitoraggio assegnato.</li>" +
                "</ul></html>";
        JOptionPane.showMessageDialog(this, helpMessage, "Guida", JOptionPane.INFORMATION_MESSAGE);
    }

    // Metodo per aggiungere un campo di input al pannello
    private void addField(JPanel panel, String labelText, JTextField field) {
        panel.add(new JLabel(labelText));
        panel.add(field);
    }

}
