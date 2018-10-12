package chat.serverside;

import chat.common.Configs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static int clientNumber = 1;

    public static void main(String[] args) {
        try (final ServerSocket server = new ServerSocket(Configs.CHAT_SERVER_PORT)) {
            LOGGER.info(String.format("The chat server has been started on port %d", server.getLocalPort()));
            while (true) {
                regNewClient(server);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Cannot start server on %d", Configs.CHAT_SERVER_PORT), e);
        }
    }

    private static void regNewClient(ServerSocket server) {
        try {
            final Socket client = server.accept();
            final String clientName = "client-" + (clientNumber++);
            final Thread clientThread = new ClientSocketHandler(client, clientName);
            clientThread.start();
            System.out.printf("%s connected to the server\n", clientName);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot register a new client", e);
        }
    }
}