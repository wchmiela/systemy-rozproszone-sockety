import java.io.BufferedReader;
import java.io.IOException;

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
            BufferedReader in = client.getIn();
            String clientMessage;

            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.equals("exit")) {
                    server.removeClient(client);
                } else {
                    MessageFormatter formatter = new MessageFormatter(clientMessage, client.getName());
                    String formatedMessage = formatter.getMessage();
                    System.out.println(formatedMessage);

                    server.sendMessage(client, String.format("%s#%s", client.getName(), clientMessage));
                }
            }
        } catch (IOException e) {
            System.out.println("Blad w dzialaniu handlera " + e.getMessage());
        }
    }
}