package LAB_B.Common.Operatore;

import LAB_B.Common.Interface.Operatore;
import LAB_B.Common.LayoutStandard;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

public class SignUp extends LayoutStandard {

    // Campi di input per la registrazione
    private final JTextField nomeField = new JTextField(15);
    private final JTextField cognomeField = new JTextField(15);
    private final JTextField codiceFiscaleField = new JTextField(15);
    private final JTextField emailField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confermaPasswordField = new JPasswordField(15);
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
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        body.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Metodo per aggiungere un campo con etichetta
    private void addField(JPanel panel, String label, JTextField field) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setLabelFor(field);
        field.setToolTipText("Inserisci " + label.toLowerCase());
        panel.add(fieldLabel);
        panel.add(field);
    }

    // Pannello di aiuto
    private JPanel createHelpPanel() {
        JPanel helpPanel = new JPanel();
        helpPanel.add(helpButton); // Aggiungi il bottone di aiuto
        helpButton.addActionListener(e -> showHelpDialog()); // Mostra la finestra di aiuto
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
                "</ul></html>";
        JOptionPane.showMessageDialog(this, helpMessage, "Guida", JOptionPane.INFORMATION_MESSAGE);
    }



    // Gestisce la registrazione dell'operatore
    private void handleRegistration() {
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String codiceFiscale = codiceFiscaleField.getText().trim().toUpperCase();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confermaPassword = new String(confermaPasswordField.getPassword());


        // Crea un nuovo oggetto operatore
        Operatore operatore = new Operatore(nome, cognome, codiceFiscale, email, password, confermaPassword, null);

        // Verifica la validità dei dati
        if (!operatore.validate()) {
            // Verifica che ci siano errori e li mostri
            String errorMessages = operatore.getErrorMessages();
            if (!errorMessages.isEmpty()) {
                JOptionPane.showMessageDialog(this, errorMessages, "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
            }
            return;  // Termina se ci sono errori
        }

        // Registrazione nel sistema
        try {
            // Salvataggio dei dati
            if (db.registrazione(operatore)) {
                // Mostra il messaggio di successo includendo l'email e lo username
                String successMessage = "Operatore registrato con successo!\n";
                successMessage += "Email: " + email + "\n";
                successMessage += "Username: " + operatore.getUsername();  // Usa il metodo getUsername() per ottenere lo username generato

                JOptionPane.showMessageDialog(this, successMessage, "Successo", JOptionPane.INFORMATION_MESSAGE);
                new Login().setVisible(true); // Vai alla schermata di login
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante la registrazione. Riprovare.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore nel contatto con il server.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}
