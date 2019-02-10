package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session extends Thread {

    private final String KEY = "theSecretKeyword";
    private final Socket socket;
    private int wordsCount = 0;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {
            AesCryptoAlgorithm aes = new AesCryptoAlgorithm();

            System.out.println("Client connected.");
            outputStream.writeUTF(aes.encryption("Hello from server", KEY));

            while (true) {
                String message = inputStream.readUTF();
                System.out.println("Encrypted message - " + message);
                message = aes.decryption(message, KEY);

                if ("exit".equals(message)) {
                    System.out.println("Client typed: " + message);
                    System.out.println("Client disconnected.");
                    break;
                }

                if (!"".equals(message) && message != null && !message.isEmpty()) {
                    System.out.println("Client typed: " + message);

                    message = countWordsInUserInput(message);
                    System.out.println("Sent to client: " + message);
                    outputStream.writeUTF(aes.encryption(message, KEY));
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String countWordsInUserInput(String message) {
        String[] words = message.trim().split("\\s+");
        wordsCount += words.length;
        return String.format("You send %d words total", wordsCount);
    }


}
