import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ClientHandler implements Runnable {

    Server server;
    Client client;

    ClientHandler(Server server, Client client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getSocket().getOutputStream()));

            String clientMessage = null;

            while ((clientMessage = in.readLine()) != null) {
                Instant now = Instant.now();
                ZoneId zoneId = ZoneId.of("Europe/Warsaw");
                ZonedDateTime dateAndTimeInKrakow = ZonedDateTime.ofInstant(now, zoneId);

                String log = String.format("[%d:%d:%d]\t%s>%s", dateAndTimeInKrakow.getHour(),
                        dateAndTimeInKrakow.getMinute(), dateAndTimeInKrakow.getSecond(), client.getName(),
                        clientMessage);

                System.out.println(log);
                this.server.sendMessage(client, clientMessage);

                // String clientLog = String.format("%s : %s\n", client.getName(), clientMessage);
                // out.write(clientLog);
                // out.newLine();
                // out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}