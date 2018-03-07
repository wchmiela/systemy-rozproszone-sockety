import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Nie podano poprawnie argumentow.");
            System.exit(1);
        }

        String address = args[0];
        int port = Integer.parseInt(args[1]);

        Client client = new Client(address, port);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(client);   
    }
}