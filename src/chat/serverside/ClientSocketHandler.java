package chat.serverside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocketHandler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(ClientSocketHandler.class.getName());

    private final Socket clientSocket;
    private final String clientName;

    private final DataInputStream input;
    private final DataOutputStream output;
    private int wordsCounter;

    public ClientSocketHandler(Socket client, String name) throws IOException {
        this.clientSocket = client;
        this.clientName = name;
        this.input = new DataInputStream(client.getInputStream());
        this.output = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                final String receivedMsg = input.readUTF();
                if (receivedMsg.trim().equalsIgnoreCase("exit")) {
                    disconnect();
                    break;
                }

                System.out.println(clientName + ": " + receivedMsg);

                final String[] words = receivedMsg.split("\\s+");
                wordsCounter += words.length;

                output.writeUTF(String.format("You sent %d words total", wordsCounter));
            }
        } catch(IOException e){
            LOGGER.log(Level.SEVERE, String.format("Cannot interact with %s", clientName));
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
            LOGGER.log(Level.SEVERE, String.format("Cannot disconnect %s", clientName));
        }
    }
}
