package chat.clientside;

import chat.common.Configs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        try (final Socket socket = new Socket(InetAddress.getByName(Configs.CHAT_SERVER_ADDRESS), Configs.CHAT_SERVER_PORT)) {
            LOGGER.info(String.format("Established connection to %s", socket.getRemoteSocketAddress().toString()));

            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            final DataInputStream input = new DataInputStream(socket.getInputStream());

            while (true) {
                final String msgToSend = scanner.nextLine();
                output.writeUTF(msgToSend);

                if (msgToSend.trim().equalsIgnoreCase("exit")) {
                    socket.close();
                    input.close();
                    output.close();
                    break;
                }

                final String receivedMsg = input.readUTF();
                System.out.println(receivedMsg);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot interact with the server", e);
        }
    }
}