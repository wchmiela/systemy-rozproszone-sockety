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

    private Client client;
    private final PrintWriter out;
    private final BufferedReader stdIn;
    private boolean udp = false;
    private final DatagramSocket socket;

    public Writer(Client client, PrintWriter out, BufferedReader stdIn, DatagramSocket socket) {
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
                                udp = true;
                                break;
                            default:
                                udp = false;
                                break;
                        }
                    } else {
                        if (udp) {
                            byte[] sendBuffer;
                            if (userInput.equals("ascii")) {
                                if (new Random().nextInt() % 2 == 0) {
                                    sendBuffer = new AsciiArtSword().getArt().getBytes();
                                } else {
                                    sendBuffer = new AsciiArtAnimal().getArt().getBytes();
                                }
                            } else {
                                sendBuffer = userInput.getBytes();
                            }

                            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length,
                                    InetAddress.getByName(client.getAddress()), client.getPort());
                            socket.send(sendPacket);
                        } else {
                            if (userInput.length() > 0) out.println(userInput);
                        }

                        if (userInput.equals("exit")) {
                            System.exit(1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("problemo");
        }
    }
}
