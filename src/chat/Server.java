package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server started!");
        String address = "127.0.0.1";
        int port = 23456;

        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        Socket socket = server.accept();
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output  = new DataOutputStream(socket.getOutputStream());

        String in = input.readUTF();
        System.out.println(in);
        output.writeUTF("Hello from Server!");
        output.flush();


    }
}