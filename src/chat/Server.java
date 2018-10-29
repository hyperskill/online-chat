package chat;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {


    public static void main(String[] args) throws IOException {

        String address = "127.0.0.1";

            ServerSocket server = new ServerSocket(Configs.port, 50, InetAddress.getByName(address));





            while (true){
                autorize(server);

            }
        }


    static int clientnumber = 1;
    static volatile int count = 0;
    static volatile int count1=1;



    static ArrayList<Socket> clients = new ArrayList<>();



        static  void autorize(ServerSocket server) throws IOException {

        final Socket socket = server.accept();
        clients.add(socket);


        final String clientname = "client " + (clientnumber++);
        Thread thread = new ClientSocketThread(socket, clientname);

        thread.start();


        }
        static synchronized void sendAll() throws  IOException {


            if(count == count1){
            Thread thread = new ClientSocketThread();
                for(int i = 0; i < clients.size(); i ++) {
                    DataOutputStream outputStream = new DataOutputStream(clients.get(i).getOutputStream());
                    outputStream.writeUTF(((ClientSocketThread) thread).lastmessage);
                }

            count1++;
        }

    }

}
