import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UDPReader implements Runnable {

    private final DatagramSocket socket;

    UDPReader(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            byte[] receiveBuffer = new byte[1024];

            do {
                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                String clientName = new String(receivePacket.getData()).split("#")[0];
                String message = new String(receivePacket.getData()).split("#")[1].trim();

                MessageFormatter formatter = new MessageFormatter(message, clientName);
                String formattedMessage = formatter.getMessage();

                System.out.println(formattedMessage);
            } while (true);
        } catch (IOException e) {
            System.out.println("Blad w odbiorze wiadomosci udp " + e.getMessage());
        }
    }
}
