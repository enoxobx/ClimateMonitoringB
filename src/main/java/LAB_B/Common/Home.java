package LAB_B.Common;

import LAB_B.Common.Operatore.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe Home - Finestra principale dell'applicazione.
 * Permette di scegliere se accedere come "Cittadino" o "Operatore".
 */
public class Home extends JFrame {
    // Dichiarazione dei bottoni per le due opzioni: "Cittadino" e "Operatore"
    private final JButton bottoneCittadino;
    private final JButton bottoneOperatore;

    /**
     * Costruttore della finestra principale Home.
     */
    public Home() {
        super("Monitoraggio Clima"); // Imposta il titolo della finestra principale

        // Impostazioni base della finestra
        setSize(400, 400); // Imposta la dimensione della finestra (400x400 pixel)
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Quando la finestra viene chiusa, l'applicazione termina

        // Layout principale per posizionare i componenti nella finestra
        Container corpo = getContentPane(); // Ottiene il contenitore della finestra
        corpo.setLayout(new GridBagLayout()); // Imposta il layout GridBagLayout per una disposizione flessibile dei componenti
        GridBagConstraints gbc = new GridBagConstraints(); // Crea un oggetto per gestire le posizioni e le restrizioni dei componenti nel layout
        corpo.setBackground(new Color(240, 240, 240)); // Imposta il colore di sfondo della finestra

        // Aggiungi un titolo alla finestra
        JLabel titolo = new JLabel("Benvenuto nel Monitoraggio Clima");
        titolo.setFont(new Font("Arial", Font.BOLD, 20)); // Imposta il font del titolo (Arial, grassetto, 20 px)
        titolo.setHorizontalAlignment(JLabel.CENTER); // Centra il titolo nella finestra
        gbc.gridx = 0; // Imposta la colonna di GridBagLayout
        gbc.gridy = 0; // Imposta la riga di GridBagLayout
        gbc.insets = new Insets(20, 0, 20, 0); // Imposta il margine (spaziatura sopra e sotto)
        corpo.add(titolo, gbc); // Aggiungi il titolo al layout

        // Crea e configura il bottone "Cittadino"
        bottoneCittadino = new JButton("Cittadino");
        bottoneCittadino.setPreferredSize(new Dimension(200, 50)); // Imposta le dimensioni preferite del bottone
        bottoneCittadino.setFont(new Font("Arial", Font.PLAIN, 16)); // Imposta il font del testo nel bottone
        bottoneCittadino.setBackground(new Color(100, 200, 255)); // Imposta il colore di sfondo del bottone
        bottoneCittadino.setForeground(Color.WHITE); // Imposta il colore del testo nel bottone
        bottoneCittadino.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Imposta il bordo del bottone
        bottoneCittadino.setFocusPainted(false); // Rimuove l'effetto del bordo quando il bottone è selezionato
        bottoneCittadino.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Imposta il cursore della mano quando ci si passa sopra

        // Aggiungi un'icona al bottone "Cittadino" (da cambiare il path se necessario)
        ImageIcon cittadinoIcon = new ImageIcon("path_to_icon/cittadino_icon.png");
        bottoneCittadino.setIcon(cittadinoIcon); // Imposta l'icona nel bottone
        bottoneCittadino.setHorizontalTextPosition(SwingConstants.CENTER); // Posiziona il testo sotto l'icona

        // Aggiungi il bottone "Cittadino" al layout
        gbc.gridx = 0; // Imposta la colonna
        gbc.gridy = 1; // Imposta la riga
        corpo.add(bottoneCittadino, gbc); // Aggiungi il bottone al layout
        bottoneCittadino.addActionListener(new Gestore()); // Aggiungi l'ActionListener per gestire gli eventi

        // Crea e configura il bottone "Operatore"
        bottoneOperatore = new JButton("Operatore");
        bottoneOperatore.setPreferredSize(new Dimension(200, 50)); // Imposta le dimensioni preferite del bottone
        bottoneOperatore.setFont(new Font("Arial", Font.PLAIN, 16)); // Imposta il font del testo nel bottone
        bottoneOperatore.setBackground(new Color(100, 200, 255)); // Imposta il colore di sfondo del bottone
        bottoneOperatore.setForeground(Color.WHITE); // Imposta il colore del testo nel bottone
        bottoneOperatore.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Imposta il bordo del bottone
        bottoneOperatore.setFocusPainted(false); // Rimuove l'effetto del bordo quando il bottone è selezionato
        bottoneOperatore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Imposta il cursore della mano quando ci si passa sopra

        // Aggiungi un'icona al bottone "Operatore" (da cambiare il path se necessario)
        ImageIcon operatoreIcon = new ImageIcon("path_to_icon/operatore_icon.png");
        bottoneOperatore.setIcon(operatoreIcon); // Imposta l'icona nel bottone
        bottoneOperatore.setHorizontalTextPosition(SwingConstants.CENTER); // Posiziona il testo sotto l'icona

        // Aggiungi il bottone "Operatore" al layout
        gbc.gridx = 0; // Imposta la colonna
        gbc.gridy = 2; // Imposta la riga
        corpo.add(bottoneOperatore, gbc); // Aggiungi il bottone al layout
        bottoneOperatore.addActionListener(new Gestore()); // Aggiungi l'ActionListener per gestire gli eventi

        setVisible(true); // Rendi visibile la finestra principale
    }

    // Gestore degli eventi per i bottoni "Cittadino" e "Operatore"
    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Gestione dell'evento per il bottone "Cittadino"
            if (e.getSource() == bottoneCittadino) {
                // Funzionalità non ancora implementata, stampa un messaggio di errore
                LayoutCittadino cittadino = new LayoutCittadino();
                cittadino.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                cittadino.setLocationRelativeTo(null); // Centra la finestra sullo schermo
                cittadino.setVisible(true); // Rende visibile la finestra
                dispose();
            }
            // Gestione dell'evento per il bottone "Operatore"
            else if (e.getSource() == bottoneOperatore) {
                // Crea e mostra la finestra di login per l'operatore
                Login finestraOperatore = new Login();
                finestraOperatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                finestraOperatore.setLocationRelativeTo(null); // Centra la finestra sullo schermo
                finestraOperatore.setVisible(true); // Rende visibile la finestra
                dispose(); // Chiude la finestra principale "Home"
            }
        }
    }

    // Metodo main per eseguire l'applicazione e mostrare la finestra principale
    public static void main(String[] args) {
        new Home(); // Crea e mostra la finestra principale
    }
}
