package LAB_B.Operatore;

import LAB_B.Common.Home;
import LAB_B.Common.LayoutStandard;
import LAB_B.Database.QueryExecutorImpl;
import LAB_B.Common.Operatore;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class SignUp extends LayoutStandard {

    // Campi di input per la registrazione
    private final JTextField nomeField = new JTextField(15);
    private final JTextField cognomeField = new JTextField(15);
    private final JTextField codiceFiscaleField = new JTextField(15);
    private final JTextField emailField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confermaPasswordField = new JPasswordField(15);
    private final JTextField centroField = new JTextField(15);

    // Pulsante aiuto
    private final JButton helpButton = new JButton("?");

    // Pulsante "Home"
    private JButton homeButton;

    // Costruttore
    public SignUp() {
        super(); // Inizializza LayoutStandard (incluso il bottone "Home")
        setTitle("Registrazione Operatore");
        setSize(600, 500); // Dimensioni personalizzate
        setLocationRelativeTo(null); // Centra la finestra
        setResizable(false); // Disabilita il ridimensionamento

        // Configura l'interfaccia utente
        initializeUI();

        setVisible(true); // Rendi visibile la finestra
    }

    // Metodo per inizializzare l'interfaccia utente
    private void initializeUI() {
        Container body = getBody(); // Ottieni il corpo principale da LayoutStandard
        body.setLayout(new BorderLayout(10, 10)); // Imposta il layout principale

        // Pannello "Home" a sinistra
        JPanel homePanel = createHomePanel();
        body.add(homePanel, BorderLayout.WEST);

        // Pannello superiore con il titolo
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Registra un nuovo Operatore", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Pannello di aiuto in alto a destra
        JPanel helpPanel = createHelpPanel();
        titlePanel.add(helpPanel, BorderLayout.EAST); // Aggiungi il pannello di aiuto accanto al titolo

        body.add(titlePanel, BorderLayout.NORTH);

        // Pannello centrale con i campi di input
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Usa addField per aggiungere i campi
        addField(inputPanel, "Nome:", nomeField);
        addField(inputPanel, "Cognome:", cognomeField);
        addField(inputPanel, "Codice Fiscale:", codiceFiscaleField);
        addField(inputPanel, "Email:", emailField);
        addField(inputPanel, "Password:", passwordField);
        addField(inputPanel, "Conferma Password:", confermaPasswordField);
        addField(inputPanel, "Centro di Monitoraggio:", centroField);

        body.add(inputPanel, BorderLayout.CENTER);

        // Pannello inferiore con i pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Salva");
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.addActionListener(e -> handleRegistration());

        JButton backButton = new JButton("Torna indietro");
        backButton.setPreferredSize(new Dimension(150, 40));
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

        // Crea un nuovo oggetto Operatore
        Operatore operatore = new Operatore(nome, cognome, codiceFiscale, email, password, centro, "username");

        // Verifica preliminare dei dati
        if (!operatore.validate()) {
            JOptionPane.showMessageDialog(this, operatore.getErrorMessages(), "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica che le password coincidano
        if (!password.equals(confermaPassword)) {
            JOptionPane.showMessageDialog(this, "Le password non coincidono", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica che la password soddisfi i requisiti di formato, usando il metodo isValidPassword dell'oggetto operatore
        if (!operatore.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "La password deve contenere almeno una lettera maiuscola, una minuscola, un numero e un simbolo.", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica che l'email non sia già registrata
        try {
            QueryExecutorImpl queryExecutor = new QueryExecutorImpl();
            if (queryExecutor.emailEsistente(email)) {
                JOptionPane.showMessageDialog(this, "L'email è già in uso.", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verifica che il codice fiscale non sia già registrato
            if (queryExecutor.codiceFiscaleEsistente(codiceFiscale)) {
                JOptionPane.showMessageDialog(this, "Il codice fiscale è già in uso.", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Salvataggio dei dati nel database
            if (queryExecutor.salvaOperatore(operatore)) {
                String usernameGenerato = operatore.getUsername(); // Username generato nel salvataggio
                JOptionPane.showMessageDialog(this,
                        "Operatore registrato con successo!\n" +
                                "Email: " + email + "\n" +
                                "Username: " + usernameGenerato,
                        "Registrazione completata",
                        JOptionPane.INFORMATION_MESSAGE);

                // Chiude la finestra di registrazione
                dispose(); // Chiude la finestra di registrazione

                // Apertura della finestra di login
                new Login();  // Mostra la finestra di login

            } else {
                JOptionPane.showMessageDialog(this, "Errore nella registrazione dell'operatore. Riprova.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            logUnexpectedError(ex, "Errore nella connessione al database durante la registrazione.");
            JOptionPane.showMessageDialog(this, "Errore nel database: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per loggare gli errori imprevisti
    private void logUnexpectedError(Exception e, String context) {
        System.err.println("Errore imprevisto: " + e.getMessage());
        System.err.println("Contesto: " + context);
        e.printStackTrace(); // Stampa dello stack trace

        // Salvataggio dell'errore in un file di log
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

    // Metodo per creare il pannello "Home"
    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setPreferredSize(new Dimension(100, 0)); // Impostiamo una larghezza fissa per il pannello
        homePanel.setMaximumSize(new Dimension(100, Integer.MAX_VALUE)); // Impostiamo una dimensione massima per la parte verticale

        homePanel.add(Box.createVerticalGlue()); // Allinea il pulsante al centro verticale

        homeButton = new JButton("Home");
        homeButton.setMaximumSize(new Dimension(100, Integer.MAX_VALUE)); // Impostiamo una larghezza fissa per il pulsante
        homeButton.addActionListener(e -> {
            new Home().setVisible(true);  // Mostra la finestra Home
            dispose();  // Chiude la finestra attuale
        });

        homePanel.add(homeButton);  // Aggiungi il pulsante al pannello
        homePanel.add(Box.createVerticalGlue());  // Allinea il pulsante al centro

        return homePanel;
    }

    // Metodo per creare il pannello di aiuto
    private JPanel createHelpPanel() {
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BorderLayout());
        helpPanel.add(helpButton, BorderLayout.CENTER);
        helpButton.addActionListener(e -> showHelpDialog()); // Usa il metodo per mostrare la guida
        return helpPanel;
    }

    // Metodo per mostrare la finestra di aiuto
    private void showHelpDialog() {
        String helpMessage = "<html><h3>Guida alla compilazione dei campi:</h3>" +
                "<ul>" +
                "<li><strong>Nome:</strong> Inserisci il tuo nome (max 30 caratteri).</li>" +
                "<li><strong>Cognome:</strong> Inserisci il tuo cognome (max 30 caratteri).</li>" +
                "<li><strong>Codice Fiscale:</strong> Inserisci il tuo codice fiscale (16 caratteri).</li>" +
                "<li><strong>Email:</strong> Inserisci un'email valida.</li>" +
                "<li><strong>Password:</strong> La password deve essere lunga almeno 8 caratteri e contenere lettere maiuscole, minuscole, numeri e simboli.</li>" +
                "<li><strong>Centro Monitoraggio:</strong> Inserisci il centro di monitoraggio.</li>" +
                "</ul></html>";

        JOptionPane.showMessageDialog(this, helpMessage, "Come compilare i campi", JOptionPane.INFORMATION_MESSAGE);
    }

    // Metodo per aggiungere un campo con etichetta
    private void addField(JPanel panel, String label, JTextField field) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setLabelFor(field);
        field.setToolTipText("Inserisci " + label.toLowerCase());

        panel.add(fieldLabel);
        panel.add(field);
    }
}
