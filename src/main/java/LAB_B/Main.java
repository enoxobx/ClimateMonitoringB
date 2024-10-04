package LAB_B;

import LAB_B.Client.Login;
import LAB_B.Common.Home;
import LAB_B.Database.Server;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        //Login a = new Login();
        new Server().start();
        Home home = new Home();
        home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        home.setLocationRelativeTo(null);
        home.setVisible(true);
        /* ;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client().start();
        */


    }

}