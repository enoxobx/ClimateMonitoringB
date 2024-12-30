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
    private JLabel titleLable;

    public LayoutOperatore(String username) {
        super(); // Chiamata al costruttore della classe padre LayoutStandard
        this.username = username;


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
        JScrollPane centriScrollPane = new JScrollPane(centriList);
        centriScrollPane.setPreferredSize(new Dimension(250, 150));  // Ridimensionato per un aspetto più equilibrato
        gbc.gridx = 1;
        contentPanel.add(centriScrollPane, gbc);

        // Bottone per creare un centro di monitoraggio
        creaCentroButton = new JButton("Crea Centro Monitoraggio");
        customizeButton(creaCentroButton);
        gbc.gridx = 1;
        gbc.gridy = 1;  // Cambiato la riga per farlo apparire prima
        contentPanel.add(creaCentroButton, gbc);

        // Bottone per aggiungere parametri climatici
        aggiungiDatiClimatici = new JButton("Aggiungi Parametri Climatici");
        customizeButton(aggiungiDatiClimatici);
        gbc.gridx = 1;
        gbc.gridy = 2;  // Cambiato la riga per farlo apparire dopo
        contentPanel.add(aggiungiDatiClimatici, gbc);

        container.add(contentPanel, BorderLayout.CENTER);

        // Pannello inferiore per i bottoni di azione
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setBackground(Color.WHITE);  // Sfondo bianco per il pannello inferiore
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

        // Recupera i centri associati all'operatore e aggiorna la lista
        aggiornaCentriAssociati();
    }

    // Metodo per recuperare i centri associati all'operatore
    private List<String> recuperaCentriAssociati(String username) {
        QueryExecutorImpl queryExecutor = new QueryExecutorImpl();
        try {
            return queryExecutor.getCentriPerOperatore(username);  // Recupera i centri associati all'operatore
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore durante il recupero dei centri: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            return List.of();  // Restituisce una lista vuota in caso di errore
        }
    }

    // Metodo per aggiornare la lista dei centri associati
    private void aggiornaCentriAssociati() {
        List<String> centriAssociati = recuperaCentriAssociati(username);
        listaCentriModel.clear();  // Pulisce la lista precedente
        for (String centro : centriAssociati) {
            listaCentriModel.addElement(centro);  // Aggiunge i centri associati alla lista
        }
    }

    // Metodo per personalizzare i bottoni
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

        container.add(panel, BorderLayout.CENTER);



        // Bottone per salvare i parametri
        JButton salvaButton = new JButton("Salva Parametri");
        salvaButton.setPreferredSize(new Dimension(150, 40));
        salvaButton.setBackground(new Color(34, 139, 34));
        salvaButton.setForeground(Color.WHITE);
        salvaButton.setFont(new Font("Arial", Font.BOLD, 14));
        salvaButton.addActionListener(e -> {
            if (centriDropdown.getSelectedItem() == null || centriDropdown.getSelectedItem().equals("Nessun centro disponibile")) {
                JOptionPane.showMessageDialog(datiClimaticiFrame, "Seleziona un centro prima di salvare!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                // Salvataggio dei dati e validazione
                StringBuilder result = new StringBuilder("Parametri salvati:\n");
                result.append("Centro: ").append(centriDropdown.getSelectedItem()).append("\n");

                for (int i = 0; i < parametri.length; i++) {
                    result.append(parametri[i]).append(": ");
                    result.append("Score: ").append(scoreDropdowns[i].getSelectedItem()).append(", ");
                    result.append("Severità: ").append(severitaTextAreas[i].getText()).append("\n");
                }

                JOptionPane.showMessageDialog(datiClimaticiFrame, result.toString(), "Parametri salvati con successo!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        container.add(salvaButton, BorderLayout.SOUTH);

        datiClimaticiFrame.setVisible(true);
    }



    // Metodo per aggiornare lo username (se necessario)
    public void aggiornaUsername(String nuovoUsername) {
        titleLable.setText("Benvenuto " + nuovoUsername.toUpperCase(Locale.ROOT));
    }




    // Metodo per aprire la finestra di creazione del centro
    private void apriFinestraCreaCentro() {
        JFrame createCenterFrame = new JFrame("Crea Centro Monitoraggio");
        createCenterFrame.setSize(400, 300);
        createCenterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createCenterFrame.setLocationRelativeTo(this);

        // Layout della finestra per creare il centro
        Container container = createCenterFrame.getContentPane();
        container.setLayout(new GridLayout(5, 2));
        container.setBackground(new Color(245, 245, 245)); // Colore chiaro per lo sfondo



        // Aggiungi i campi per creare il centro di monitoraggio
        container.add(new JLabel("id:"));
        JTextField nomeCentroField = new JTextField();
        container.add(nomeCentroField);

        container.add(new JLabel("nomeCentro"));
        JTextField descrizioneField = new JTextField();
        container.add(descrizioneField);

        container.add(new JLabel("descrizione"));
        JTextField posizioneField = new JTextField();
        container.add(posizioneField);

        // Bottone per salvare il centro
        JButton salvaCentroButton = new JButton("Salva Centro");
        salvaCentroButton.addActionListener(e -> {
            String nomeCentro = nomeCentroField.getText();
            String descrizione = descrizioneField.getText();
            String id = posizioneField.getText();

            if (nomeCentro.isEmpty() || descrizione.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(createCenterFrame, "Tutti i campi sono obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                QueryExecutorImpl queryExecutor = new QueryExecutorImpl();
                boolean success = false;

                try {
                    success = queryExecutor.salvaCentroMonitoraggio(id,nomeCentro, descrizione, username);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(createCenterFrame, "Errore nel database: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                if (success) {
                    listaCentriModel.addElement(nomeCentro);
                    JOptionPane.showMessageDialog(createCenterFrame, "Centro monitoraggio creato con successo.");
                    createCenterFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(createCenterFrame, "Errore durante il salvataggio del centro.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        container.add(new JLabel()); // Spazio vuoto per il layout
        container.add(salvaCentroButton);

        createCenterFrame.setVisible(true);
    }


    // Classe per la gestione degli eventi
    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == aggiungiDatiClimatici) {
                apriFinestraDatiClimatici(); // Apri la finestra dei dati climatici
            } else if (e.getSource() == salvaDatiButton) {
                JOptionPane.showMessageDialog(null, "Dati salvati.");
            } else if (e.getSource() == indietroButton) {
                dispose();
            } else if (e.getSource() == creaCentroButton) {
                apriFinestraCreaCentro(); // Apri la finestra per creare un centro
            }
        }
    }


    public static void main(String[] args) {
        // Questo username dovrebbe provenire da un altro contesto, ad esempio da una classe di registrazione
        String usernameRegistrato = "nomeOperatore";  // Sostituisci con il nome dell'operatore registrato
        new LayoutOperatore(usernameRegistrato);
    }
}
