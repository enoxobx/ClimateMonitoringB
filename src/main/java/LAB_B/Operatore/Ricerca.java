package LAB_B.Operatore;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Ricerca extends JFrame {

    private JTable table;
    private JButton backButton; // Pulsante per tornare indietro
    private String path3; // path CentriMonitoraggio
    private ArrayList<String[]> centriList;

    private String codiceFiscale; // Variabile per il codice fiscale dell'utente
    private String userId; // ID dell'utente generato dopo il login

    public Ricerca() {
        String osName = System.getProperty("os.name").toLowerCase();
        String t = System.getProperty("user.dir");

        // Gestione del path in base al sistema operativo
        if (osName.contains("win")) {
            path3 = t + "\\src\\main\\java\\ClimateMonitoring\\data\\CentriMonitoraggio.dati.csv";
        } else if (osName.contains("mac")) {
            path3 = t + "/src/main/java/ClimateMonitoring/data/CentriMonitoraggio.dati.csv";
        } else {
            System.out.println("Sistema operativo non riconosciuto");
            path3 = ""; // Puoi gestire altre piattaforme se necessario
        }

        setTitle("Ricerca Centri Monitoraggio");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principale
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Titolo
        JLabel titleLabel = new JLabel("Centri di Monitoraggio", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        container.add(titleLabel, BorderLayout.NORTH);

        // Carica i centri dal file CSV
        centriList = loadCentriFromCSV(path3);

        // Tabella per visualizzare i centri
        String[] columnNames = {"ID", "Nome Centro", "Indirizzo", "Aree di Interesse"};
        Object[][] data = convertListToTableData(centriList);
        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane, BorderLayout.CENTER);

        // Pannello per il pulsante di ricerca
        JPanel bottomPanel = new JPanel();
        JButton searchButton = new JButton("Aggiorna");
        searchButton.addActionListener(e -> updateTable());
        bottomPanel.add(searchButton);

        // Pulsante per tornare indietro
        backButton = new JButton("Torna indietro");
        backButton.addActionListener(e -> {
            dispose(); // Chiudi la finestra attuale

        });
        bottomPanel.add(backButton);

        container.add(bottomPanel, BorderLayout.SOUTH);

        // Simula il login e ottieni l'ID utente
        String inputCodiceFiscale = JOptionPane.showInputDialog(this, "Inserisci Codice Fiscale o Username:");
        String inputPassword = JOptionPane.showInputDialog(this, "Inserisci la Password:");

        if (loginSuccess(inputCodiceFiscale, inputPassword)) {
            userId = generateUserId(inputCodiceFiscale); // Genera l'ID dell'utente dopo login
            codiceFiscale = inputCodiceFiscale;
            JOptionPane.showMessageDialog(this, "Login riuscito! ID utente: " + userId);
        } else {
            JOptionPane.showMessageDialog(this, "Login fallito! Verifica codice fiscale, username e password.");
            return; // Non proseguire se il login non è riuscito
        }

        setVisible(true);
    }

    // Metodo per caricare i centri di monitoraggio dal CSV
    private ArrayList<String[]> loadCentriFromCSV(String path) {
        ArrayList<String[]> centri = new ArrayList<>();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                centri.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return centri;
    }

    // Metodo per convertire la lista di centri in un formato compatibile con JTable
    private Object[][] convertListToTableData(ArrayList<String[]> centriList) {
        Object[][] data = new Object[centriList.size()][4];
        for (int i = 0; i < centriList.size(); i++) {
            String[] centro = centriList.get(i);
            data[i][0] = centro[0];  // ID
            data[i][1] = centro[1];  // Nome Centro
            data[i][2] = centro[2];  // Indirizzo
            data[i][3] = centro[3];  // Aree di Interesse
        }
        return data;
    }

    // Metodo per aggiornare la tabella
    private void updateTable() {
        // Ricarica i dati dal CSV
        centriList = loadCentriFromCSV(path3);
        Object[][] updatedData = convertListToTableData(centriList);
        table.setModel(new javax.swing.table.DefaultTableModel(updatedData, new String[]{"ID", "Nome Centro", "Indirizzo", "Aree di Interesse"}));
    }

    // Metodo per il login dell'operatore
    private boolean loginSuccess(String codiceFiscaleOrUsername, String password) {
        File file = new File("src/main/java/ClimateMonitoring/data/OperatoriRegistrati.dati.csv"); // Percorso al file degli operatori
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                // Verifica se il codice fiscale o username corrispondono e se la password è corretta
                if ((data[6].equals(codiceFiscaleOrUsername) || data[4].equals(codiceFiscaleOrUsername)) && data[3].equals(password)) {
                    return true; // Login riuscito
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Login fallito
    }

    // Metodo per generare un ID utente (esempio basato sul codice fiscale)
    private String generateUserId(String codiceFiscale) {
        return codiceFiscale.substring(0, 5) + System.currentTimeMillis(); // Genera un ID unico
    }

    public static void main(String[] args) {
        new Ricerca();  // Creazione della finestra di ricerca
    }
}
