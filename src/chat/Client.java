package chat;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    final static int port = Configs.port;

    public static void main(String[] args) throws Exception {

        String address = "127.0.0.1";


        Socket socket = new Socket(InetAddress.getByName(address), port);


        final DataInputStream input = new DataInputStream(socket.getInputStream());

        final DataOutputStream output = new DataOutputStream(socket.getOutputStream());


        Scanner in = new Scanner(System.in);
        String mes = "";
        String ans = "";
        ClientSide income = new ClientSide(in, output, input);


        boolean connect = false;

       System.out.println(input.readUTF());


        while(mes.equals("exit")!=true){
            mes = in.nextLine();
            output.writeUTF(mes);
            if(connect == false){
                income.start();
            connect = true;
            }
        }



    }
}
