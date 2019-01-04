package chat;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
    private static Map<String, Connection> clients = new ConcurrentHashMap<>();
    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (Connection connection = new Connection(socket)) {
             String clientName = serverHello(connection);
             dialogServer(clientName,connection);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private static String serverHello(Connection connection) throws IOException, ClassNotFoundException {
        while (true) {
            Message in = connection.receive();
            if(in.getType() == MessageType.HELLO){
                String clientName = in.getData();
                connection.send(new Message(MessageType.TEXT,"Hello from Server, "+in.getData()+"!"));
                Helper.write(clientName+ " connected.");
                return clientName;
            }
        }
    }

    private static void dialogServer(String clientName,Connection connection) throws IOException, ClassNotFoundException{
        while (true){
            Message in = connection.receive();
            if(in.getType() != MessageType.TEXT){
                Helper.write("Not a text!");
                continue;

            }
            String text = in.getData();
            Helper.write(clientName+" typed: "+text);
            if(text.equals("exit")){
                Helper.write(clientName+ " disconnected.");
                break;
            }
            connection.send(new Message(MessageType.TEXT,"Words in message "+text.split("\\s+").length));

        }

    }

    public static void main(String[] args) throws Exception {
        Helper.write("Server started!");

        try (ServerSocket server = new ServerSocket(Helper.getPort(), 50, InetAddress.getByName(Helper.getAddress()))) {
            while (true) {
                Socket socket = server.accept();
                Handler handler = new Handler(socket);
                handler.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}