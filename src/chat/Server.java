package chat;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
    private static Map<String, Connection> clients = new ConcurrentHashMap<>();
    private static Map<String, Integer> registration = new ConcurrentHashMap<>();

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (Connection connection = new Connection(socket)) {
                String clientName = serverHello(connection);
                dialogServer(clientName, connection);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private static String serverHello(Connection connection) throws IOException, ClassNotFoundException {
        while (true) {
            Message in = connection.receive();
            String request = in.getData();
            String[] requestWords = request.split(" ");
            if (request.startsWith("/reg") && requestWords.length == 3) {
                String clientName = requestWords[1];
                String password = requestWords[2];
                if (registration.containsKey(clientName)) {
                    connection.send(new Message(MessageType.TEXT, "Server: you have already authorized"));
                } else{
                    if (password.length() < 8) {
                        connection.send(new Message(MessageType.TEXT, "Server: password too short"));
                        continue;
                    }
                    clients.put(clientName, connection);
                    registration.put(clientName, password.hashCode());
                    connection.send(new Message(MessageType.VALID_NAME,"Server: you are registered successfully" ));
                    Helper.write(clientName + " connected.");
                    return clientName;
                }
            } else if (request.startsWith("/auth")&& requestWords.length == 3) {
                String clientName = requestWords[1];
                String password = requestWords[2];
                if (registration.containsKey(clientName)) {
                    if(password.hashCode() != registration.get(clientName)){
                        connection.send(new Message(MessageType.TEXT, "Server: password wrong"));
                        continue;
                    }
                    clients.put(clientName, connection);
                    connection.send(new Message(MessageType.VALID_NAME, "Server: you are authorized successfully"));
                    Helper.write(clientName + " connected.");
                    return clientName;
                } else  {
                    connection.send(new Message(MessageType.TEXT,"Server: you are not registered"));
                }
            }else{
                connection.send(new Message(MessageType.TEXT, "Server: please start with (/auth or /reg) name pass"));
            }
        }
    }

    private static void dialogServer(String clientName, Connection connection) throws IOException, ClassNotFoundException {
        boolean stopped = false;
        while (!stopped) {
            Message in = connection.receive();
            String text;
            switch (in.getData()) {
                case "/list":
                    text = clients.keySet().toString();
                    connection.send(new Message(MessageType.TEXT, text));
                    break;
                case "/exit": {
                    text = clientName + " disconnected.";
                    Helper.write(text);
                    clients.remove(clientName);
                    stopped = true;
                    for (Map.Entry<String, Connection> pair : clients.entrySet()) {
                        pair.getValue().send(new Message(MessageType.TEXT, text));

                    }
                }
                break;
                default:
                    text = clientName + ": " + in.getData();
                    for (Map.Entry<String, Connection> pair : clients.entrySet()) {
                        pair.getValue().send(new Message(MessageType.TEXT, text));

                    }

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