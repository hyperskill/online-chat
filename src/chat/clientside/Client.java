package chat.clientside;

import chat.common.Commands;
import chat.common.Configs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private final String name;

    public Client(String name) {
        this.name = name;
    }

    public void start() {
        final Scanner scanner = new Scanner(System.in);

        try (final Socket socket = new Socket(InetAddress.getByName(Configs.CHAT_SERVER_ADDRESS), Configs.CHAT_SERVER_PORT)) {
            LOGGER.info(String.format("Established connection to %s", socket.getRemoteSocketAddress().toString()));

            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            final DataInputStream input = new DataInputStream(socket.getInputStream());

            final Thread receivedMessagesHandler = new Thread(() -> {
                try {
                    while (true) {
                        final String msg = input.readUTF();
                        final LocalDateTime now = LocalDateTime.now().withNano(0);
                        System.out.println(String.format("[%s] %s", now.toString().replace('T', ' '), msg));
                    }
                } catch(IOException e){
                    LOGGER.log(Level.SEVERE, "Cannot interact with the server");
                }
            });

            receivedMessagesHandler.start();

            output.writeUTF(name);

            while (true) {
                final String msgToSend = scanner.nextLine();
                output.writeUTF(msgToSend);

                if (msgToSend.trim().equalsIgnoreCase(Commands.EXIT.getCode())) {
                    break;
                }
            }

            socket.close();
            input.close();
            output.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot interact with the server", e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please, specify your name as an argument when starting it.");
            return;
        }

        Client client = new Client(args[0]);
        client.start();
    }
}