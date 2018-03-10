import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final String name;
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String name, Socket socket) {
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

    public Socket getSocket() {
        return socket;
    }

    public void writeMessage(String message) {
        out.println(message);
    }
}