package LAB_B;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Press Opt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        // Press Ctrl+R or click the green arrow button in the gutter to run the code.
        for (int i = 1; i <= 5; i++) {

            // Press Ctrl+D to start debugging your code. We have set one breakpoint
            // for you, but you can always add more by pressing Cmd+F8.
            System.out.println("i = " + i);
        }

        try {
            // Esegui il comando per aprire il cmd
            Process process = Runtime.getRuntime().exec("cmd /c start cmd");

            // Attendi 30 secondi (30000 millisecondi)
            Thread.sleep(30000);

            // Termina il processo (chiude il cmd aperto)
            process.destroy();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}