import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.Collections;

public class ChatServer {
    public static void main(String []args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(1286);
        final Executor pool = Executors.newCachedThreadPool();
        boolean loop = true;
        Set<ChatWorker> workers = Collections.synchronizedSet(new HashSet<ChatWorker>());

        while (loop) {
            Socket socket = serverSocket.accept();
            ChatWorker worker = new ChatWorker(socket, workers, pool);

            synchronized (workers) {
                workers.add(worker);
            }

            pool.execute(worker);
        }

        serverSocket.close();
    }
}
