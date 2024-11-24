package LAB_B.Operatore;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class DatiClimatici extends JFrame {

    private JTable datiTable;
    private JButton refreshButton;
    private JButton backButton; // Pulsante per tornare indietro

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/climate_monitoring";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "0000";

    private String username;
    private String centro;
    private String area;

    // Costruttore che accetta username, centro, area
    public DatiClimatici(String username, String centro, String area) {
        this.username = username;
        this.centro = centro;
        this.area = area;

        setTitle("Dati Climatici");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principale
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Titolo
        JLabel titleLabel = new JLabel("Visualizza Dati Climatici", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        container.add(titleLabel, BorderLayout.NORTH);

        // Pannello centrale per la tabella
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BorderLayout());

        // Dati per la tabella (esempio)
        String[] columnNames = {"Data", "Temperatura", "Umidità", "Pressione"};
        Object[][] data = fetchDataFromDatabase();

        datiTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(datiTable);
        dataPanel.add(scrollPane, BorderLayout.CENTER);

        container.add(dataPanel, BorderLayout.CENTER);

        // Pannello per il pulsante di aggiornamento
        JPanel bottomPanel = new JPanel();
        refreshButton = new JButton("Aggiorna Dati");
        refreshButton.addActionListener(e -> updateData());
        bottomPanel.add(refreshButton);

        // Pulsante per tornare indietro al LayoutOperatore
        backButton = new JButton("Torna indietro");
        backButton.addActionListener(e -> {
            dispose(); // Chiudi la finestra attuale
            new LayoutOperatore(username); // Passa l'username al costruttore di LayoutOperatore
        });
        bottomPanel.add(backButton);

        container.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Metodo per recuperare i dati dal database
    private Object[][] fetchDataFromDatabase() {
        ArrayList<Object[]> dataList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT data, temperatura, umidita, pressione FROM dati_climatici WHERE area = '" + area + "'")) {

            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getString("data");
                row[1] = rs.getDouble("temperatura");
                row[2] = rs.getDouble("umidita");
                row[3] = rs.getDouble("pressione");
                dataList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataList.toArray(new Object[0][0]);
    }

    // Metodo per aggiornare i dati nella tabella
    private void updateData() {
        Object[][] updatedData = fetchDataFromDatabase();
        datiTable.setModel(new javax.swing.table.DefaultTableModel(updatedData, new String[]{"Data", "Temperatura", "Umidità", "Pressione"}));
    }

    public static void main(String[] args) {
        // Crea una nuova finestra di DatiClimatici con parametri esempio
        new DatiClimatici("user1", "Centro 1", "Area 1");  // Esempio di parametri
    }
}
