package chat;

import java.net.InetAddress;
import java.net.Socket;

public class Client  implements ClientSide{

    static  Socket socket;
    public static void main(String[] args) throws Exception {

        System.out.println("Client started!");

        String address = "127.0.0.1";
        int port = 9103;
       socket = new Socket(InetAddress.getByName(address), port);

       ClientSide.hello(socket);
       ClientSide.outcome(socket, "Hello server from Client 1!");
       ClientSide.income(socket);
       ClientSide.outcome(socket,"I want to sent more messages");
       ClientSide.income(socket);
       ClientSide.outcome(socket,"exit");



    }



}
