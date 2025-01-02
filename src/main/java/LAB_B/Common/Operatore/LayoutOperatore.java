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
        // Aggiungi il bottone "Home" che è già stato creato in LayoutStandard
        container.add(home, BorderLayout.WEST);


        container.add(actionPanel, BorderLayout.SOUTH);

        // Aggiungi action listeners
        Gestore gestore = new Gestore();
        aggiungiDatiClimatici.addActionListener(gestore);
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

    private void apriFinestraDatiClimatici() {

        JFrame datiClimaticiFrame = new JFrame("Dati Climatici");
        datiClimaticiFrame.setSize(600, 600); // Aumentata la dimensione per dare più spazio
        datiClimaticiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        datiClimaticiFrame.setLocationRelativeTo(this);

        // Layout e componenti della finestra
        Container container = datiClimaticiFrame.getContentPane();
        container.setLayout(new BorderLayout());

        JLabel label = new JLabel("Inserisci Parametri Climatici", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        container.add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dropdown per selezionare il centro
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Seleziona Centro:"), gbc);

        JComboBox<String> centriDropdown = new JComboBox<>();
        for (int i = 0; i < listaCentriModel.size(); i++) {
            centriDropdown.addItem(listaCentriModel.getElementAt(i));
        }
        if (listaCentriModel.isEmpty()) {
            centriDropdown.addItem("Nessun centro disponibile");
        }
        gbc.gridx = 1;
        panel.add(centriDropdown, gbc);

        // Parametri climatici con score e area di testo per severità
        String[] parametri = {"Velocità Vento", "Temperatura", "Umidità", "Precipitazioni"};
        JComboBox<Integer>[] scoreDropdowns = new JComboBox[parametri.length]; // Array per i dropdown di score
        JTextArea[] severitaTextAreas = new JTextArea[parametri.length]; // Array per le JTextArea

// Campo per l'inserimento del valore della chiave primaria "key"
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Key (ID univoco):"), gbc);

        JTextField keyField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(keyField, gbc);

// Loop per aggiungere i parametri climatici
        for (int i = 0; i < parametri.length; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            panel.add(new JLabel(parametri[i] + " (Score 1-5):"), gbc);

            // Dropdown per il punteggio (Score 1-5)
            scoreDropdowns[i] = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            gbc.gridx = 1;
            panel.add(scoreDropdowns[i], gbc);

            // Area di testo per inserire la severità
            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("Note (max 256 caratteri) " + parametri[i] + ":"), gbc);

            severitaTextAreas[i] = new JTextArea(3, 20); // 3 righe e 20 colonne
            severitaTextAreas[i].setLineWrap(true); // Abilita il wrapping del testo
            severitaTextAreas[i].setWrapStyleWord(true); // Parola intera a capo
            severitaTextAreas[i].setDocument(new javax.swing.text.PlainDocument() {
                @Override
                public void insertString(int offs, String str, javax.swing.text.AttributeSet a) {
                    if (getLength() + str.length() <= 256) {  // Limita a 256 caratteri
                        try {
                            super.insertString(offs, str, a);
                        } catch (BadLocationException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            JScrollPane scrollPane = new JScrollPane(severitaTextAreas[i]);
            scrollPane.setPreferredSize(new Dimension(200, 60)); // Ridimensionato per una visualizzazione ottimale
            gbc.gridx = 1;
            panel.add(scrollPane, gbc);
        }



        // Pulsante "Salva Parametri"
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton salvaParametriButton = new JButton("Salva Parametri");
        panel.add(salvaParametriButton, gbc);

        salvaParametriButton.addActionListener(e -> {
            // Validazione dei dati
            String key = keyField.getText();
            String centro = (String) centriDropdown.getSelectedItem();
            if (key.isEmpty() || centro == null || centro.equals("Nessun centro disponibile")) {
                JOptionPane.showMessageDialog(datiClimaticiFrame, "Compila tutti i campi obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Raccolta dei dati inseriti
            StringBuilder datiInseriti = new StringBuilder("Key: " + key + "\nCentro: " + centro + "\n");
            for (int i = 0; i < parametri.length; i++) {
                datiInseriti.append(parametri[i])
                        .append(" - Score: ")
                        .append(scoreDropdowns[i].getSelectedItem())
                        .append(", Note: ")
                        .append(severitaTextAreas[i].getText())
                        .append("\n");
            }

            QueryExecutorImpl q = new QueryExecutorImpl();
            try {
                q.salvaDatiClimatici(key,centro,scoreDropdowns,severitaTextAreas);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            // Mostra un messaggio di conferma
            JOptionPane.showMessageDialog(datiClimaticiFrame, "Dati salvati localmente:\n" + datiInseriti, "Successo", JOptionPane.INFORMATION_MESSAGE);
            datiClimaticiFrame.dispose();
        });

        container.add(panel, BorderLayout.CENTER);
        datiClimaticiFrame.setVisible(true);
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
                    success = queryExecutor.salvaCentroMonitoraggio(id, nomeCentro, descrizione,username);
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
        QueryExecutorImpl q = new QueryExecutorImpl();
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
                apriFinestraDatiClimatici();
                QueryExecutorImpl q = new QueryExecutorImpl();
            } else if (e.getSource() == creaCentroButton) {
                apriFinestraCreaCentro();
            }
        }
    }
}
