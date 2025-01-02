package LAB_B.Common.Operatore;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import LAB_B.Common.*;
import LAB_B.Database.*;

public class LayoutOperatore extends LayoutStandard {

    private final String username;
    private final JButton aggiungiDatiClimatici;
    private final JButton salvaDatiButton;
    private final JButton indietroButton;
    private final JButton creaCentroButton;
    private final JList<String> centriList;
    private final DefaultListModel<String> listaCentriModel;
    private final JLabel titleLable;
    private final ScrollPane centriScrollPane;

    public LayoutOperatore(String username) {
        super(); // Chiamata al costruttore della classe padre LayoutStandard
        this.username = username;

        // Inizializzazione del componente centriScrollPane
        centriScrollPane = new ScrollPane(); // Inizializzazione del componente

        // Ottieni il contenitore dalla classe LayoutStandard
        Container container = getBody();
        container.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Benvenuto " + username.toUpperCase(Locale.ROOT), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(70, 130, 180));  // Colore blu
        container.add(titleLabel, BorderLayout.NORTH);

        // Pannello centrale
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));  // Aggiunto padding
        contentPanel.setBackground(Color.WHITE);  // Colore di sfondo bianco
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componenti per centri
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("Centri Collegati all'Operatore:"), gbc);

        listaCentriModel = new DefaultListModel<>();
        centriList = new JList<>(listaCentriModel);

        caricaCentri(); // Carica i centri all'avvio

        centriScrollPane.setPreferredSize(new Dimension(250, 150));
        centriScrollPane.add(new JScrollPane(centriList)); // Usa JScrollPane per il JList
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Centrare la lista
        contentPanel.add(centriScrollPane, gbc);

        // Bottone per creare un centro di monitoraggio
        creaCentroButton = new JButton("Crea Centro Monitoraggio");
        customizeButton(creaCentroButton);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(creaCentroButton, gbc);

        // Bottone per aggiungere parametri climatici
        aggiungiDatiClimatici = new JButton("Aggiungi Parametri Climatici");
        customizeButton(aggiungiDatiClimatici);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(aggiungiDatiClimatici, gbc);

        container.add(contentPanel, BorderLayout.CENTER);

        // Pannello inferiore per i bottoni di azione
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setBackground(Color.WHITE);
        salvaDatiButton = new JButton("Salva Dati");
        indietroButton = new JButton("Indietro");
        customizeButton(salvaDatiButton);
        customizeButton(indietroButton);

        actionPanel.add(indietroButton);
        actionPanel.add(salvaDatiButton);

        container.add(actionPanel, BorderLayout.SOUTH);

        // Aggiungi action listeners
        Gestore gestore = new Gestore();
        aggiungiDatiClimatici.addActionListener(gestore);
        salvaDatiButton.addActionListener(gestore);
        indietroButton.addActionListener(gestore);
        creaCentroButton.addActionListener(gestore);

        setVisible(true);
        titleLable = null;
    }

    private void caricaCentri() {
        listaCentriModel.clear(); // Svuota la lista esistente
        try {
            QueryExecutorImpl queryExecutor = new QueryExecutorImpl();
            List<String> centri = queryExecutor.getCentriPerOperatore(username); // Carica i centri dal database
            if (centri.isEmpty()) {
                listaCentriModel.addElement("Nessun centro disponibile");
            } else {
                for (String centro : centri) {
                    listaCentriModel.addElement(centro); // Aggiungi ogni centro al modello della lista
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento dei centri: " + ex.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apriFinestraCreaCentro() {
        JFrame createCenterFrame = new JFrame("Crea Centro Monitoraggio");
        createCenterFrame.setSize(400, 300);
        createCenterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createCenterFrame.setLocationRelativeTo(this);

        // Layout della finestra per creare il centro
        Container container = createCenterFrame.getContentPane();
        container.setLayout(new GridLayout(5, 2));
        container.setBackground(new Color(245, 245, 245));

        container.add(new JLabel("id:"));
        JTextField idField = new JTextField();
        container.add(idField);

        container.add(new JLabel("Nome Centro:"));
        JTextField nomeCentroField = new JTextField();
        container.add(nomeCentroField);

        container.add(new JLabel("Descrizione:"));
        JTextField descrizioneField = new JTextField();
        container.add(descrizioneField);

        JButton salvaCentroButton = new JButton("Salva Centro");
        salvaCentroButton.addActionListener(e -> {
            String id = idField.getText();
            String nomeCentro = nomeCentroField.getText();
            String descrizione = descrizioneField.getText();

            if (id.isEmpty() || nomeCentro.isEmpty() || descrizione.isEmpty()) {
                JOptionPane.showMessageDialog(createCenterFrame, "Tutti i campi sono obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                QueryExecutorImpl queryExecutor = new QueryExecutorImpl();
                boolean success = false;

                try {
                    success = queryExecutor.salvaCentroMonitoraggio(id, nomeCentro, descrizione);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(createCenterFrame, "Errore nel database: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                if (success) {
                    JOptionPane.showMessageDialog(createCenterFrame, "Centro monitoraggio creato con successo.");
                    caricaCentri(); // Ricarica la lista dei centri
                    createCenterFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(createCenterFrame, "Errore durante il salvataggio del centro.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        container.add(new JLabel());
        container.add(salvaCentroButton);

        createCenterFrame.setVisible(true);
    }

    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(220, 45));
        button.setBackground(new Color(34, 139, 34));  // Verde
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        button.setBorderPainted(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == aggiungiDatiClimatici) {
                // Apre la finestra dei dati climatici
            } else if (e.getSource() == salvaDatiButton) {
                JOptionPane.showMessageDialog(null, "Dati salvati.");
            } else if (e.getSource() == indietroButton) {
                dispose();
            } else if (e.getSource() == creaCentroButton) {
                apriFinestraCreaCentro();
            }
        }
    }
}

    public static void main(String[] args) {
        String usernameRegistrato = "nomeOperatore"; // Sostituisci con il nome dell'operatore
        new LayoutOperatore(usernameRegistrato);
    }
}
