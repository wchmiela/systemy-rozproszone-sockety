import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable {

    private String address;
    private int port;
    private Socket clientSocket;
    private DatagramSocket clientDatagramSocket;

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
            clientDatagramSocket = new DatagramSocket(clientSocket.getLocalPort()); //dlaczego tutaj localport?

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            ExecutorService ex = Executors.newCachedThreadPool();
            Runnable writer = new Writer(this, out, stdIn, clientDatagramSocket);
            Runnable reader = new Reader(this, in);
            Runnable udpReader = new UDPReader(this, clientDatagramSocket);
            ex.execute(writer);
            ex.execute(reader);
            ex.execute(udpReader);

            while(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clientDatagramSocket != null) {
                clientDatagramSocket.close();
            }
        }
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}

