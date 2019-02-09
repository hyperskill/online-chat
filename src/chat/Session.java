package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session extends Thread {

    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {
            String msg = inputStream.readUTF();

            if (!"".equals(msg) && msg != null && !msg.isEmpty()) {
                System.out.println(msg);
                outputStream.writeUTF("Hello from server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
