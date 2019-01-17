package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server started!");
        int Clients = 1;
        String address = "127.0.0.1";
        int port = 12345;
        ServerSocket ss = new ServerSocket(port, 50, InetAddress.getByName(address));

        while (true) {
            Socket socket = ss.accept();
            Cloent cl = new Cloent(socket, Clients);
            cl.start();
            Clients++;
        }



    }
}

class Cloent extends Thread{
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;
    int count = -1;

    Cloent(Socket socket, int cleintsNb) throws IOException {
        super("Client-" + cleintsNb);
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("Connected to server. Hello!");
    }

    @Override
    public void run(){
        System.out.println("Client connected.");
        String str = "";
        while (!str.equals("exit")){
            try {
                str = this.dis.readUTF();
            } catch (Exception e) {
                try {
                    socket.close();
                    break;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
            if (!str.equals("exit")) {
                System.out.println("Client wrote: " +str + " from " + Thread.currentThread().getName());
            }
            try {
                System.out.println("Send to client: " + "You wrote " + getWordsCount(str) + " word(s).");
                dos.writeUTF("You wrote " + getWordsCount(str) + " word(s).");
            } catch (Exception e) {
                e.printStackTrace();
            }
            count += getWordsCount(str);
        }
        try {
            Thread.sleep(1000);
            socket.close();
            System.out.println("Client disconnected.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getWordsCount(String str){
        return str.split("\\s+").length;
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