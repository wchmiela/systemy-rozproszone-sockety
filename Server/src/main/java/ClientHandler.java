import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ClientHandler implements Runnable {

    private final Server server;
    private final Client client;

    ClientHandler(Server server, Client client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
            String clientMessage;

            while ((clientMessage = in.readLine()) != null) {
                MessageFormatter formatter = new MessageFormatter(clientMessage, client.getName());
                String formatedMessage = formatter.getMessage();
                System.out.println(formatedMessage);

                server.sendMessage(client, String.format("%s#%s", client.getName(), clientMessage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}