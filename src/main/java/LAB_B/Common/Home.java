package LAB_B.Common;

import LAB_B.Client.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe Home - Finestra principale dell'applicazione.
 * Permette di scegliere se accedere come "Cittadino" o "Operatore".
 */
public class Home extends JFrame {
    private JButton bottoneCittadino;
    private JButton bottoneOperatore;

    /**
     * Costruttore della finestra principale Home.
     */
    public Home() {
        super("Monitoraggio Clima");

        // Impostazioni base della finestra
        setSize(400, 400); // Dimensioni uniformi
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout principale
        Container corpo = getContentPane();
        corpo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        corpo.setBackground(new Color(240, 240, 240)); // Colore di sfondo chiaro

        // Aggiungi un titolo alla finestra
        JLabel titolo = new JLabel("Benvenuto nel Monitoraggio Clima");
        titolo.setFont(new Font("Arial", Font.BOLD, 20)); // Font più grande e grassetto
        titolo.setHorizontalAlignment(JLabel.CENTER); // Centra il titolo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0); // Margine sopra e sotto il titolo
        corpo.add(titolo, gbc);

        // Crea e configura il bottone "Cittadino"
        bottoneCittadino = new JButton("Cittadino");
        bottoneCittadino.setPreferredSize(new Dimension(200, 50));
        bottoneCittadino.setFont(new Font("Arial", Font.PLAIN, 16));
        bottoneCittadino.setBackground(new Color(100, 200, 255)); // Colore di sfondo
        bottoneCittadino.setForeground(Color.WHITE); // Colore del testo
        bottoneCittadino.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Bordi
        bottoneCittadino.setFocusPainted(false); // Rimuove il focus da bordi
        bottoneCittadino.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambia cursore quando ci si passa sopra

        // Aggiungi un'icona al bottone Cittadino (se presente il file immagine)
        ImageIcon cittadinoIcon = new ImageIcon("path_to_icon/cittadino_icon.png"); // Cambia il path in base all'icona
        bottoneCittadino.setIcon(cittadinoIcon);
        bottoneCittadino.setHorizontalTextPosition(SwingConstants.CENTER); // Centra il testo sotto l'icona

        // Aggiungi il bottone "Cittadino" al layout
        gbc.gridx = 0;
        gbc.gridy = 1;
        corpo.add(bottoneCittadino, gbc);
        bottoneCittadino.addActionListener(new Gestore());

        // Crea e configura il bottone "Operatore"
        bottoneOperatore = new JButton("Operatore");
        bottoneOperatore.setPreferredSize(new Dimension(200, 50));
        bottoneOperatore.setFont(new Font("Arial", Font.PLAIN, 16));
        bottoneOperatore.setBackground(new Color(100, 200, 255)); // Colore di sfondo
        bottoneOperatore.setForeground(Color.WHITE); // Colore del testo
        bottoneOperatore.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Bordi
        bottoneOperatore.setFocusPainted(false); // Rimuove il focus da bordi
        bottoneOperatore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambia cursore quando ci si passa sopra

        // Aggiungi un'icona al bottone Operatore (se presente il file immagine)
        ImageIcon operatoreIcon = new ImageIcon("path_to_icon/operatore_icon.png"); // Cambia il path in base all'icona
        bottoneOperatore.setIcon(operatoreIcon);
        bottoneOperatore.setHorizontalTextPosition(SwingConstants.CENTER); // Centra il testo sotto l'icona

        // Aggiungi il bottone "Operatore" al layout
        gbc.gridx = 0;
        gbc.gridy = 2;
        corpo.add(bottoneOperatore, gbc);
        bottoneOperatore.addActionListener(new Gestore());

        setVisible(true); // Rendi visibile la finestra
    }

    // Gestore degli eventi per i bottoni
    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bottoneCittadino) {
                // Azione per il pulsante Cittadino
                /*private void gestisciPulsanteCittadino() {
                    new Thread(() -> {
                        try (Socket socket = new Socket("localhost", 12345);
                             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                            out.println("REGISTER:cittadino");
                            out.println("GET_CLIMATE_DATA");

                            String risposta = in.readLine();

                            SwingUtilities.invokeLater(() -> {
                                LayoutCittadino layout = new LayoutCittadino(risposta);
                                layout.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                layout.setLocationRelativeTo(null);
                                layout.setVisible(true);
                                dispose();
                            });

                        } catch (IOException ex) {
                            ex.printStackTrace();
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Errore di connessione al server!"));
                        }
                    }).start();
                }*/
                System.err.println("Funzionalità Cittadino non ancora implementata.");
            } else if (e.getSource() == bottoneOperatore) {
                // Azione per il pulsante Operatore
                Login finestraOperatore = new Login();
                finestraOperatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                finestraOperatore.setLocationRelativeTo(null); // Centra la finestra di login
                finestraOperatore.setVisible(true);
                dispose(); // Chiudi la finestra principale
            }
        }
    }

    public static void main(String[] args) {
        new Home(); // Crea e mostra la finestra principale
    }
}