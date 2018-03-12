import asciiarts.AsciiArtAnimal;
import asciiarts.AsciiArtSword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Writer implements Runnable {

    private final Client client;
    private final PrintWriter out;
    private final BufferedReader stdIn;
    private final DatagramSocket socket;
    private boolean UDPMessage = false;

    Writer(Client client, PrintWriter out, BufferedReader stdIn, DatagramSocket socket) {
        this.client = client;
        this.out = out;
        this.stdIn = stdIn;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {

                    if (userInput.length() == 1 && (userInput.startsWith("U") || userInput.startsWith("T"))) {
                        switch (userInput) {
                            case "U":
                                UDPMessage = true;
                                break;
                            default:
                                UDPMessage = false;
                                break;
                        }
                    } else {
                        if (UDPMessage) {
                            sendUDP(userInput);
                        } else {
                            sendTCP(userInput);
                        }

                        if (userInput.equals("exit")) {
                            System.exit(0);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Blad w wyslaniu wiaodmosci od kleinta" + e.getMessage());
        }
    }

    private void sendTCP(String message) {
        if (message.length() > 0)
            out.println(message);
    }

    private void sendUDP(String message) throws IOException {
        byte[] sendBuffer;
        if (message.equals("ascii")) {
            sendBuffer = sendAscii();
        } else {
            sendBuffer = message.getBytes();
        }

        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length,
                InetAddress.getByName(client.getAddress()), client.getPort());
        socket.send(sendPacket);
    }

    private byte[] sendAscii() {
        byte[] sendBuffer;
        if (new Random().nextInt() % 2 == 0) {
            sendBuffer = new AsciiArtSword().getArt().getBytes();
        } else {
            sendBuffer = new AsciiArtAnimal().getArt().getBytes();
        }
        return sendBuffer;
    }
}
