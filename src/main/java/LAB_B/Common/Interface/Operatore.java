package LAB_B.Common.Interface;

import LAB_B.Database.QueryExecutorImpl;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class Operatore {

    private final String nome;
    private final String cognome;
    private final String codFiscale;
    private final String email;
    private final String password;
    private final String confermaPassword;  // Aggiunto campo per conferma password
    private final String centroMonitoraggio;
    private String username;
    private final StringBuilder err = new StringBuilder();

    // Costruttore completo con tutti i parametri
    public Operatore(String nome, String cognome, String codFiscale, String email, String password, String confermaPassword, String centroMonitoraggio, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.codFiscale = codFiscale != null ? codFiscale.toUpperCase() : ""; // Assicurati che il codice fiscale sia in maiuscolo
        this.email = email;
        this.password = password;
        this.confermaPassword = confermaPassword;
        this.centroMonitoraggio = centroMonitoraggio;
        this.username = username; // Username può essere null o vuoto
    }

    // Getter per ottenere i vari campi
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCodFiscale() {
        return codFiscale;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCentroMonitoraggio() {
        return centroMonitoraggio;
    }

    public String getUsername() {
        return username;
    }

    // Metodo per validare i dati dell'operatore
    public boolean validate() {
        // Reset degli errori
        err.setLength(0);

        // Nome e cognome (max 30 caratteri)
        if (nome == null || nome.isEmpty() || nome.length() > 30) {
            err.append("Nome non valido. Deve essere non vuoto e massimo 30 caratteri.\n");
        }

        if (cognome == null || cognome.isEmpty() || cognome.length() > 30) {
            err.append("Cognome non valido. Deve essere non vuoto e massimo 30 caratteri.\n");
        }

        // Codice fiscale (deve essere 16 caratteri)
        if (codFiscale == null || codFiscale.length() != 16 || !Pattern.matches("[A-Z0-9]{16}", codFiscale)) {
            err.append("Codice Fiscale non valido. Deve essere di 16 caratteri alfanumerici.\n");
        }

        // Email (deve essere una email valida)
        if (!isValidEmail(email)) {
            err.append("Email non valida. Formato non corretto.\n");
        }

        // Password (minimo 8 caratteri con almeno un numero, una lettera maiuscola, una minuscola e un simbolo)
        if (password == null || password.length() < 8 || !isValidPassword(password)) {
            err.append("Password non valida. Deve essere lunga almeno 8 caratteri e contenere almeno un numero, una lettera maiuscola, una minuscola e un simbolo.\n");
        }

        // Verifica che le password coincidano
        if (!password.equals(confermaPassword)) {
            err.append("Le password non coincidono.\n");
        }

        // Centro monitoraggio (non deve essere vuoto)
        if (centroMonitoraggio == null || centroMonitoraggio.isEmpty()) {
            err.append("Centro Monitoraggio non valido. Non può essere vuoto.\n");
        }

        // Se non ci sono errori, generiamo lo username, se necessario
        if (err.length() == 0 && (username == null || username.isEmpty())) {
            // Crea una nuova istanza di QueryExecutorImpl per generare lo username
            QueryExecutorImpl queryExecutor = new QueryExecutorImpl();
            try {
                this.username = queryExecutor.generateUsername(nome, cognome, codFiscale); // Genera un username tramite QueryExecutorImpl
            } catch (SQLException e) {
                err.append("Errore nella generazione dello username: " + e.getMessage() + "\n");
            }
        }

        return err.length() == 0; // Restituisce true se non ci sono errori
    }

    // Metodo per ottenere i messaggi di errore
    public String getErrorMessages() {
        return err.toString();
    }

    // Verifica se la email è valida
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;  // Email vuota
        }
        // Regex per validare email con almeno una lettera e simbolo '@' e '.'
        return email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,6}$");
    }

    // Metodo per validare la password con i requisiti specifici
    private boolean isValidPassword(String password) {
        // Pattern per verificare la presenza di caratteri speciali
        Pattern specialCharPattern = Pattern.compile("[!@#\\$%^&*()_+\\-=\\[\\]{};':\",\\\\|,.<>\\/?]");

        // Verifica che la password contenga almeno una lettera maiuscola, una minuscola, un numero e un simbolo
        return password.matches(".*[A-Z].*") && // almeno una lettera maiuscola
                password.matches(".*[a-z].*") && // almeno una lettera minuscola
                password.matches(".*[0-9].*") && // almeno un numero
                specialCharPattern.matcher(password).find(); // almeno un simbolo
    }
}
