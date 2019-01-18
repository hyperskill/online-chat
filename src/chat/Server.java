package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server started!");
        int Clients = 1;
        String address = "127.0.0.1";
        int port = 12345;
        ServerSocket ss = new ServerSocket(port, 50, InetAddress.getByName(address));
        DataBase db = new DataBase();

        while (true) {
            Socket socket = ss.accept();
            Cloent cl = new Cloent(socket, Clients, db);
            cl.start();
            db.addClient(cl);
            Clients++;
        }



    }
}

class Cloent extends Thread{
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;
    DataBase db;
    int count = -1;
    int nbOfMsg = 10;//how many words get at enter chat


    Cloent(Socket socket, int cleintsNb, DataBase db) throws IOException {
        super("Client-" + cleintsNb);
        this.db = db;
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("Hello, pick your Nickname pleasse!");
    }

    @Override
    public void run(){
        System.out.println("Client connected.");
        String str = "";
        String selfName = "";

        //picking nickname
        while (true){
            try {
                str = dis.readUTF();
                System.out.println("Client wrote: " +str + " from " + Thread.currentThread().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (db.getNameBase().contains(str)){
                try {
                    dos.writeUTF("Name already used, please pick other.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    db.addName(str);
                    selfName = str;
                    dos.writeUTF("run");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        try {
            dos.writeUTF(db.getChat(nbOfMsg));
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (!str.equals("/exit")){
            try {
                str = dis.readUTF();
            } catch (Exception e) {
                try {
                    socket.close();
                    break;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
            if (!str.equals("/exit")) {
                db.addMessage(selfName + ": " + str);
                System.out.println("Client wrote: " + str + " from " + Thread.currentThread().getName());
                try {
                    //sending
                    for (Cloent a : db.getClients()) {
                        a.sendMsg(selfName + ": " + str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            count += getWordsCount(str);
        }
        try {
            Thread.sleep(1000);
            db.getClients().remove(this);
            socket.close();
            System.out.println("Client disconnected.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getWordsCount(String str){
        return str.split("\\s+").length;
    }

    private void sendMsg(String msg) throws IOException {
        dos.writeUTF(msg);
    }
}

class DataBase implements Serializable {
    private ArrayList<String> nameBase;
    private ArrayList<String> messages;
    private ArrayList<Cloent> clients;

    DataBase(){
        nameBase = new ArrayList<>();
        messages = new ArrayList<>();
        clients = new ArrayList<>();
        messages.add("Hello from the server!");
    }

    synchronized void addName(String nameBase) {
        this.nameBase.add(nameBase);
    }

    synchronized ArrayList getNameBase(){
        return nameBase;
    }

    synchronized String getChat(int nb){
        StringBuilder str = new StringBuilder();
        if (nb > messages.size())
            nb = messages.size();
        for (int i = 0; i < nb; i++){
            str.append(messages.get(messages.size() - (nb - i))).append("\n");
        }
        return str.toString();
    }

    synchronized void addMessage(String st){
        messages.add(st);
    }

    synchronized ArrayList<Cloent> getClients() {
        return clients;
    }

    synchronized void addClient(Cloent client){
        this.clients.add(client);
    }
}