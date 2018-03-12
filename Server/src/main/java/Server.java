import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server implements Runnable {

    private Set<Client> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static int clientsCount = 0;

    private final int port;

    private DatagramSocket udpServerSocket;

    Server(int serverPort) {
        this.port = serverPort;

        String message = String.format("TCP Server. Port: %d", port);
        System.out.println(message);
    }

    @Override
    public void run() {
        System.out.println("Oczekiwanie na klientow...");

        ExecutorService executor = null;
        ServerSocket serverSocket = null;

        try {
            udpServerSocket = new DatagramSocket(port);
        } catch (IOException e) {
            System.out.println("Blad w utworzeniu socketa udp " + e.getMessage());
        }

        while (true) {
            try {
                serverSocket = new ServerSocket(port);

                executor = Executors.newCachedThreadPool();
                Socket clientSocket = serverSocket.accept();

                Client client = new Client(this, "User" + clientsCount++, clientSocket);
                addClient(client);

                Runnable worker = new ClientHandler(this, client);
                Runnable udpworker = new UDPClientHandler(this, client);
                executor.execute(worker);
                executor.execute(udpworker);

            } catch (IOException e) {
                System.out.println("Blad w utworzeniu socketa " + e.getMessage());
            } finally {
                if (serverSocket != null)
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        System.out.println("Blad w zamknieciu socketa " + e.getMessage());
                    }

                if (executor != null) {
                    executor.shutdown();
                }
            }
        }
    }

    private void addClient(Client client) {
        clients.add(client);
        System.out.println(String.format("%s dolaczyl do chatu.", client.getName()));
    }

    public void removeClient(Client client) {
        clients.remove(client);
        System.out.println(String.format("%s opuscil chat.", client.getName()));
    }

    public void sendMessage(Client sender, String message) {
        getOtherClients(sender).forEach(client -> client.writeMessage(message));
    }

    public void sendUDPMessage(Client sender, String message) {
        getOtherClients(sender).forEach(client -> client.writeUDPMessage(message));
    }

    private Set<Client> getOtherClients(Client sender) {
        return clients.parallelStream().filter(x -> !x.equals(sender)).collect(Collectors.toSet());
    }

    public DatagramSocket getUdpServerSocket() {
        return udpServerSocket;
    }
}
