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

    private StringBuilder res = new StringBuilder();
    private StringBuilder err = new StringBuilder();

    // Costruttore completo
    public Operatore(String nome, String cognome, String codFiscale, String email, String password, String centroMonitoraggio) {
        this.nome = nome;
        this.cognome = cognome;
        this.codFiscale = codFiscale.toUpperCase();
        this.email = email;
        this.password = password;
        this.centroMonitoraggio = centroMonitoraggio;
        this.username = generateUsername(nome, cognome, codFiscale);
    }

    // Costruttore con 4 parametri (nome, cognome, codFiscale, email)
    public Operatore(String nome, String cognome, String codFiscale, String email) {
        this(nome, cognome, codFiscale, email, "", "");  // Default password e centroMonitoraggio
    }

    public String getUsername() {
        return this.username;
    }

    // Metodo di validazione migliorato
    public boolean validate() {
        res.setLength(0);
        err.setLength(0);

        // Esegui tutte le validazioni sui campi
        validateField(nome, "Il nome non è stato inserito.", 30);
        validateField(cognome, "Il cognome non è stato inserito.", 30);
        validateCodFiscale(codFiscale); // Validazione per il codice fiscale
        validateEmail(email);
        validatePassword(password);
        validateField(centroMonitoraggio, "Il centro di monitoraggio non è stato inserito.", 50);

        return err.length() == 0;
    }

    private void validateField(String value, String errorMessage, int maxLength) {
        if (value.isEmpty()) {
            err.append(errorMessage).append("\n");
        } else if (value.length() > maxLength) {
            err.append("Il campo " + errorMessage.split(" ")[1] + " supera la lunghezza massima di " + maxLength + " caratteri.\n");
        } else {
            res.append(value.toUpperCase()).append("; ");
        }
    }

    private void validateCodFiscale(String codFiscale) {
        // Regex per il formato corretto del Codice Fiscale
        String regexCodFiscale = "^[A-Z0-9]{16}$";
        if (codFiscale.isEmpty()) {
            err.append("Il codice fiscale non è stato inserito.\n");
        } else if (codFiscale.length() != 16 || !codFiscale.matches(regexCodFiscale)) {
            err.append("Il codice fiscale deve avere esattamente 16 caratteri e deve essere composto da lettere e numeri.\n");
        } else {
            res.append(codFiscale.toUpperCase()).append("; ");
        }
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email.isEmpty()) {
            err.append("L'email non è stata inserita.\n");
        } else if (!Pattern.matches(emailRegex, email)) {
            err.append("L'email non è valida.\n");
        } else {
            res.append(email.toUpperCase()).append("; ");
        }
    }

    private void validatePassword(String password) {
        if (password.isEmpty()) {
            err.append("La password non può essere vuota.\n");
        } else if (password.length() < 8) {
            err.append("La password deve avere almeno 8 caratteri.\n");
        } else if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*[0-9].*") || !password.matches(".*[^A-Za-z0-9].*")) {
            err.append("La password deve contenere almeno una lettera maiuscola, una lettera minuscola, un numero e un simbolo.\n");
        }
    }

    // Metodo per generare lo username
    private String generateUsername(String nome, String cognome, String codFiscale) {
        // Prima lettera maiuscola di nome e cognome, resto in minuscolo
        String initials = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase() +
                cognome.substring(0, 1).toUpperCase() + cognome.substring(1).toLowerCase();

        // Ultimi due caratteri del codice fiscale
        String lastTwoDigits = codFiscale.substring(14, 16);

        // Combina le iniziali e i due ultimi caratteri del codice fiscale per generare lo username
        return initials + lastTwoDigits;
    }

    // Restituisce i messaggi di errore
    public String getErrorMessages() {
        return err.toString();
    }
}
