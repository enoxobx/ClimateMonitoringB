package LAB_B.Operatore;

import LAB_B.Common.Home;
import LAB_B.Common.LayoutStandard;
import LAB_B.Database.DatabaseImpl;
import LAB_B.Database.QueryExecutorImpl;
import LAB_B.Common.Operatore;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class SignUp extends LayoutStandard {

    // Campi di input per i dati dell'operatore
    private final JTextField nome = new JTextField(15);
    private final JTextField cognome = new JTextField(15);
    private final JTextField codFiscale = new JTextField(15);
    private final JTextField email = new JTextField(15);
    private final JPasswordField password = new JPasswordField(15);
    private final JTextField centro = new JTextField(15);
    private final JPasswordField confermaPassword = new JPasswordField(15);

    // Pulsanti principali
    private JButton homeButton;
    private JButton helpButton;

    public SignUp() {
        super();

        // Configurazione del contenitore principale
        Container body = getBody();
        body.setLayout(new BorderLayout());

        // Pannello Home
        JPanel homePanel = createHomePanel();
        body.add(homePanel, BorderLayout.WEST);

        // Etichetta per il titolo
        JLabel bio = new JLabel("Registra nuovo Operatore", JLabel.CENTER);
        bio.setFont(new Font("Courier", Font.BOLD, 20));
        body.add(bio, BorderLayout.NORTH);

        // Pannello di aiuto
        JPanel helpPanel = createHelpPanel();
        body.add(helpPanel, BorderLayout.NORTH);

        // Pannello principale con i campi di input e i pulsanti
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello per i campi di input
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel);

        // Pannello per i pulsanti di azione
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel);

        // Aggiunta del pannello principale al contenitore
        body.add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Metodo per creare il pannello Home
    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setPreferredSize(new Dimension(100, 0));
        homePanel.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));

        homePanel.add(Box.createVerticalGlue());

        homeButton = new JButton("Home");
        homeButton.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
        homeButton.addActionListener(e -> {
            new Home().setVisible(true);
            dispose();
        });

        homePanel.add(homeButton);
        homePanel.add(Box.createVerticalGlue());
        return homePanel;
    }

    // Metodo per creare il pannello di aiuto
    private JPanel createHelpPanel() {
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        helpButton = new JButton("?");
        helpButton.addActionListener(e -> showHelpDialog());

        helpPanel.add(helpButton);
        return helpPanel;
    }

    // Metodo per creare il pannello di input
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10));
        inputPanel.setPreferredSize(new Dimension(350, 250));

        // Aggiunta dei campi al pannello
        addField(inputPanel, "Nome:", nome);
        addField(inputPanel, "Cognome:", cognome);
        addField(inputPanel, "Codice Fiscale:", codFiscale);
        addField(inputPanel, "Email:", email);
        addField(inputPanel, "Password:", password);
        addField(inputPanel, "Conferma Password:", confermaPassword);
        addField(inputPanel, "Centro Monitoraggio:", centro);

        return inputPanel;
    }

    // Metodo per creare il pannello dei pulsanti
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton butSalva = new JButton("Salva");
        butSalva.setPreferredSize(new Dimension(150, 40));
        butSalva.addActionListener(e -> salvaOperatore());

        JButton butChiudi = new JButton("Torna indietro");
        butChiudi.setPreferredSize(new Dimension(150, 40));
        butChiudi.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        buttonPanel.add(butChiudi);
        buttonPanel.add(butSalva);
        return buttonPanel;
    }

    // Metodo per aggiungere un campo con etichetta
    private void addField(JPanel panel, String label, JTextField field) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setLabelFor(field);
        field.setToolTipText("Inserisci " + label.toLowerCase());

        panel.add(fieldLabel);
        panel.add(field);
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

    // Metodo per salvare l'operatore
    private void salvaOperatore() {
        // Lettura e validazione dei campi
        String nomeText = nome.getText().trim();
        String cognomeText = cognome.getText().trim();
        String codFiscaleText = codFiscale.getText().trim().toUpperCase();
        String emailText = email.getText().trim();
        String passwordText = new String(password.getPassword());
        String confermaPasswordText = new String(confermaPassword.getPassword());
        String centroText = centro.getText().trim();

        // Controllo di validità dei dati
        if (nomeText.isEmpty() || cognomeText.isEmpty() || codFiscaleText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || centroText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutti i campi devono essere compilati.", "Errore", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!codFiscaleText.matches("[A-Z0-9]{16}")) {
            JOptionPane.showMessageDialog(this, "Il Codice Fiscale deve essere di 16 caratteri alfanumerici.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!emailText.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Inserisci un'email valida.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (passwordText.length() < 8 || !passwordText.matches(".*[A-Z].*") || !passwordText.matches(".*[a-z].*") || !passwordText.matches(".*\\d.*") || !passwordText.matches(".*[!@#\\$%\\^&*].*")) {
            JOptionPane.showMessageDialog(this, "La password deve essere lunga almeno 8 caratteri e contenere lettere maiuscole, minuscole, numeri e simboli.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!passwordText.equals(confermaPasswordText)) {
            JOptionPane.showMessageDialog(this, "Le password non corrispondono.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creazione oggetto Operatore e verifica ulteriori controlli
        Operatore operatore = new Operatore(nomeText, cognomeText, codFiscaleText, emailText, passwordText, centroText);

        if (!operatore.validate()) {
            JOptionPane.showMessageDialog(this, operatore.getErrorMessages(), "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            QueryExecutorImpl queryExecutor = new QueryExecutorImpl(DatabaseImpl.getConnection());

            // Controlli di unicità
            if (queryExecutor.codiceFiscaleEsistente(codFiscaleText)) {
                JOptionPane.showMessageDialog(this, "Sei gi\u00e0 registrato con questo codice fiscale.", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (queryExecutor.emailEsistente(emailText)) {
                JOptionPane.showMessageDialog(this, "Questa email \u00e8 gi\u00e0 stata utilizzata.", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String usernameGenerato = operatore.getUsername();
            if (queryExecutor.isUsernameExist(usernameGenerato)) {
                JOptionPane.showMessageDialog(this, "Questo username \u00e8 gi\u00e0 stato utilizzato.", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Salvataggio dell'operatore
            boolean registrazioneSuccesso = queryExecutor.salvaOperatore(nomeText, cognomeText, codFiscaleText, emailText, passwordText, centroText, usernameGenerato);

            if (registrazioneSuccesso) {
                JOptionPane.showMessageDialog(this, "Registrazione completata con successo.\nUsername: " + usernameGenerato + "\nEmail: " + emailText, "Successo", JOptionPane.INFORMATION_MESSAGE);
                new Login().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Si \u00e8 verificato un errore durante la registrazione.", "Errore", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore di connessione al database.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUp::new);
    }
}
