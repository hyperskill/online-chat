package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server started!");

        String address = "127.0.0.1";
        int port = 12345;
        ServerSocket ss = new ServerSocket(port, 50, InetAddress.getByName(address));
        Socket socket = ss.accept();
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        String str = dis.readUTF();
        dos.writeUTF("Hello from server!");

        System.out.println(str);

    }
}


/*
    String[] allStr = str.split("\\s+");
            if (allStr[1].equals("joined") || allStr[1].equals("left") || allStr[1].equals("disconnected") || allStr[1].equals("connected")){
                    continue;
                    }
                    if (allStr[1].equals("sent")){
                    allStr[1] = ":";
                    System.out.print(allStr[0]);
                    for (int i = 1; i < allStr.length; i++){
        System.out.print(allStr[i] + " ");
        }
        System.out.println("");
        }*/