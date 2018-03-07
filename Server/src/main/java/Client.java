import java.net.Socket;

public class Client {
    private String name;
    private Socket socket;

    Client(String clientName, Socket clientSocket) {
        this.name = clientName;
        this.socket = clientSocket;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }
}