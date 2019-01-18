package chat;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {

        System.out.println("Client started!");

        String address = "127.0.0.1";
        Scanner sc = new Scanner(System.in);

        int port = 12345;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        System.out.println(dis.readUTF());

        String str;
        //Nick picking
        while (true){
            str = sc.nextLine();
            dos.writeUTF(str);

           str = dis.readUTF();
            if (str.equals("run"))
                break;
            System.out.println(str);
        }
        str = dis.readUTF();
        System.out.print(str);
        readBuffer rd = new readBuffer(socket);
        rd.start();
        while (true) {
            str = sc.nextLine();
            if (str.equals("/exit")) {
                rd.alive = false;
                dos.writeUTF(str);
                rd.join();
                break;
            }
            dos.writeUTF(str);
        }
    }
}

class readBuffer extends Thread{
    DataInputStream dis;
    DataOutputStream dos;
    boolean alive = true;

    readBuffer(Socket socket) throws IOException {
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run(){
        String str = "";
        while (alive){
            try {
                str = dis.readUTF();
            } catch (IOException e) {
                break;
            }
            System.out.println(str);
        }
    }
}