package LAB_B.Common;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import java.util.*;
import javax.swing.*;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;

import java.rmi.RemoteException;



public class LayoutCittadino extends LayoutStandard {
    private JPanel centerP;
    private JMapViewer mapViewer;
    private JButton caricaCitta;
    private JButton submit;
    private JComboBox<String> comboBox;
    private List<Coordinate> coordinateM;
    private MapMarkerDot newDot;

    public LayoutCittadino() {

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

        mapViewer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ottieni le coordinate dove l'utente ha cliccato e si attiva solo con doppio
                // click sx
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    // setComboBox();

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

        caricaCitta.addActionListener(e -> {
            setComboBox();
            comboBox.showPopup();
        });

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (index == -1 && value == null) { // Nessun elemento selezionato
                    setText("Premi refresh per ottenere le città nella zona"); // Testo di default
                }
                return this;
            }
        });

        comboBox.addActionListener(e -> {
            String selectedItem = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
            if (selectedItem != null && comboBox.isEnabled()) {
                double mapX = mapViewer.getPosition().getLat();
                double mapY = mapViewer.getPosition().getLon();
                Double tolerance;
                int zoom = mapViewer.getZoom();
                if (zoom <= 5)
                    tolerance = 10.00;
                else if (zoom > 5 && zoom <= 11)
                    tolerance = 0.5;
                else if (zoom > 11 && zoom <= 15)
                    tolerance = 0.05;
                else
                    tolerance = 0.005;

                List<Coordinate> temp = coordinateM.stream()
                        .filter(x -> x.getName().equals(selectedItem) && x.getLat() >= (mapX - tolerance)
                                && x.getLat() <= (mapX + tolerance) && x.getLon() >= (mapY - tolerance)
                                && x.getLon() <= (mapY + tolerance))
                        .toList();
                if (temp.size() > 1) {
                    // qua non deve mai entrare
                    System.err.println("ci sono ambiguità");
                } else if (temp.size() == 1) {
                    mapViewer.setDisplayPosition(temp.getFirst(), mapViewer.getZoom());
                    newDot = new MapMarkerDot(new Layer(temp.getFirst().getName()),temp.getFirst().getLat(),temp.getFirst().getLon());
                    mapViewer.addMapMarker(newDot);
                } else {
                    JOptionPane.showMessageDialog(body,
                            "Si è verificato un errore, riprova a selezionare una città e fare Refresh");
                }
            }

        });
        

        submit.addActionListener(e -> {
            setComboBox();
        });

        this.setSize(800, 600);

    }

    // prendere da ESEMPIO
    private void setMarker() {

        try {
            // DB è UN RIFERIMENTO CHE HA LA CLASSE layoutStandard, dalla quale devono
            // estendere tutte le classi della GUI
            coordinateM = db.getCoordinaResultSet();
            if (coordinateM == null) {
                System.err.print(" vuoto");
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void setComboBox() {
        comboBox.setSelectedIndex(-1);
        comboBox.removeAllItems();
        Double lat = mapViewer.getPosition().getLat();
        Double lon = mapViewer.getPosition().getLon();
        int round = 5;
        Double tolerance;
        int zoom = mapViewer.getZoom();
        if (zoom <= 5)
            tolerance = 10.00;
        else if (zoom > 5 && zoom <= 11)
            tolerance = 0.5;
        else if (zoom > 11 && zoom <= 15)
            tolerance = 0.05;
        else
            tolerance = 0.005;

        List<Coordinate> ciao;
        try {
            ciao = db.getCoordinaResultSet(lat, lon, tolerance);
            comboBox.setEnabled(false);
            for (Coordinate coordinate : ciao) {

                comboBox.addItem(coordinate.getName());

            }
            comboBox.setSelectedIndex(-1);
            comboBox.setEnabled(true);
            if(newDot!=null)newDot.setVisible(false);

        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
    

}
