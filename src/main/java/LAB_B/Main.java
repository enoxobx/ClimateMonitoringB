package LAB_B;

import LAB_B.Database.Server;
import LAB_B.Client.Client;

public class Main {
    public static void main(String[] args) {
        // Avvia prima il server
        Server server = new Server();
        server.start();  // Avvia il server

        // Poi avvia il client
        Client client = new Client();
        client.start();  // Avvia il client
    }
}
