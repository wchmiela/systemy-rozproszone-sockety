import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Nie podano poprawnie argumentow.");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        Server server = new Server(port);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(server);
    }
}