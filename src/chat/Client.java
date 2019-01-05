package chat;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class Client {

    private static class ClientHelper extends Thread {
        private Connection connection;
        private boolean stopped = false;

        public ClientHelper(Connection connection) {
            this.connection = connection;
        }

        public void setStopped(boolean stopped) {
            this.stopped = stopped;
        }

        @Override
        public void run() {
            while (!stopped) {
                try {
                    Message in = connection.receive();
                    Helper.write(in.getData());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Helper.write("Client started!");
        Socket socket = new Socket(InetAddress.getByName(Helper.getAddress()), Helper.getPort());
        try(Connection connection = new Connection(socket)){
            clientHello(connection);
            ClientHelper clientHelper = new ClientHelper(connection);
            clientHelper.setDaemon(true);
            clientHelper.start();
            while (true) {
                String msg = Helper.read();
                connection.send(new Message(MessageType.TEXT, msg));
                if (msg.equals("/exit")) {
                    clientHelper.setStopped(true);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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