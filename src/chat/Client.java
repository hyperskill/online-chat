package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final String KEY = "theSecretKeyword";

    public static void main(String[] args) throws Exception {
        AesCryptoAlgorithm aes = new AesCryptoAlgorithm();

        System.out.println("Client started!");

        try (
                Socket socket = new Socket(ADDRESS, PORT);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(aes.decryption(inputStream.readUTF(), KEY));

            while (true) {
                System.out.print("Type message: ");
                String message = scanner.nextLine();

                if ("".equals(message.replaceAll("\\s+", ""))) {
                    continue;
                } else {
                    String encryptMessage = aes.encryption(message, KEY);
                    outputStream.writeUTF(encryptMessage);
                }

                if ("exit".equals(message)) {
                    break;
                }

                System.out.println(aes.decryption(inputStream.readUTF(), KEY));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}