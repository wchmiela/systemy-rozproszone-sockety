import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable {

    private String address;
    private int port;
    private Socket clientSocket = null;

    Client(String clientAddress, int clientPort) {
        this.address = clientAddress;
        this.port = clientPort;

        String message = String.format("TCP Client. Adres: %s Port: %d", address, port);
        System.out.println(message);
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(address, port);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            ExecutorService ex = Executors.newCachedThreadPool();
            ex.execute(new Writer(out, stdIn));
            ex.execute(new Reader(in));

            while (true) ;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (clientSocket != null)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
