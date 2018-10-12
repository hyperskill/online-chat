package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private final static String CHAT_SERVER_ADDRESS = "127.0.0.1";
    private final static int CHAT_SERVER_PORT = 23456;

    public static void main(String[] args) {
        try (final Socket socket = new Socket(InetAddress.getByName("localhost"), CHAT_SERVER_PORT)) {
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF("Hello from Client!");
            final DataInputStream input = new DataInputStream(socket.getInputStream());
            final String msg = input.readUTF();
            System.out.println(msg);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format("Cannot interact with server on %s:%d", CHAT_SERVER_ADDRESS, CHAT_SERVER_PORT), e);
        }
    }
}