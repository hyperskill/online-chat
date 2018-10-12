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

    public static void main(String[] args) throws Exception {
        try {
            final Socket socket = new Socket(InetAddress.getByName(CHAT_SERVER_ADDRESS), CHAT_SERVER_PORT);
            final DataInputStream input = new DataInputStream(socket.getInputStream());
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format("Cannot connect to %s on port %d", CHAT_SERVER_ADDRESS, CHAT_SERVER_PORT));
        }
    }
}