package chat.serverside;

import chat.common.Commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private final Server server;
    private final Socket clientSocket;

    private final DataInputStream input;
    private final DataOutputStream output;

    private final UUID clientId;
    private String clientName = "Unknown";

    public ClientHandler(UUID clientId, Server server, Socket client) throws IOException {
        this.clientId = clientId;
        this.server = server;
        this.clientSocket = client;
        this.input = new DataInputStream(client.getInputStream());
        this.output = new DataOutputStream(client.getOutputStream());
    }

    public void sendMsg(String msg) throws IOException {
        output.writeUTF(msg);
        output.flush();
    }

    @Override
    public void run() {
        try {
            this.clientName = input.readUTF();

            final String helloMsg = String.format("%s is ready to start chatting\n", clientName);
            server.broadcastMessage(helloMsg);

            while (true) {
                if (clientSocket.isClosed()) {
                    break;
                }

                final String msg = input.readUTF();
                if (msg.trim().equalsIgnoreCase(Commands.EXIT.getCode())) {
                    disconnect();
                    break;
                }

                final String msgWithClient = clientName + ": " + msg;
                LOGGER.info(msgWithClient);
                server.broadcastMessage(msgWithClient);
            }
        } catch(IOException e){
            LOGGER.log(Level.SEVERE, String.format("Cannot interact with %s", clientName), e);
            disconnect();
        }
    }

    private void disconnect() {
        try {
            clientSocket.close();
            input.close();
            output.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Cannot disconnect %s", clientName), e);
        }
        server.dropClient(clientId);
        server.broadcastMessage(String.format("%s disconnected from the server\n", clientName));
    }

    public String getClientName() {
        return clientName;
    }

    public UUID getClientId() {
        return clientId;
    }
}
