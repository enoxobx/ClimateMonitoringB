package LAB_B.Operatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistraCentroAree extends JFrame {

    // Dichiarazione dei campi di testo
    private JTextField nomeCentro;
    private JTextField indirizzo;
    private JTextField areeInteresse;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/climate_monitoring";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "0000";

    public RegistraCentroAree() {
        // Impostazioni della finestra principale
        setSize(888, 588);
        setTitle("REGISTRA NUOVO CENTRO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        // Pannello per le etichette (NOME CENTRO, INDIRIZZO, AREE DI INTERESSE)
        JPanel panelEtichette = new JPanel(new GridLayout(3, 1));
        panelEtichette.add(new JLabel("NOME CENTRO MONITORAGGIO", JLabel.RIGHT));
        panelEtichette.add(new JLabel("INDIRIZZO", JLabel.RIGHT));
        panelEtichette.add(new JLabel("AREE DI INTERESSE", JLabel.RIGHT));

        // Pannello per le caselle di testo
        nomeCentro = new JTextField(20);
        indirizzo = new JTextField();
        areeInteresse = new JTextField();

        JPanel panelTesto = new JPanel(new GridLayout(3, 1, 10, 0));
        panelTesto.add(nomeCentro);
        panelTesto.add(indirizzo);
        panelTesto.add(areeInteresse);

        // Pannello intermedio che unisce etichette e caselle di testo
        JPanel Jcentro = new JPanel();
        Jcentro.setLayout(new BoxLayout(Jcentro, BoxLayout.X_AXIS));
        Jcentro.add(panelEtichette);
        Jcentro.add(panelTesto);

        JPanel panel = new JPanel();
        panel.add(Jcentro);

        // Pannello inferiore con i bottoni (Salva e Indietro)
        JButton botSalva = new JButton("SALVA");
        JButton botIndietro = new JButton("INDIETRO");

        // Aggiungi azioni ai bottoni
        botSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Codice per salvare il nuovo centro
                salvaCentro();
            }
        });

        botIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Torna alla finestra precedente
                tornaIndietro();
            }
        });

        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        panelSud.add(botSalva);
        panelSud.add(botIndietro);

        // Aggiungi i pannelli alla finestra
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.getContentPane().add(panelSud, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Metodo per gestire la logica di salvataggio del centro
    private void salvaCentro() {
        String nome = nomeCentro.getText().trim();
        String indirizzoCentro = indirizzo.getText().trim();
        String aree = areeInteresse.getText().trim();

        // Verifica che tutti i campi siano compilati
        if (nome.isEmpty() || indirizzoCentro.isEmpty() || aree.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutti i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Logica di salvataggio nel database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO centri_monitoraggio (nome, indirizzo, aree_interesse) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setString(2, indirizzoCentro);
                stmt.setString(3, aree);

                // Esegui il salvataggio
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Centro salvato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

                // Pulisci i campi dopo il salvataggio
                nomeCentro.setText("");
                indirizzo.setText("");
                areeInteresse.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio del centro", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per tornare alla finestra precedente
    private void tornaIndietro() {
        this.dispose();  // Chiude la finestra corrente
    }

    // Metodo main per testare la finestra in modo indipendente
    public static void main(String[] args) {
        new RegistraCentroAree();
    }
}
