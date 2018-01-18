import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

class InputThread implements Runnable {
    private final DataInputStream stream;
    private final Queue<String> requests;

    InputThread(Socket socket, Queue<String> requests) throws IOException {
        this.stream = new DataInputStream(socket.getInputStream());
        this.requests = requests;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String input = this.stream.readUTF();
                synchronized (requests) {
                    requests.offer(input);
                    requests.notify();
                }
            } catch (IOException e) {
                break;
            }
        }

        try {
            this.stream.close();
        } catch (IOException e) {
        }
    }
}
