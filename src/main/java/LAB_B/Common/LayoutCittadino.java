package LAB_B.Common;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import LAB_B.Common.Interface.Citta;
import LAB_B.Common.Interface.Coordinate;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;

public class LayoutCittadino extends LayoutStandard {
    private final JPanel centerP;
    private final JMapViewer mapViewer;
    private final JButton caricaCitta;
    private final JButton search;
    private final JButton posiziona;
    private final JComboBox<Coordinate> comboBox;
    private List<Coordinate> coordinateM;
    private MapMarkerDot newDot;

    public LayoutCittadino() {
        // Container e componenti della GUI
        Container body = this.getBody();

        centerP = new JPanel();
        centerP.setLayout(new BoxLayout(centerP, BoxLayout.Y_AXIS));
        mapViewer = new JMapViewer();
        comboBox = new JComboBox<Coordinate>();
        comboBox.setEditable(true);
        caricaCitta = new JButton("Carica città vicine");
        posiziona = new JButton("Mostra nella mappa");
        search = new JButton("Cerca");
        Citta varese = new Citta("Varese", "Varese", "Varese", "it", "italia", 8.825058, 45.820599);
        Coordinate Vare = new Coordinate(varese);
        mapViewer.setDisplayPosition(Vare, 12);

        body.add(mapViewer, BorderLayout.NORTH);
        body.add(centerP, BorderLayout.CENTER);
        centerP.add(comboBox, BorderLayout.CENTER);
        centerP.add(caricaCitta, BorderLayout.CENTER);
        centerP.add(posiziona, BorderLayout.CENTER);
        centerP.add(search, BorderLayout.SOUTH);
        setMarker();

        // Listener per il pulsante "Refresh"
        caricaCitta.addActionListener(e -> {
            setComboBox();
        });
        posiziona.addActionListener(e ->{
            var pos = (Coordinate)comboBox.getSelectedItem();
            setPosition(pos);
            assert pos != null;
            newDot = new MapMarkerDot(new Layer(pos.getCitta().getName()), pos.getLat(),pos.getLon());
            mapViewer.addMapMarker(newDot);
            newDot.setVisible(true);
        });
        
        search.addActionListener(e -> {
            JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
            try {
                if(!editor.getText().isEmpty())setComboBox(editor.getText());
                else{
                    JOptionPane.showMessageDialog(body,"Inserisci dei dati");
                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        this.setSize(800, 600);
    }

    // Metodo per impostare i marker sulla mappa
    private void setMarker() {
        if (db != null) {
            try {
                coordinateM = db.getCoordinaResultSet();
                if (coordinateM == null) {
                    System.err.print("vuoto");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Errore: db non è stato inizializzato.");
        }
    }

    // Metodo per impostare la JComboBox con le città
    private void setComboBox() {
        comboBox.setEnabled(false);
        comboBox.setSelectedIndex(-1);
        comboBox.removeAllItems();
        double lat = mapViewer.getPosition().getLat();
        double lon = mapViewer.getPosition().getLon();
        int zoom = mapViewer.getZoom();
        double tolerance;
        if (zoom <= 5)
            tolerance = 10.00;
        else if (zoom <= 11)
            tolerance = 0.5;
        else if (zoom <= 15)
            tolerance = 0.05;
        else
            tolerance = 0.005;

        if (db != null) {
            try {
                List<Coordinate> ciao = db.getCoordinaResultSet(lat, lon, tolerance);

                for (Coordinate coordinate : ciao) {
                    comboBox.addItem(coordinate);
                }
                if (newDot != null)
                    newDot.setVisible(false);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            comboBox.setSelectedIndex(-1);
            comboBox.setEnabled(true);
            comboBox.showPopup();
        } else {
            System.err.println("Errore: db non è stato inizializzato.");
        }
    }

    // Metodo per impostare la JComboBox con le città
    private void setComboBox(String search) throws Exception {
        comboBox.setEnabled(false);
        comboBox.setSelectedIndex(-1);
        comboBox.removeAllItems();
        if (db != null) {
            try {
                List<Coordinate> ciao = db.getCoordinaResultSet(search);

                if (ciao == null)
                    throw new Exception("Errore nel caricamento");
                for (Coordinate coordinate : ciao) {
                    comboBox.addItem(coordinate);
                }

                if (newDot != null)
                    newDot.setVisible(false);
            } catch (RemoteException e1) {
                e1.printStackTrace();
                throw new Exception("Errore nel caricamento");
            }

            comboBox.setSelectedIndex(-1);
            comboBox.setEnabled(true);
            comboBox.showPopup();
        } else {
            System.err.println("Errore: db non è stato inizializzato.");
        }
    }

    private void setPosition(Coordinate pos){
        mapViewer.setDisplayPosition(pos, 10);
    }
}
