import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UDPReader implements Runnable {

    private Client client;
    DatagramSocket socket;

    public UDPReader(Client client, DatagramSocket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            byte[] receiveBuffer = new byte[1024];

            while (true) {

                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                String clientName = new String(receivePacket.getData()).split("#")[0];
                String message = new String(receivePacket.getData()).split("#")[1].trim();

                MessageFormatter formatter = new MessageFormatter(message, clientName);
                String formatedMessage = formatter.getMessage();

                System.out.println(formatedMessage);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
