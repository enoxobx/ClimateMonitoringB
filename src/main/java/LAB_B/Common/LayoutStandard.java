package LAB_B.Common;
import LAB_B.Client.Client;
import LAB_B.Database.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Classe astratta che fornisce una finestra di layout standard con un bottone "Home".
// Nota: la classe è dichiarata "abstract" per non essere utilizzata direttamente,
// ma probabilmente estesa da altre classi.
abstract public class LayoutStandard extends JFrame {

    // Dichiarazione dei componenti dell'interfaccia
    private JButton home;    // Bottone "Home"
    private Container body;  // Corpo della finestra (contenitore principale)
    private Gestore gestore; // Gestore degli eventi (ActionListener)
    public Database db;

    // Costruttore della classe LayoutStandard
    public LayoutStandard() {
        super("Climate Monitoring");  // Titolo della finestra

        // Ottiene il contenitore della finestra (il pannello principale)
        body = getContentPane();
        body.setLayout(new BorderLayout());  // Imposta il layout come BorderLayout

        // Crea un'istanza del gestore che si occuperà di gestire gli eventi
        gestore = new Gestore();

        // Crea il bottone "Home"
        home = new JButton("Home");
        // Aggiunge il bottone alla finestra nella posizione a sinistra (BorderLayout.WEST)
        body.add(home, BorderLayout.WEST);
        // Associa l'azione del bottone al gestore (Gestore implementa ActionListener)
        home.addActionListener(gestore);

        //istanzia il db da usare;
        db = Client.getDb();
        

        // Imposta le proprietà predefinite della finestra
        setDefaultProperties();
    }

    // Metodo che imposta le proprietà di base della finestra
    // In particolare, imposta le dimensioni fisse, la chiusura dell'applicazione
    // al termine della finestra e la posizione centrata.
    protected void setDefaultProperties() {
        setSize(600, 400); // Imposta la dimensione della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Chiude l'applicazione quando la finestra viene chiusa
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setResizable(false); // Disabilita la possibilità di ridimensionare la finestra
    }

    // Gestore degli eventi per il bottone "Home"
    private class Gestore implements ActionListener {

        // Metodo che gestisce l'evento quando il bottone viene cliccato
        @Override
        public void actionPerformed(ActionEvent e) {
            // Controlla se l'evento è stato generato dal bottone "Home"
            if (e.getSource() == home) {
                // Crea una nuova finestra "Home" (classe che probabilmente rappresenta una schermata principale)
                Home t = new Home();
                // Imposta la chiusura dell'applicazione quando questa finestra viene chiusa
                t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // Centra la finestra "Home" sullo schermo
                t.setLocationRelativeTo(null);
                // Rende visibile la finestra
                t.setVisible(true);
                // Chiude la finestra corrente (quella che contiene il bottone "Home")
                dispose();
            }
        }
    }

    // Metodo getter per ottenere il corpo della finestra (contenitore principale)
    public Container getBody() {
        return body;
    }

    // Metodo getter per ottenere il gestore degli eventi
    public Gestore getGestore() {
        return gestore;
    }
    
}
