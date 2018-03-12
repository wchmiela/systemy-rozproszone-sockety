import java.io.BufferedReader;
import java.io.IOException;

public class Reader implements Runnable {

    private final BufferedReader in;

    Reader(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String clientMessage;
            do {
                clientMessage = in.readLine();
                if (clientMessage != null) {
                    String clientName = clientMessage.split("#")[0];
                    String message = clientMessage.split("#")[1];
                    MessageFormatter formatter = new MessageFormatter(message, clientName);
                    String formatedMessage = formatter.getMessage();

                    System.out.println(formatedMessage);
                }
            } while (true);
        } catch (IOException e) {
            System.out.println("Blad w odbiorze wiadomosci tcp " + e.getMessage());
        }
    }
}