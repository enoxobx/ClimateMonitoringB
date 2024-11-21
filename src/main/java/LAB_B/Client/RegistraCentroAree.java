package LAB_B.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistraCentroAree extends JFrame {

    // Dichiarazione dei campi di testo
    private JTextField nomeCentro;
    private JTextField indirizzo;
    private JTextField areeInteresse;

    public RegistraCentroAree() {
        // Impostazioni della finestra principale
        setSize(888, 588);  // Imposta le dimensioni della finestra
        setTitle("REGISTRA NUOVO CENTRO");  // Imposta il titolo della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Chiude l'applicazione quando la finestra viene chiusa
        setResizable(false);  // Disabilita la modifica delle dimensioni della finestra
        setLocationRelativeTo(null);  // Posiziona la finestra al centro dello schermo
        setVisible(true);  // Rende visibile la finestra

        // Pannello per le etichette (NOME CENTRO, INDIRIZZO, AREE DI INTERESSE)
        JPanel panelEtichette = new JPanel(new GridLayout(3, 1));
        panelEtichette.add(new JLabel("NOME CENTRO MONITORAGGIO", JLabel.RIGHT));
        panelEtichette.add(new JLabel("INDIRIZZO", JLabel.RIGHT));
        panelEtichette.add(new JLabel("AREE DI INTERESSE", JLabel.RIGHT));

        // Pannello per le caselle di testo
        nomeCentro = new JTextField(20);
        indirizzo = new JTextField();
        areeInteresse = new JTextField();  // Max 10 aree di interesse per centro

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
                // Codice per salvare il nuovo centro (inserimento nel database o altro)
                salvaCentro();
            }
        });

        botIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Codice per tornare alla finestra precedente (navigazione)
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

        // Logica di salvataggio nel database o altre operazioni
        // Puoi aggiungere qui il codice per salvare i dati nel database
        // Ad esempio, chiamare un metodo del database che registra il centro

        JOptionPane.showMessageDialog(this, "Centro salvato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

        // Pulisci i campi dopo il salvataggio
        nomeCentro.setText("");
        indirizzo.setText("");
        areeInteresse.setText("");
    }

    // Metodo per tornare alla finestra precedente
    private void tornaIndietro() {
        // Codice per chiudere la finestra corrente e tornare alla finestra precedente
        this.dispose();  // Chiude la finestra corrente
        // Puoi anche aprire un'altra finestra, se necessario
    }

    // Metodo main per testare la finestra in modo indipendente
    public static void main(String[] args) {
        new RegistraCentroAree();
    }
}
