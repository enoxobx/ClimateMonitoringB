package LAB_B.Common.Cittadino;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import LAB_B.Common.Cittadino.Panels.*;
import LAB_B.Common.Interface.Coordinate;
import LAB_B.Common.Interface.TipiPlot;
import LAB_B.Database.Database;

public class LayoutPlot extends JFrame {
    private Database db;
    private Container body;
    private CardLayout cards;
    private JPanel center;

    public LayoutPlot(Coordinate tempCity, Database db) {
        super(tempCity.toString());
        this.db=db;

        body = getContentPane();

        BorderLayout border = new BorderLayout();

        body.setLayout(border);

        JPanel north = setNorth();
        body.add(north, BorderLayout.NORTH);

        center = setCenter(tempCity);
        body.add(center, BorderLayout.CENTER);

    }

    private JPanel setNorth() {

        JPanel north = new JPanel();

        List<String> comboBoxItems = new ArrayList<String>();
        TipiPlot[] tipi = TipiPlot.values();
        for (var t : tipi) {
            comboBoxItems.add(t.getName());
        }

        JComboBox<String> cb = new JComboBox<String>(comboBoxItems.toArray(new String[comboBoxItems.size()]));
        cb.setEditable(false);

        cb.addActionListener(e -> {
            cards.show(center, (String) cb.getSelectedItem());
        });
        north.add(cb);

        return north;
    }

    private JPanel setCenter(Coordinate city) {
        JPanel tempPanel = new JPanel();
        cards = new CardLayout();
        tempPanel.setLayout(cards);

        List<JPanel> plots = new ArrayList<JPanel>();
        TipiPlot[] tipi = TipiPlot.values();

        for (var temp : tipi) {
            JPanel t = new Plot1(temp, db,city);
            plots.add(t);
            tempPanel.add(temp.getName(), t);
        }

        return tempPanel;
    }

}
