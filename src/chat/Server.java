package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final static String CHAT_SERVER_ADDRESS = "127.0.0.1";
    private final static int CHAT_SERVER_PORT = 23456;

    public static void main(String[] args) {
        try (final ServerSocket server = new ServerSocket(CHAT_SERVER_PORT, 50, InetAddress.getByName(CHAT_SERVER_ADDRESS));
             final Socket socket = server.accept()) {

            final DataInputStream input = new DataInputStream(socket.getInputStream());
            final String msg = input.readUTF();
            System.out.println(msg);

            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF("Hello from Server!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format("Cannot start server on %s:%d", CHAT_SERVER_ADDRESS, CHAT_SERVER_PORT));
        }
    }
}