package chat;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {


    public static void main(String[] args) throws IOException {

            ServerSocket server = new ServerSocket(Configs.port, 50, InetAddress.getByName(Configs.adress));

            while (true){
                autorize(server);

            }
        }

        static  void autorize(ServerSocket server) throws IOException {

        final Socket socket = server.accept();
        Thread thread = new ClientSocketThread(socket);

        thread.start();

        }

    }




