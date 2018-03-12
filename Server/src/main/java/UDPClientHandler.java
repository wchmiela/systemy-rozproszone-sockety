import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;

public class UDPClientHandler implements Runnable {

    private final Server server;
    private final Client client;

    UDPClientHandler(Server server, Client client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            byte[] receiveBuffer = new byte[1024];

            while (true) {

                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                server.getUdpServerSocket().receive(receivePacket);

                String clientMessage = new String(receivePacket.getData()).trim();

                if (clientMessage.equals("exit")) {
                    server.removeClient(client);
                } else {
                    MessageFormatter formatter = new MessageFormatter(clientMessage, client.getName());
                    String formatedMessage = formatter.getMessage();

                    System.out.println(formatedMessage);

                    server.sendUDPMessage(client, String.format("%s#%s", client.getName(), clientMessage));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

