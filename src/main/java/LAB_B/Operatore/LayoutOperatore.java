package LAB_B.Operatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LayoutOperatore extends JFrame {

    private String username;
    private JButton aggiungiCentro, aggiungiDatiClimatici, salvaDatiButton, indietroButton, creaCentroButton;
    private JTextField centroTextField;
    private JList<String> centriList;
    private DefaultListModel<String> listaCentriModel;

    public LayoutOperatore(String username) {
        this.username = username;

        // Impostazioni generali della finestra
        setTitle("Operatore Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principale
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Pannello in alto con il titolo
        JLabel titleLabel = new JLabel("Benvenuto Operatore: " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        container.add(titleLabel, BorderLayout.NORTH);

        // Pannello centrale
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        centriScrollPane.setPreferredSize(new Dimension(200, 150));
        gbc.gridx = 1;
        contentPanel.add(centriScrollPane, gbc);

        // Campo per aggiungere un nuovo centro
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(new JLabel("Nome Centro:"), gbc);

        centroTextField = new JTextField(20);
        gbc.gridx = 1;
        contentPanel.add(centroTextField, gbc);

        // Bottone per aggiungere un centro
        aggiungiCentro = new JButton("Aggiungi Centro");
        customizeButton(aggiungiCentro);
        gbc.gridx = 2;
        contentPanel.add(aggiungiCentro, gbc);

        // Bottone per aggiungere parametri climatici
        aggiungiDatiClimatici = new JButton("Aggiungi Parametri Climatici");
        customizeButton(aggiungiDatiClimatici);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(aggiungiDatiClimatici, gbc);

        // Bottone per creare un centro di monitoraggio
        creaCentroButton = new JButton("Crea Centro Monitoraggio");
        customizeButton(creaCentroButton);
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(creaCentroButton, gbc);

        container.add(contentPanel, BorderLayout.CENTER);

        // Pannello inferiore per i bottoni di azione
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        salvaDatiButton = new JButton("Salva Dati");
        indietroButton = new JButton("Indietro");
        customizeButton(salvaDatiButton);
        customizeButton(indietroButton);

        actionPanel.add(indietroButton);
        actionPanel.add(salvaDatiButton);

        container.add(actionPanel, BorderLayout.SOUTH);

        // Aggiungi action listeners
        Gestore gestore = new Gestore();
        aggiungiCentro.addActionListener(gestore);
        aggiungiDatiClimatici.addActionListener(gestore);
        salvaDatiButton.addActionListener(gestore);
        indietroButton.addActionListener(gestore);
        creaCentroButton.addActionListener(gestore);

        setVisible(true);
    }

    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
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

        // Aggiungi i campi per creare il centro di monitoraggio
        container.add(new JLabel("Nome Centro:"));
        JTextField nomeCentroField = new JTextField();
        container.add(nomeCentroField);

        container.add(new JLabel("Descrizione Centro:"));
        JTextField descrizioneField = new JTextField();
        container.add(descrizioneField);

        container.add(new JLabel("Posizione (Lat, Long):"));
        JTextField posizioneField = new JTextField();
        container.add(posizioneField);

        // Bottone per salvare il centro
        JButton salvaCentroButton = new JButton("Salva Centro");
        salvaCentroButton.addActionListener(e -> {
            String nomeCentro = nomeCentroField.getText();
            String descrizione = descrizioneField.getText();
            String posizione = posizioneField.getText();

            if (nomeCentro.isEmpty() || descrizione.isEmpty() || posizione.isEmpty()) {
                JOptionPane.showMessageDialog(createCenterFrame, "Tutti i campi sono obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                // Aggiungi il centro alla lista (o database se desiderato)
                listaCentriModel.addElement(nomeCentro);
                JOptionPane.showMessageDialog(this, "Centro monitoraggio creato con successo.");
                createCenterFrame.dispose();
            }
        });

        container.add(salvaCentroButton);

        createCenterFrame.setVisible(true);
    }

    // Classe per la gestione degli eventi
    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == aggiungiCentro) {
                String nuovoCentro = centroTextField.getText();
                if (!nuovoCentro.isEmpty()) {
                    listaCentriModel.addElement(nuovoCentro);
                    JOptionPane.showMessageDialog(null, "Centro aggiunto: " + nuovoCentro);
                }
            } else if (e.getSource() == aggiungiDatiClimatici) {
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

    /**
     * Metodo per aprire la finestra dei dati climatici.
     */
    private void apriFinestraDatiClimatici() {
        JFrame datiClimaticiFrame = new JFrame("Dati Climatici");
        datiClimaticiFrame.setSize(600, 500);
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

        // Parametri climatici con score
        String[] parametri = {"Velocità Vento", "Temperatura", "Umidità", "Precipitazioni"};
        JTextArea[] areeTesto = new JTextArea[parametri.length]; // Array per le aree di testo
        for (int i = 0; i < parametri.length; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            panel.add(new JLabel(parametri[i] + " (Score 1-5):"), gbc);

            JComboBox<Integer> scoreDropdown = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            gbc.gridx = 1;
            panel.add(scoreDropdown, gbc);

            // Aggiungi area di testo per severità
            gbc.gridy++;
            gbc.gridx = 0;
            panel.add(new JLabel("Severità per " + parametri[i] + ":"), gbc);

            JTextArea areaTesto = new JTextArea(3, 20);
            areaTesto.setLineWrap(true);
            areaTesto.setWrapStyleWord(true);
            areaTesto.setDocument(new LimitedDocument(256)); // Limite a 256 caratteri
            areeTesto[i] = areaTesto;

            JScrollPane scrollPane = new JScrollPane(areaTesto);
            gbc.gridx = 1;
            panel.add(scrollPane, gbc);
        }

        container.add(panel, BorderLayout.CENTER);

        JButton salvaButton = new JButton("Salva Parametri");
        salvaButton.setPreferredSize(new Dimension(150, 40));
        salvaButton.setBackground(new Color(34, 139, 34));
        salvaButton.setForeground(Color.WHITE);
        salvaButton.setFont(new Font("Arial", Font.BOLD, 14));
        salvaButton.addActionListener(e -> {
            if (centriDropdown.getSelectedItem() == null || centriDropdown.getSelectedItem().equals("Nessun centro disponibile")) {
                JOptionPane.showMessageDialog(datiClimaticiFrame, "Seleziona un centro prima di salvare!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(datiClimaticiFrame, "Parametri salvati con successo!");
            }
        });
        container.add(salvaButton, BorderLayout.SOUTH);

        datiClimaticiFrame.setVisible(true);
    }

    // Classe per limitare il numero di caratteri nelle aree di testo
    public class LimitedDocument extends javax.swing.text.PlainDocument {
        private int limit;

        public LimitedDocument(int limit) {
            this.limit = limit;
        }

        @Override
        public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
            if (str != null && (getLength() + str.length()) <= limit) {
                super.insertString(offs, str, a);
            }
        }
    }

    public static void main(String[] args) {
        new LayoutOperatore("Operatore1");
    }
}
