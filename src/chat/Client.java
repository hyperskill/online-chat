package chat;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        System.out.println("Client started!");

        String address = "127.0.0.1";
        int port = 12345;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        dos.writeUTF("Hello from client!");
        String str = dis.readUTF();
        System.out.println(str);
    }
}