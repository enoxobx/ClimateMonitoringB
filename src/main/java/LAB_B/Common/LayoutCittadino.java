package LAB_B.Common;

import LAB_B.Client.*;
import LAB_B.Database.*;

import org.openstreetmap.*;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import java.util.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.sql.ClientInfoStatus;

public class LayoutCittadino extends LayoutStandard {
    private JMapViewer mapViewer;

    public LayoutCittadino() {
        mapViewer = new JMapViewer();
        Coordinate francia = new Coordinate(48.8588443, 2.2943506);
        mapViewer.setDisplayPosition(francia, 12);
        Container body = this.getBody();

        setMarker();

        mapViewer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ottieni le coordinate dove l'utente ha cliccato e si attiva solo con doppio
                // click sx
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    double lat = mapViewer.getPosition().getLat();
                    double lon = mapViewer.getPosition().getLon();

                    // Mostra le coordinate in una finestra di dialogo
                    JOptionPane.showMessageDialog(body, "Cliccato su: \nLat: " + lat + "\nLon: " + lon);
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

        this.getBody().add(mapViewer, BorderLayout.CENTER);
        this.setSize(800, 600);

    }


    //prendere da   ESEMPIO
    private void setMarker() {
        List<Coordinate> coordinateM;
        try {
            //DB è UN RIFERIMENTO CHE HA LA CLASSE layoutStandard, dalla quale devono estendere tutte le classi della GUI
            coordinateM = db.getCoordinaResultSet();
            if (coordinateM == null){
                System.err.print(" vuoto");
            }else{
                for (Coordinate coordinate : coordinateM) {

                    mapViewer.addMapMarker(new MapMarkerDot(coordinate.getLat(), coordinate.getLon()));
                }
            }
                

            
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
