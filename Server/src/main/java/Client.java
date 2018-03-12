import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    private final Server server;
    private final String name;
    private final Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    Client(Server server, String name, Socket socket) {
        this.server = server;
        this.name = name;
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Wystapil blad w tworzeniu buforow");
        }
    }

    public String getName() {
        return name;
    }

    public void writeMessage(String message) {
        out.println(message);
    }

    public void writeUDPMessage(String message) {
        byte[] sendBuffer = message.getBytes();

        try {
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, socket.getInetAddress(), socket.getPort());
            server.getUdpServerSocket().send(sendPacket);
        } catch (IOException e) {
            System.out.println("Blad w wyslaniu datagramu udp " + e.getMessage());
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }
}