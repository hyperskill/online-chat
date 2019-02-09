package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) throws Exception {
        System.out.println("Client started!");

        try (
                Socket socket = new Socket(ADDRESS, PORT);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {

            outputStream.writeUTF("Hello from client");
            System.out.println(inputStream.readUTF());
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}