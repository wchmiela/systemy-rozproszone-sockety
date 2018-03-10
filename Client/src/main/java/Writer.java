import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Writer implements Runnable {

    private final PrintWriter out;
    private final BufferedReader stdIn;

    public Writer(PrintWriter out, BufferedReader stdIn) {
        this.out = out;
        this.stdIn = stdIn;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    out.println(userInput);
                }
            }
        } catch (IOException e) {
            System.out.println("problemo");
        }
    }
}
