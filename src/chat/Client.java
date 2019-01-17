package chat;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {

        System.out.println("Client started!");

        String address = "127.0.0.1";
        Scanner sc = new Scanner(System.in);
        int count = -1;

        int port = 12345;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        System.out.println(dis.readUTF());

        String str;
        while (true) {
            System.out.print("Type message: ");
            str = sc.nextLine();
            count += str.split("\\s+").length;
            if (str.equals("exit")) {
                dos.writeUTF(str);
                break;
            }
            dos.writeUTF(str);
            str = dis.readUTF();
            System.out.println(str);
        }
    }
}