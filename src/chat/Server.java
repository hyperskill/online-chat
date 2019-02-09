package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) throws Exception {
        System.out.println("Server started!");

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            while (true) {
                Session session = new Session(serverSocket.accept());
                session.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}