package LAB_B.Operatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LayoutOperatore extends JFrame {

    private String username;
    private JButton aggiungiCentro, aggiungiArea, aggiorna, pulsante;
    private JTextField area, aCentro;
    private JComboBox<String> listaComboBox, listaComboBoxA;
    private DefaultListModel<String> listaC, listaA;
    private JList<String> centriM, centriA;

    public LayoutOperatore(String username) {
        this.username = username;

        // Impostazioni generali della finestra
        setTitle("Operatore Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principale della finestra
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Pannello in alto con il titolo
        JLabel titleLabel = new JLabel("Benvenuto Operatore: " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        container.add(titleLabel, BorderLayout.NORTH);

        // Pannello centrale per i vari contenuti
        JPanel contentPanel = new JPanel(new GridBagLayout());  // Cambiato a GridBagLayout
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margini tra i componenti
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Aggiungi bottoni e campi per la gestione
        pulsante = new JButton("AGGIUNGI DATI CLIMATICI");
        aggiorna = new JButton("Aggiorna Centri");
        aggiungiCentro = new JButton("Aggiungi Centro Monitoraggio");
        aggiungiArea = new JButton("Aggiungi Area di Interesse");
        area = new JTextField("Inserisci area da aggiungere");
        aCentro = new JTextField("Inserisci centro da aggiungere");

        // ComboBox per centri e aree
        listaC = new DefaultListModel<>();
        listaA = new DefaultListModel<>();
        centriM = new JList<>(listaC);
        centriA = new JList<>(listaA);
        listaComboBox = new JComboBox<>(new String[]{}); // Aggiungere centri dinamicamente
        listaComboBoxA = new JComboBox<>(new String[]{}); // Aggiungere aree dinamicamente

        // Aggiungi action listeners
        pulsante.addActionListener(new Gestore());
        aggiorna.addActionListener(new Gestore());
        aggiungiCentro.addActionListener(new Gestore());
        aggiungiArea.addActionListener(new Gestore());

        // Aggiungi componenti al pannello centrale
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(new JScrollPane(listaComboBox), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(pulsante, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        contentPanel.add(new JScrollPane(listaComboBoxA), gbc);

        gbc.gridy = 1;
        contentPanel.add(aggiorna, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(aCentro, gbc);

        gbc.gridy = 3;
        contentPanel.add(area, gbc);

        gbc.gridx = 1;
        contentPanel.add(aggiungiCentro, gbc);

        gbc.gridx = 2;
        contentPanel.add(aggiungiArea, gbc);

        container.add(contentPanel, BorderLayout.CENTER);

        // Pannello inferiore per il logout
        JPanel bottomPanel = new JPanel();
        JButton logoutButton = new JButton("Logout");
        bottomPanel.add(logoutButton);
        container.add(bottomPanel, BorderLayout.SOUTH);

        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler effettuare il logout?", "Conferma Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new Login();
            }
        });

        setVisible(true);
    }

    // Gestore per le azioni dei pulsanti
    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == pulsante) {
                String centro = (String) listaComboBox.getSelectedItem();
                String areaSelezionata = (String) listaComboBoxA.getSelectedItem();
                System.out.println("Centro: " + centro + ", Area: " + areaSelezionata);
                new DatiClimatici(username, centro, areaSelezionata);
            } else if (e.getSource() == aggiungiCentro) {
                String nuovoCentro = aCentro.getText();
                if (!nuovoCentro.isEmpty() && !nuovoCentro.equals("Inserisci centro da aggiungere")) {
                    aggiungiCentroMonitoraggio(nuovoCentro);
                }
            } else if (e.getSource() == aggiungiArea) {
                String areaDaAggiungere = area.getText();
                String centro = aCentro.getText();
                if (!areaDaAggiungere.isEmpty() && !areaDaAggiungere.equals("Inserisci area da aggiungere")) {
                    aggiungiAreaDiInteresse(centro, areaDaAggiungere);
                }
            } else if (e.getSource() == aggiorna) {
                aggiornaCentri();
            }
        }

        private void aggiungiCentroMonitoraggio(String centro) {
            System.out.println("Aggiungi centro: " + centro);
        }

        private void aggiungiAreaDiInteresse(String centro, String area) {
            System.out.println("Aggiungi area: " + area + " al centro: " + centro);
        }

        private void aggiornaCentri() {
            System.out.println("Aggiorna lista dei centri");
            listaC.addElement("Centro Aggiornato");
        }
    }

    public static void main(String[] args) {
        String username = "operatore1";
        new LayoutOperatore(username);
    }
}
