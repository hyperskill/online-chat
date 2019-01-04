package chat;


import java.net.InetAddress;
import java.net.Socket;


public class Client {


    public static void main(String[] args) throws Exception {
        Helper.write("Client started!");
        Socket socket = new Socket(InetAddress.getByName(Helper.getAddress()), Helper.getPort());
        Connection connection = new Connection(socket);
        Helper.write("Name: ");
        String name = Helper.read();
        connection.send(new Message(MessageType.HELLO,name));
        Helper.write(connection.receive().getData());
        while (true) {
            Helper.write("Message: ");
            String msg = Helper.read();
            connection.send(new Message(MessageType.TEXT,msg));
            if(msg.equals("exit")){
                break;
            }
            Message in = connection.receive();
            Helper.write(in.getData());
        }

    }
}