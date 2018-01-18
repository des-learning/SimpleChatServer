import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

class OutputThread implements Runnable {
    private final DataOutputStream stream;
    private final Queue<String> responses;

    OutputThread(Socket socket, Queue<String> responses) throws IOException {
        this.stream = new DataOutputStream(socket.getOutputStream());
        this.responses = responses;
    }

    @Override
    public void run() {
        while (true) {
            String output = null;

            synchronized (responses) {
                output = responses.poll();
                if (output == null) {
                    try {
                        responses.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }

            try {
                if (output != null)
                    this.stream.writeUTF(output);
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
