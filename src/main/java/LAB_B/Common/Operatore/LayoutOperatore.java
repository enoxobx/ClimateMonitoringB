package LAB_B.Common.Operatore;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import LAB_B.Common.*;
import LAB_B.Common.Interface.*;

public class LayoutOperatore extends LayoutStandard {

    private final String username;
    private final JButton aggiungiDatiClimatici;
    private final JButton creaCentroButton;
    private final JList<String> centriList;
    private final DefaultListModel<String> listaCentriModel;
    private final JLabel titleLable;
    private final ScrollPane centriScrollPane;
    private final JComboBox<String> cittaDropdown = new JComboBox<>();
    private final JComboBox<String> centriDropdown = new JComboBox<>();

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
        titleLabel.setForeground(new Color(70, 130, 180)); // Colore blu
        container.add(titleLabel, BorderLayout.NORTH);

        // Pannello centrale
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(Color.WHITE);
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

        JScrollPane centriScrollPane = new JScrollPane(centriList);
        centriScrollPane.setPreferredSize(new Dimension(300, 150));
        gbc.gridy = 1;
        contentPanel.add(centriScrollPane, gbc);


        // Bottone per creare un centro di monitoraggio
        // Bottone Crea Centro Monitoraggio
        creaCentroButton = new JButton("Crea Centro Monitoraggio");
        customizeButton(creaCentroButton);
        gbc.gridy = 2;
        contentPanel.add(creaCentroButton, gbc);

        // Bottone Aggiungi Dati Climatici
        aggiungiDatiClimatici = new JButton("Aggiungi Dati Climatici");
        customizeButton(aggiungiDatiClimatici);
        gbc.gridy = 3;
        contentPanel.add(aggiungiDatiClimatici, gbc);

        container.add(contentPanel, BorderLayout.CENTER);


        container.add(contentPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.add(home);
        container.add(footerPanel, BorderLayout.SOUTH);

        // Aggiungi action listeners
        Gestore gestore = new Gestore();
        aggiungiDatiClimatici.addActionListener(gestore);
        creaCentroButton.addActionListener(gestore);

        setVisible(true);
        titleLable = null;
    }

    private void caricaCentri() {
        listaCentriModel.clear();
        try {
            List<String> centri = db.getCentriPerOperatore(username);
            if (centri.isEmpty()) {
                listaCentriModel.addElement("Nessun centro disponibile");
            } else {
                centri.forEach(listaCentriModel::addElement);

                // Se ci sono centri, seleziona automaticamente il primo
                centriList.setSelectedIndex(0);  // Seleziona il primo centro
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Errore durante il caricamento dei centri: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apriFinestraDatiClimatici() {
        JFrame parametroFrame = new JFrame("Inserisci Dati Parametro");
        parametroFrame.setSize(1000, 800);   // Imposta la dimensione della finestra
        parametroFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        parametroFrame.setLocationRelativeTo(this);

        String[] fields = { "Wind", "Humidity", "Pressure", "Temperature", "Precipitation", "Glacier Altitude", "Glacier Mass" };
        String[] comments = { "Wind Comment", "Humidity Comment", "Pressure Comment", "Temperature Comment", "Precipitation Comment", "Glacier Altitude Comment", "Glacier Mass Comment" };
        String[] units = { "km/h", "%", "hPa", "°C", "mm", "m", "kg" };

        Container container = parametroFrame.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Inserisci Dati della Tabella Parametro", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 51, 102));
        container.add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 255, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(new JLabel("Parametro"), gbc);
        gbc.gridx++;
        panel.add(new JLabel("Valore"), gbc);
        gbc.gridx++;
        panel.add(new JLabel("Unità"), gbc);
        gbc.gridx++;
        panel.add(new JLabel("Commento (Max 256 caratteri)"), gbc);
        gbc.gridx++;
        panel.add(new JLabel("Punteggio"), gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JTextField[] fieldInputs = new JTextField[fields.length];
        JTextArea[] commentInputs = new JTextArea[comments.length];
        JComboBox<Integer>[] scoreDropdowns = new JComboBox[fields.length];

        // DocumentFilter for numeric input
        DocumentFilter numericFilter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        };

        // Add the input fields dynamically for each parameter
        for (int i = 0; i < fields.length; i++) {
            gbc.gridx = 0;
            panel.add(new JLabel(fields[i]), gbc);
            gbc.gridx++;

            fieldInputs[i] = new JTextField(10);
            ((AbstractDocument) fieldInputs[i].getDocument()).setDocumentFilter(numericFilter);
            panel.add(fieldInputs[i], gbc);
            gbc.gridx++;

            panel.add(new JLabel(units[i]), gbc);
            gbc.gridx++;

            // Comment input is optional
            commentInputs[i] = new JTextArea(3, 10);
            commentInputs[i].setLineWrap(true);
            commentInputs[i].setWrapStyleWord(true);
            commentInputs[i].setDocument(new PlainDocument() {
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if (getLength() + str.length() <= 255) {
                        super.insertString(offs, str, a);
                    }
                }
            });
            JScrollPane commentScrollPane = new JScrollPane(commentInputs[i]);
            panel.add(commentScrollPane, gbc);
            gbc.gridx++;

            scoreDropdowns[i] = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });
            panel.add(scoreDropdowns[i], gbc);
            gbc.gridx = 0;
            gbc.gridy++;
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 255, 255));

