package LAB_B.Common;

import LAB_B.Client.Client;
import LAB_B.Database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.RemoteException;

// Classe astratta che fornisce una finestra di layout standard con un bottone "Home".
// Nota: la classe è dichiarata "abstract" per non essere utilizzata direttamente,
// ma estesa da altre classi.
abstract public class LayoutStandard extends JFrame {

    // Dichiarazione dei componenti dell'interfaccia
    protected final JButton home; // Bottone "Home"
    private final Container body; // Corpo della finestra (contenitore principale)
    private final Gestore gestore; // Gestore degli eventi (ActionListener)
    public Database db;

    // Costruttore della classe LayoutStandard
    public LayoutStandard() {
        super("Climate Monitoring"); // Titolo della finestra
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Handle exception if Nimbus is not available
            e.printStackTrace();
        }

        // Ottiene il contenitore della finestra (il pannello principale)
        body = getContentPane();
        body.setLayout(new BorderLayout()); // Imposta il layout come BorderLayout

        // Crea un'istanza del gestore che si occuperà di gestire gli eventi
        gestore = new Gestore();

        // Crea il bottone "Home"
        home = new JButton("Home");
        // Aggiunge il bottone alla finestra nella posizione a sinistra
        // (BorderLayout.WEST)
        body.add(home, BorderLayout.WEST);
        // Associa l'azione del bottone al gestore (Gestore implementa ActionListener)
        home.addActionListener(gestore);

        // istanzia il db da usare;
        try {
            db = Client.getDb();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Imposta le proprietà predefinite della finestra
        setDefaultProperties();
    }

    // Metodo che imposta le proprietà di base della finestra
    // In particolare, imposta le dimensioni fisse, la chiusura dell'applicazione
    // al termine della finestra e la posizione centrata.
    protected void setDefaultProperties() {
        setSize(800, 800); // Imposta la dimensione della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Chiude l'applicazione quando la finestra viene chiusa
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setResizable(false); // Disabilita la possibilità di ridimensionare la finestra
    }
    protected void setDefaultProperties(JFrame temp) {
        temp.setSize(800, 800); // Imposta la dimensione della finestra
        temp.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Chiude l'applicazione quando la finestra viene chiusa
        temp.setLocationRelativeTo(null); // Centra la finestra sullo schermo
        temp.setResizable(true); // Disabilita la possibilità di ridimensionare la finestra
        temp.setVisible(true);
    }

    // Gestore degli eventi per il bottone "Home"
    public class Gestore implements ActionListener, Serializable {

        // Metodo che gestisce l'evento quando il bottone viene cliccato
        @Override
        public void actionPerformed(ActionEvent e) {
            // Controlla se l'evento è stato generato dal bottone "Home"
            if (e.getSource() == home) {
                // Crea una nuova finestra "Home"
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
