package LAB_B.Common;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Operatore implements Serializable {
    // Versione seriale per la serializzazione della classe
    private static final long serialVersionUID = 1L;

    // Dichiarazione dei campi della classe (informazioni dell'operatore)
    private String cf;        // Codice Fiscale
    private String name;      // Nome
    private String surname;   // Cognome
    private String email;     // Email
    private String username;  // Nome utente
    private String password;  // Password

    // Costruttore della classe Operatore
    public Operatore(String cf, String name, String surname, String email, String username, String password) throws Exception {
        // Esegui le validazioni sui dati prima di settarli
        if (!(this.checkCf(cf) & this.checkName(name) & this.checkSurname(surname) & this.checkEmail(email) & this.checkUsername(username) & this.checkPassword(password))) {
            // Se una delle validazioni fallisce, stampa un errore e non inizializza l'operatore
            System.out.println("Errore nei dati inseriti");
        } else {
            // Se tutte le validazioni passano, assegna i valori ai campi
            this.cf = cf;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.username = username;
            this.password = password;
        }
    }

    // Metodo per validare la password
    private boolean checkPassword(String password) {
        // Regola per una password sicura: almeno un numero, una lettera maiuscola, una minuscola, un simbolo e lunghezza minima di 8 caratteri
        Pattern PASSWORD_PATTERN = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        );
        // Se la password è nulla o vuota, ritorna falso
        if (password == null || password.isEmpty()) {
            System.out.println("Password errata");
            return false;
        }
        // Ritorna vero se la password corrisponde al pattern
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    // Metodo per validare l'email
    private boolean checkEmail(String email) {
        // Se l'email è nulla o vuota, ritorna falso
        if (email == null || email.isEmpty()) return false;

        // Rimuove gli spazi all'inizio e alla fine dell'email
        email = email.trim();
        // Pattern per validare l'email (simile alla struttura di un'email standard)
        Pattern EMAIL_PATTERN = Pattern.compile(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
        // Ritorna vero se l'email corrisponde al pattern
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Metodo per validare il nome (non deve essere nullo e deve avere lunghezza massima di 30 caratteri)
    private boolean checkName(String name) {
        // Se il nome è nullo o supera la lunghezza massima, ritorna falso
        if (name == null || name.length() > 30) return false;
        // Altrimenti ritorna vero
        return true;
    }

    // Metodo per validare il cognome (stessa logica della validazione del nome)
    private boolean checkSurname(String surname) {
        // Se il cognome è nullo o supera la lunghezza massima, ritorna falso
        if (surname == null || surname.length() > 30) return false;
        // Altrimenti ritorna vero
        return true;
    }

    // Metodo per validare il codice fiscale (deve rispettare un formato specifico di 16 caratteri)
    private boolean checkCf(String cf) {
        // Pattern che rappresenta il formato del codice fiscale italiano
        Pattern CF_PATTERN = Pattern.compile("^[A-Z]{6}[0-9]{2}[A-EHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$");

        // Se il codice fiscale è nullo o ha una lunghezza diversa da 16, ritorna falso
        if (cf == null || cf.length() != 16) return false;

        // Ritorna vero se il codice fiscale corrisponde al pattern
        return CF_PATTERN.matcher(cf).matches();
    }

    // Metodo per validare l'username (deve essere non nullo, avere una lunghezza minima di 3 e massima di 50 caratteri)
    private boolean checkUsername(String username) {
        // Se l'username è nullo, ha una lunghezza minore di 3 o maggiore di 50, ritorna falso
        if (username == null || username.length() < 3 || username.length() > 50) {
            System.out.println("Username non valido");
            return false;
        }
        // Altrimenti ritorna vero
        return true;
    }

    // Metodo per il login, che permette di usare il codice fiscale o lo username
    public boolean login(String identifier, String password) {
        // Se l'identificatore è il codice fiscale o l'username e la password corrisponde
        if ((identifier.equals(this.cf) || identifier.equals(this.username)) && this.password.equals(password)) {
            return true;  // Login riuscito
        }
        return false;  // Login fallito
    }

    // Getter e Setter per tutti i campi

    public String getCodiceFiscale() {
        return cf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
