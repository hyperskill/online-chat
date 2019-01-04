package chat;


import java.net.InetAddress;
import java.net.Socket;


public class Client {


    public static void main(String[] args) throws Exception {
        Helper.write("Client started!");
        Socket socket = new Socket(InetAddress.getByName(Helper.getAddress()), Helper.getPort());
        Connection connection = new Connection(socket);
        while (true) {
            connection.send(Helper.read());
            String in = connection.receive();
            Helper.write(in);
        }

    }
}