package chat;

import java.net.InetAddress;
import java.net.Socket;

public class Client2 implements ClientSide{
    static Socket socket;
    public static void main(String[] args) throws Exception {

        System.out.println("Client2 started!");

        String address = "127.0.0.1";
        int port = 9104;
        socket = new Socket(InetAddress.getByName(address), port);

        ClientSide.hello(socket);
        ClientSide.outcome(socket,"Hello server from Client 2!");
        ClientSide.income(socket);
        ClientSide.outcome(socket,"Client2 want to talk!");
        ClientSide.income(socket);
        ClientSide.outcome(socket,"exit");

    }}
