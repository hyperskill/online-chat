package chat;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                Connection connection = new Connection(socket);
                String in = null;

                while (true) {
                    in = connection.receive();
                    Helper.write(in);
                    connection.send("Hello from Server!");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

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