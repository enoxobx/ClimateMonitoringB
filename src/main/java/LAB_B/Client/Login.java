package LAB_B.Client;

import LAB_B.Common.LayoutStandard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends LayoutStandard {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Login() {
        super();
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setSize(400, 300); // Dimensione della finestra
        setResizable(false); // Finestra non ridimensionabile

        // Pannello principale con BoxLayout per allineare gli elementi verticalmente
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margini attorno al contenuto

        // Campo username
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Massima larghezza
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spaziatura verticale

        // Campo password
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Massima larghezza
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spaziatura verticale

        // Pannello per i pulsanti con FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Aggiungi i pannelli al contenitore principale
        mainPanel.add(buttonPanel);

        // Configura i bottoni
        configureButtons();

        // Aggiungi il pannello principale alla finestra
        add(mainPanel);
        setVisible(true);
    }

    private void configureButtons() {
        // Azione per il bottone login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                // Verifica se i campi sono vuoti
                if (username.isEmpty() && password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Entrambi i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Il campo username non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Il campo password non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Simuliamo una funzione di login
                    Client client = new Client();

                    // Verifica se l'username e la password sono corretti
                    boolean success = client.login(username, password);  // Supponiamo che client.login() restituisca un booleano

                    // Verifica se login ha avuto successo
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Login effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        // Procedi con l'apertura della finestra successiva (ad esempio la schermata principale dell'app)
                    } else {
                        // Mostra messaggio di errore in caso di username o password errati
                        JOptionPane.showMessageDialog(null, "Username o password errati!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Azione per il bottone registrazione
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SignUp sign = new SignUp();
                dispose();
            }
        });
    }
}