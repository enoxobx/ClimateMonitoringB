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


    public Login(){
        super();
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra sullo schermo

        // Pannello principale
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        // Campo username
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        panel.add(usernameField);

        // Campo password
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        panel.add(passwordField);

        // Bottone login
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                Client client = new Client();


                // messaggio di servizio da sustituire con l'apertura della finestra oppurta dell'operatore
                JOptionPane.showMessageDialog(null, "Successo login: " +client.login(username,password));
            }
        });
        panel.add(loginButton);

        //bottone registrazione
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SignUp sign = new SignUp();
                dispose();
            }
        });
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

}