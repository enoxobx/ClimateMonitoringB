package LAB_B.Common;

import java.util.regex.Pattern;

public class Operatore {

    private String nome;
    private String cognome;
    private String codFiscale;
    private String email;
    private String password;
    private String centroMonitoraggio;
    private String username;
    private StringBuilder err = new StringBuilder();

    // Costruttore completo con tutti i parametri
    public Operatore(String nome, String cognome, String codFiscale, String email, String password, String centroMonitoraggio, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.codFiscale = codFiscale != null ? codFiscale.toUpperCase() : ""; // Assicurati che il codice fiscale sia in maiuscolo
        this.email = email;
        this.password = password;
        this.centroMonitoraggio = centroMonitoraggio;
        this.username = (username != null && !username.isEmpty()) ? username : generateUsername(nome, cognome, codFiscale);
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

        // Centro monitoraggio (non deve essere vuoto)
        if (centroMonitoraggio == null || centroMonitoraggio.isEmpty()) {
            err.append("Centro Monitoraggio non valido. Non può essere vuoto.\n");
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
    public boolean isValidPassword(String password) {
        return password.matches(".*[A-Z].*") && // almeno una lettera maiuscola
                password.matches(".*[a-z].*") && // almeno una lettera minuscola
                password.matches(".*[0-9].*") && // almeno un numero
                password.matches(".*[!@#\\$%^&*].*"); // almeno un simbolo
    }


    // Metodo per generare il nome utente
    private String generateUsername(String nome, String cognome, String codFiscale) {
        // Assicurati che nome e cognome abbiano almeno 3 caratteri
        String nomeParte = nome.length() >= 3 ? nome.substring(0, 3) : nome;
        String cognomeParte = cognome.length() >= 3 ? cognome.substring(0, 3) : cognome;
        return nomeParte + cognomeParte + codFiscale.substring(0, 4); // Esempio di generazione
    }
}