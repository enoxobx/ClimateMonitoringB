package LAB_B.Common;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;

public class LayoutCittadino extends LayoutStandard {
    private final JPanel centerP;
    private final JMapViewer mapViewer;
    private final JButton caricaCitta;
    private final JButton submit;
    private final JComboBox<String> comboBox;
    private List<Coordinate> coordinateM;
    private MapMarkerDot newDot;

    public LayoutCittadino() {
        // Container e componenti della GUI
        Container body = this.getBody();

        centerP = new JPanel();
        centerP.setLayout(new BoxLayout(centerP, BoxLayout.Y_AXIS));
        mapViewer = new JMapViewer();
        comboBox = new JComboBox<>();
        comboBox.setEditable(true);
        caricaCitta = new JButton("Refresh");
        submit = new JButton("Cerca");

        Coordinate parigi = new Coordinate(48.8588443, 2.2943506);
        mapViewer.setDisplayPosition(parigi, 11);

        body.add(mapViewer, BorderLayout.NORTH);
        body.add(centerP, BorderLayout.CENTER);
        centerP.add(comboBox, BorderLayout.CENTER);
        centerP.add(caricaCitta, BorderLayout.CENTER);
        body.add(submit, BorderLayout.SOUTH);

        setComboBox();
        setMarker();

        // Listener per il clic sulla mappa
        mapViewer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    // Gestione del doppio clic
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        // Listener per il pulsante "Refresh"
        caricaCitta.addActionListener(e -> {
            setComboBox();
            comboBox.showPopup();
        });

        // Renderer per la JComboBox
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (index == -1 && value == null) {
                    setText("Premi refresh per ottenere le città nella zona");
                }
                return this;
            }
        });

        // Listener per la selezione nella JComboBox
        comboBox.addActionListener(e -> {
            if (e.getSource().getClass() == JComboBox.class) {
                String selectedItem = (String) ((JComboBox<?>) e.getSource()).getSelectedItem();
                if (selectedItem != null && comboBox.isEnabled()) {
                    double mapX = mapViewer.getPosition().getLat();
                    double mapY = mapViewer.getPosition().getLon();
                    Double tolerance;
                    int zoom = mapViewer.getZoom();
                    if (zoom <= 5)
                        tolerance = 10.00;
                    else if (zoom <= 11)
                        tolerance = 0.5;
                    else if (zoom <= 15)
                        tolerance = 0.05;
                    else
                        tolerance = 0.005;

                    List<Coordinate> temp = coordinateM.stream()
                            .filter(x -> x.getName().equals(selectedItem) && x.getLat() >= (mapX - tolerance)
                                    && x.getLat() <= (mapX + tolerance) && x.getLon() >= (mapY - tolerance)
                                    && x.getLon() <= (mapY + tolerance))
                            .toList();
                    if (temp.size() > 1) {
                        System.err.println("ci sono ambiguità");
                    } else if (temp.size() == 1) {
                        mapViewer.setDisplayPosition(temp.getFirst(), mapViewer.getZoom());
                        newDot = new MapMarkerDot(new Layer(temp.getFirst().getName()), temp.getFirst().getLat(), temp.getFirst().getLon());
                        mapViewer.addMapMarker(newDot);
                    } else {
                        JOptionPane.showMessageDialog(body,
                                "Si è verificato un errore, riprova a selezionare una città e fare Refresh");
                    }
                } else {
                    //non dovrebbe mai entrare qua
                    System.err.println("Non è stato attivato dalla classe corretta");
                }
            }

        });

        submit.addActionListener(e -> {
            setComboBox();
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
                comboBox.setEnabled(false);
                for (Coordinate coordinate : ciao) {
                    comboBox.addItem(coordinate.getName());
                }
                comboBox.setSelectedIndex(-1);
                comboBox.setEnabled(true);
                if (newDot != null) newDot.setVisible(false);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        } else {
            System.err.println("Errore: db non è stato inizializzato.");
        }
    }
}
