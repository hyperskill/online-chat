package chat.serverside;

import chat.common.Configs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private static ArrayList<ClientHandler> handlers = new ArrayList<>();

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (final ServerSocket server = new ServerSocket(port)) {
            LOGGER.info(String.format("The chat server has been started on port %d", server.getLocalPort()));
            while (true) {
                LOGGER.info("Waiting for a client");
                final Socket client = server.accept();
                regNewClient(client);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Cannot start server on %d", Configs.CHAT_SERVER_PORT), e);
        }
    }

    public synchronized void regNewClient(Socket client) {
        try {
            final ClientHandler clientThread = new ClientHandler(this, client);
            clientThread.start();
            handlers.add(clientThread);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot register a new client", e);
        }
    }

    public synchronized void broadcastMessage(String msg) {
        handlers.forEach(handler -> {
            try {
                handler.sendMsg(msg);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, String.format("Cannot send message to %s", handler.getClientName()), e);
            }
        });
    }

    public static void main(String[] args) {
        final Server chatServer = new Server(Configs.CHAT_SERVER_PORT);
        chatServer.start();
    }
}