        JButton salvaButton = new JButton("Salva Parametri");
        salvaButton.addActionListener(e -> {
            try {
                // Recupera i valori dai campi di input
                for (int i = 0; i < fields.length; i++) {
                    String valore = fieldInputs[i].getText();  // Valore del parametro (ad esempio, wind, humidity, etc.)
                    String commento = commentInputs[i].getText().isEmpty() ? null : commentInputs[i].getText();  // Commento (opzionale)
                    int punteggio = (int) scoreDropdowns[i].getSelectedItem();  // Punteggio selezionato

                    // Verifica che il valore non sia vuoto
                    if (valore.isEmpty()) {
                        JOptionPane.showMessageDialog(parametroFrame, "Per favore, inserisci tutti i campi.");
                        return;  // Uscita se un campo obbligatorio è vuoto
                    }

                    // Chiamata al metodo salvaDatiClimatici per ogni parametro
                    boolean successo = db.salvaDatiClimatici(fields[i], valore, commento, punteggio, username, System.currentTimeMillis());

                    if (!successo) {
                        JOptionPane.showMessageDialog(parametroFrame, "Errore nel salvataggio dei dati.");
                        return;
                    }
                }

                // Conferma che i dati sono stati salvati con successo
                JOptionPane.showMessageDialog(parametroFrame, "Dati salvati con successo!");
                parametroFrame.dispose();  // Chiudi la finestra dopo il salvataggio

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parametroFrame, "Errore nel salvataggio dei dati: " + ex.getMessage());
            }
        });

        // Pulsante Indietro
        JButton indietroButton = new JButton("Indietro");
        indietroButton.setFont(new Font("Arial", Font.BOLD, 14));  // Font personalizzato per il pulsante
        indietroButton.setBackground(new Color(255, 99, 71));  // Colore di sfondo rosso per il pulsante
        indietroButton.setForeground(Color.WHITE);  // Colore testo bianco
        indietroButton.setFocusPainted(false);
        indietroButton.setBorderPainted(false);
        indietroButton.setPreferredSize(new Dimension(150, 40));  // Imposta dimensioni personalizzate
        indietroButton.addActionListener(e -> parametroFrame.dispose());  // Chiude la finestra quando cliccato

        // Aggiungi i pulsanti al pannello
        buttonPanel.add(salvaButton);
        buttonPanel.add(indietroButton);

        container.add(buttonPanel, BorderLayout.SOUTH);  // Aggiungi il pannello dei pulsanti in basso

        // Aggiungi il pannello dei parametri a uno JScrollPane per consentire lo scorrimento
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        container.add(scrollPane, BorderLayout.CENTER);

        // Mostra la finestra
        parametroFrame.setVisible(true);
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

        container.add(new JLabel("Nome Centro:"));
        JTextField nomeCentroField = new JTextField();
        container.add(nomeCentroField);

        container.add(new JLabel("indirizzo:"));
        JTextField indirizzoField = new JTextField();
        container.add(indirizzoField);

        JButton salvaCentroButton = new JButton("Salva Centro");
        salvaCentroButton.addActionListener(e -> {
            String nomeCentro = nomeCentroField.getText();
            String indirizzo = indirizzoField.getText();

            if (nomeCentro.isEmpty() || indirizzo.isEmpty()) {
                JOptionPane.showMessageDialog(createCenterFrame, "Tutti i campi sono obbligatori", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                boolean success = false;

                try {

                    success = db.salvaCentroMonitoraggio(nomeCentro, indirizzo, username);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(createCenterFrame, "Errore nel database: " + ex.getMessage(),
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }

                if (success) {
                    JOptionPane.showMessageDialog(createCenterFrame, "Centro monitoraggio creato con successo.");
                    caricaCentri(); // Ricarica la lista dei centri
                    createCenterFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(createCenterFrame, "Errore durante il salvataggio del centro.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        container.add(new JLabel());
        container.add(salvaCentroButton);
        createCenterFrame.setVisible(true);

    }

    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(250, 40));
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == creaCentroButton) {
                apriFinestraCreaCentro();
            } else if (e.getSource() == aggiungiDatiClimatici) {
                apriFinestraDatiClimatici();
            }
        }
    }
}
