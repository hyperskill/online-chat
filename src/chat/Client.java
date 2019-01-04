package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) throws Exception {
        String address = "127.0.0.1";
        int port = 23456;
        System.out.println("Client started!");
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        output.writeUTF("Hello from Client!");
        output.flush();
        String in = input.readUTF();
        System.out.println(in);


    }
}