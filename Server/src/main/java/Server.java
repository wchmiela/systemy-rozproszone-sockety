import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private Set<Client> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static int clientsCount = 0;

    private final int port;

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

        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                executor = Executors.newCachedThreadPool();
                Socket clientSocket = serverSocket.accept();

                Client client = new Client("User" + clientsCount++, clientSocket);
                addClient(client);

                Runnable worker = new ClientHandler(this, client);
                executor.execute(worker);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null)
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
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

    private void removeClient(Client client) {
        clients.remove(client);
        System.out.println(String.format("%s opuscil chat.", client.getName()));
    }

    public void sendMessage(Client sender, String message) {
        clients.parallelStream().filter(x -> !x.equals(sender)).forEach(client -> client.writeMessage(message));
    }
}
