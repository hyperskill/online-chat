package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public interface ClientSide {

    public static  void hello(Socket socket) throws IOException {

        DataInputStream input = new DataInputStream(socket.getInputStream());
        System.out.println(input.readUTF());

    }
    public static void outcome(Socket socket, String message) throws IOException {

        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        System.out.println("Type message: " + message);
        output.writeUTF(message);
    }
    public static  void  income (Socket socket) throws IOException{


        DataInputStream input = new DataInputStream(socket.getInputStream());



        String income = input.readUTF();
        System.out.println(income);
    }
}
