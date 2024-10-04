package LAB_B.Common;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Operatore implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cf;
    private String name;
    private String surname;
    private String userId;
    private String mail;
    private String password;

    public Operatore(String cf, String name, String surname,String userId,String mail, String password) throws Exception {
        if (!(this.checkCf(cf) | this.checkName(name) | this.checkSurname(surname) | this.checkUserId(userId) | this.checkMail(mail) | this.checkPsw(password))){
            System.out.println(this.checkCf(cf) | this.checkName(name) | this.checkSurname(surname) | this.checkUserId(userId) | this.checkMail(mail) | this.checkPsw(password));
        } else {
            this.cf = cf;
            this.name = name;
            this.surname = surname;
            this.userId = userId;
            this.mail = mail;
            this.password=password;
        }
    }

    /*
    Regex per la password:

    ^(?=.*[0-9]): La password deve contenere almeno un numero.
    (?=.*[a-z]): La password deve contenere almeno una lettera minuscola.
    (?=.*[A-Z]): La password deve contenere almeno una lettera maiuscola.
    (?=.*[@#$%^&+=]): La password deve contenere almeno un carattere speciale tra @, #, $, %, ^, &, +, =.
    (?=\\S+$): La password non deve contenere spazi.
    . {8,}$: La password deve essere lunga almeno 8 caratteri.
     */

    private boolean checkPsw( String password){
        Pattern PASSWORD_PATTERN = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        );
        if(password==null || password.isEmpty()) {
            System.out.println("password errata");
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();

    }



    /*
        Regex per la validazione dell'email:
        ^[A-Za-z0-9._%+-]+: Inizio dell'email, può contenere lettere maiuscole/minuscole, numeri, e alcuni caratteri speciali come . _ % + -.
        @: L'email deve contenere il simbolo @.
        [A-Za-z0-9.-]+: Il dominio deve contenere lettere, numeri, punti o trattini.
        \\.[A-Za-z]{2,}$: Il dominio deve terminare con un punto seguito da almeno due lettere (es: .com, .it).
         */
    private Boolean checkMail( String mail){
        Pattern EMAIL_PATTERN = Pattern.compile(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
        if (mail == null || mail.isEmpty()) return false;
        return EMAIL_PATTERN.matcher(mail).matches();
    }

    private boolean checkUserId ( String userId){
        if(userId == null || userId.length()>12) return false;
        return true;
    }
    private boolean checkName(String name){
        if(name == null || name.length()>30) return false;
        return true;
    }
    private boolean checkSurname(String surname){
        if(surname == null || surname.length()>30) return false;
        return true;
    }


    private boolean checkCf (String cf){
        // Pattern che definisce la struttura di un codice fiscale
        Pattern CF_PATTERN = Pattern.compile("^[A-Z]{6}[0-9]{2}[A-EHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$");

        if( cf == null || cf.length()!=16)return false;

        return CF_PATTERN.matcher(cf).matches();
    }

    //getter and setter

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
