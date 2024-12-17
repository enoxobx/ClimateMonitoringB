package LAB_B.Common;


import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import java.util.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.stream.Collectors;

import static java.lang.Math.round;

public class LayoutCittadino extends LayoutStandard {
    private JMapViewer mapViewer;
    private  JButton submit;
    private JComboBox<String> comboBox;

    public LayoutCittadino() {

        Container body = this.getBody();

        mapViewer = new JMapViewer();
        comboBox = new JComboBox<>();
        submit = new JButton("Cerca");


        Coordinate francia = new Coordinate(48.8588443, 2.2943506);
        mapViewer.setDisplayPosition(francia, 12);



        body.add(mapViewer,BorderLayout.NORTH);
        body.add(submit,BorderLayout.SOUTH);
        body.add(comboBox,BorderLayout.CENTER);

        setComboBox();
        setMarker();

        mapViewer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ottieni le coordinate dove l'utente ha cliccato e si attiva solo con doppio
                // click sx
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    double lat = mapViewer.getPosition().getLat() ;
                    double lon = mapViewer.getPosition().getLon();

                    var ciao = mapViewer.getMapMarkerList().stream()
                            .filter( x -> round(x.getLat()) == round(lat) &&  round(x.getLon()) == round(lon))
                            .toList();
                    List<String> listone = new ArrayList<>();
                    for(var dot : ciao){
                        listone.add(dot.getLayer().getName()+"\n");
                    }
                    // Mostra le coordinate in una finestra di dialogo
                    JOptionPane.showMessageDialog(body, "Cliccato su: \nLat: " + lat + "\nLon: " + lon +"\n"+listone.toString());

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

        submit.addActionListener(e ->{
            double lat = mapViewer.getPosition().getLat() ;
            double lon = mapViewer.getPosition().getLon();

            var ciao = mapViewer.getMapMarkerList().stream()
                    .filter( x -> round(x.getLat()) == round(lat) &&  round(x.getLon()) == round(lon))
                    .toList();
            List<String> listone = new ArrayList<>();
            for(var dot : ciao){
                listone.add(dot.getLayer().getName()+"\n");
            }
            System.out.println(listone);
        });




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
                    Layer layer = new Layer(coordinate.getName());
                    MapMarkerDot marker = new MapMarkerDot(layer, coordinate.getLat(), coordinate.getLon());
                    marker.setVisible(false);
                    mapViewer.addMapMarker( marker);
                }
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void setComboBox(){
        List<Coordinate> coordinateM;
        try {
            //DB è UN RIFERIMENTO CHE HA LA CLASSE layoutStandard, dalla quale devono estendere tutte le classi della GUI
            coordinateM = db.getCoordinaResultSet();
            if (coordinateM == null){
                System.err.print(" vuoto");
            }else{
                for (Coordinate coordinate : coordinateM) {
                    comboBox.addItem(coordinate.getName());

                }
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
