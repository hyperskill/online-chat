package chat.serverside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private final Server server;
    private final Socket clientSocket;

    private final DataInputStream input;
    private final DataOutputStream output;

    private String clientName = "Unknown";

    public ClientHandler(Server server, Socket client) throws IOException {
        this.server = server;
        this.clientSocket = client;
        this.input = new DataInputStream(client.getInputStream());
        this.output = new DataOutputStream(client.getOutputStream());
    }

    public void sendMsg(String msg) throws IOException {
        output.writeUTF(msg);
    }

    @Override
    public void run() {
        try {
            this.clientName = input.readUTF();

            final String helloMsg = String.format("%s is ready to start chatting\n", clientName);
            server.broadcastMessage(helloMsg);

            while (true) {
                final String msg = input.readUTF();
                if (msg.trim().equalsIgnoreCase("/exit")) {
                    disconnect();
                    break;
                }

                LOGGER.info(String.format("%s sent \'%s\'", clientName, msg));

                server.broadcastMessage(clientName + ": " + msg);
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
            System.out.printf("%s disconnected from the server\n", clientName);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Cannot disconnect %s", clientName), e);
        }
    }

    public String getClientName() {
        return clientName;
    }
}
