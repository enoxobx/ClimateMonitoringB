package LAB_B.Common;

import LAB_B.Client.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Home extends JFrame {
    private JButton bottoneCittadino;
    private JButton bottoneOperatore;

    public Home() {
        super("Monitoraggio Clima");
        Container corpo = getContentPane();
        corpo.setLayout(new FlowLayout());

        bottoneCittadino = new JButton("Cittadino");
        corpo.add(bottoneCittadino);
        bottoneCittadino.addActionListener(new Gestore());

        bottoneOperatore = new JButton("Operatore");
        corpo.add(bottoneOperatore);
        bottoneOperatore.addActionListener(new Gestore());

        setSize(400, 400);
        setVisible(true);
    }

    private class Gestore implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bottoneCittadino) {
                //gestisciPulsanteCittadino();
                System.err.println("ancora da implementare");
            } else if (e.getSource() == bottoneOperatore) {
                gestisciPulsanteOperatore();
            }
        }

        /*private void gestisciPulsanteCittadino() {
            new Thread(() -> {
                try (Socket socket = new Socket("localhost", 12345);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    out.println("REGISTER:cittadino");
                    out.println("GET_CLIMATE_DATA");

                    String risposta = in.readLine();

                    SwingUtilities.invokeLater(() -> {
                        LayoutCittadino layout = new LayoutCittadino(risposta);
                        layout.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        layout.setLocationRelativeTo(null);
                        layout.setVisible(true);
                        dispose();
                    });

                } catch (IOException ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Errore di connessione al server!"));
                }
            }).start();
        }*/

        private void gestisciPulsanteOperatore() {
            Login finestraOperatore = new Login();
            finestraOperatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            finestraOperatore.setLocationRelativeTo(null);
            finestraOperatore.setVisible(true);
            dispose();
        }
    }


}
