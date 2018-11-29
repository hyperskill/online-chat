package chat;

import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws Exception {
        System.out.println("Client started!");
        String address = "127.0.0.1";
        int port = 23456;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output  = new DataOutputStream(socket.getOutputStream());

        System.out.println(input.readUTF());
        output.writeUTF("Hello from client!");
    }
}
