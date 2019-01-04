package chat;


import java.net.InetAddress;
import java.net.Socket;


public class Client {


    public static void main(String[] args) throws Exception {
        Helper.write("Client started!");
        Socket socket = new Socket(InetAddress.getByName(Helper.getAddress()), Helper.getPort());
        Connection connection = new Connection(socket);
        while (true) {
            Helper.write("Server: write your name: ");
            String name = Helper.read();
            connection.send(new Message(MessageType.HELLO, name));
            Message message = connection.receive();
            Helper.write(message.getData());
            if(message.getType() == MessageType.VALID_NAME){
                break;
            }
        }
        while (true) {
            String msg = Helper.read();
            connection.send(new Message(MessageType.TEXT, msg));
            if (msg.equals("exit")) {
                break;
            }
            Message in = connection.receive();
            Helper.write(in.getData());
        }

    }
}