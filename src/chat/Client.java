package chat;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class Client {

    private static class ClientHelper extends Thread {
        private Connection connection;


        public ClientHelper(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String msg = Helper.read();
                    connection.send(new Message(MessageType.TEXT, msg));
                    if (msg.equals("exit")) {
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    public static void main(String[] args) throws Exception {
        Helper.write("Client started!");
        Socket socket = new Socket(InetAddress.getByName(Helper.getAddress()), Helper.getPort());
        Connection connection = new Connection(socket);
        clientHello(connection);
        ClientHelper clientHelper = new ClientHelper(connection);
        clientHelper.start();
        while (true) {
            Message in = connection.receive();
            Helper.write(in.getData());
        }
    }

    private static void clientHello(Connection connection) {
        while (true) {
            try {
                Helper.write("Server: write your name: ");
                String name = Helper.read();
                connection.send(new Message(MessageType.HELLO, name));
                Message message = connection.receive();
                Helper.write(message.getData());
                if (message.getType() == MessageType.VALID_NAME) {
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}