package LAB_B.Common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// classe da non utlizzare
abstract public class LayoutStandard extends JFrame {

    private JButton home;
    private Container body;
    private Gestore gestore;

    public LayoutStandard() {
        super("Climate Monitoring");

        body = getContentPane();
        body.setLayout(new BorderLayout());
        gestore = new Gestore();

        home = new JButton("Home");
        body.add(home, BorderLayout.WEST);
        home.addActionListener(gestore);

        setDefaultProperties();
    }

    // Metodo comune per uniformare finestre
    protected void setDefaultProperties() {
        setSize(600, 400); // Imposta dimensione fissa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra sullo schermo
        setResizable(false); // Facoltativo: blocca il ridimensionamento
    }

    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == home) {
                Home t = new Home();
                t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                t.setLocationRelativeTo(null);
                t.setVisible(true);
                dispose();
            }
        }
    }

    public Container getBody() {
        return body;
    }

    public Gestore getGestore() {
        return gestore;
    }
}