package chat;

import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server started!");
        String address = "127.0.0.1";
        int port = 23456;
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        Socket socket = server.accept();
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
        output.writeUTF("Hello from server!");
        System.out.println(input.readUTF());
    }
}
