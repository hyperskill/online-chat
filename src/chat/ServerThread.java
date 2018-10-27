package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {



    int count = 0;

    private Socket socket;
    public ServerThread(Socket socket) throws IOException{
        this.socket = socket;




    }


    public  void send(DataOutputStream output) {
        try {
           System.out.println("Client connected.");
            output.writeUTF("Hello from server!");





        }catch (IOException e){
            System.out.println("I cannot sent Hello");
        }
    }

    public int count(String message){

        String trimmed = message.trim();
        int count = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
       return  count;
    }



    public  void getMessage() throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream input = new DataInputStream(socket.getInputStream());
        String message = input.readUTF();

        System.out.println("Client typed: " + message);

        if (message.equals("exit")) {
            System.out.println("Client disconnected.");

        } else {

            count += count(message);
            String count_words = "You sent " + count + " words total!";
            output.writeUTF(count_words);

            System.out.println("Sent to Client: " + count_words);
            getMessage();
        }
    }

    public  void  run(){
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            send(output);
            getMessage();

        }catch (IOException e){
            System.out.println("I cannot connect");
        }
      
    }
}
