package chat;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void send(String message) throws IOException {
        synchronized (out) {
            out.writeUTF(message);
            out.flush();
        }
    }

    public String receive() throws IOException, ClassNotFoundException {
        synchronized (in) {
            return in.readUTF();
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
        out.close();
        in.close();
    }
}
