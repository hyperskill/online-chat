package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientSocketThread extends Thread {
    Socket socket;
    String clientName;
    DataInputStream in;
    DataOutputStream output;
    static volatile ArrayList<String>chat = new ArrayList<>();
    static volatile String lastmessage = "";
    static volatile ArrayList<String> users = new ArrayList<>();



    public  ClientSocketThread(){

   }

    public ClientSocketThread(Socket socket, String clientname) throws IOException {

this.socket = socket;
this.clientName = clientname;
this.in = new DataInputStream(socket.getInputStream());
this.output = new DataOutputStream(socket.getOutputStream());
    }
    @Override
    public void run(){


        try {
            getNickname();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < chat.size(); i ++){
            try {
                output.writeUTF(chat.get(i));
            } catch (IOException e) {
                System.out.println("Cannot sent all message history");
            }
        }

        while (true){
            try{
                String mes = in.readUTF();


                if(mes.equals("exit")){
                    socket.close();
                    break;
                }else {
                    mes = clientName + ": " + mes;
                    System.out.println(mes);
                    AddAndSend(mes);
                }
            }catch (IOException e){
                System.out.println("cannot sent message");
                break;
            }


        }
    }


    public  synchronized void getNickname() throws IOException {
        output.writeUTF("give me ur name: ");
       String name = in.readUTF();
       if(users.contains(name) == true){
           output.writeUTF("its name already close");
           getNickname();
       }else{
           users.add(name);
           clientName = name;
       }

    }
    private synchronized void AddAndSend(String mes) {

        this.lastmessage = mes;

        int len = chat.size();
        if(chat.size()<9){
            chat.add(mes);
        }else{
            chat.remove(0);
            chat.add(mes);


        }

        Server.count++;
        try {
            Server.sendAll();
        } catch (IOException e) {
            System.out.println("we cannot get message");
        }


    }



}
