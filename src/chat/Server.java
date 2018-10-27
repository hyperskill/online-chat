package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Server started!");

        String address = "127.0.0.1";
        int port = 9103;
        int port2 = 9104;

        ServerSocket server = new ServerSocket(port, 50,  InetAddress.getByName(address));
        Socket socket = server.accept();
        ServerSocket serverSocket = new ServerSocket(port2,50, InetAddress.getByName(address));
        Socket socket1 = serverSocket.accept();

            ServerThread thread = new ServerThread(socket);

        ServerThread thread1 = new ServerThread(socket1);

        thread.start();

        thread1.start();


    }
}
