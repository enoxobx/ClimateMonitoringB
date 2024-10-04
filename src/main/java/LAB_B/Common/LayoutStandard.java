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


    public LayoutStandard(){
        super("Climate Monitoring");

        body = getContentPane(); //crea la finestra
        body.setLayout(new BorderLayout()); // imposta il layout della finestra
        gestore = new Gestore(); //creazione di un gestore di eventi

        home = new JButton("Home");
        body.add(home,BorderLayout.WEST);
        home.addActionListener(gestore);

        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    //gestore di eventi
    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==home){
                Home t = new Home();
                t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                t.setLocationRelativeTo(null);
                t.setVisible(true);
                dispose(); // comando per rilasciare le risorse e chiudere la finestra
            }
        }
    }




    //getter e setter
    public Container getBody() {
        return body;
    }

    public Gestore getGestore() {
        return gestore;
    }



}