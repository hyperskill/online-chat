package chat;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientSocketThread extends Thread {
    Socket socket;
    String clientName; //имя пользователя
    DataInputStream in;
    DataOutputStream output;
    static volatile   Map  <String, Socket> logg = new HashMap<String, Socket>(); //связь юзер-сокет для создания диалога
    static  volatile Map<String, Integer> passwordmap = new HashMap<String, Integer>(); //Map с никнеймом и хэш-кодом пароля
    volatile String dialog; // имя пользователя, с которым создаем диалог
    volatile String filename; // имя файла, в котором будет сохранен диалог
    static volatile Map <String, File> datamessage_acsess= new HashMap<>(); //ничего интереснее не придумала


    public ClientSocketThread(Socket socket) throws IOException {

this.socket = socket;
this.in = new DataInputStream(socket.getInputStream());
this.output = new DataOutputStream(socket.getOutputStream());
    }
    @Override
    public void run(){
        try {
            getNickname();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true){
            try{
                String mes = in.readUTF();
                        String [] chatting = mes.split(" ", 2); // для распоззнания команд и обычных сообщений в диалоге
                            switch (chatting[0]) {
                                case ("/exit"):
                                    AddAndSend(clientName, dialog, mes);
                                case ("/list"):
                                    String ArrayOfClients = logg.keySet().toString();
                                    output.writeUTF("online: " + ArrayOfClients.substring(1, ArrayOfClients.length() - 1));
                                    break;
                                case ("/chat"):

                                    String str = clientName+chatting[1]; //формируем такое название файла, чтобы сообщения
                                    //из потоков двух пользователей в одном диалоге сохранялись в общий файл
                                    str = Sorted(str);
                                    filename = str+".txt";

                                    if(datamessage_acsess.containsKey(filename)){ //при подключении к чату печатаем последние 10 сообщений
                                        int count = 0;
                                        Scanner scanner = new Scanner(datamessage_acsess.get(filename));
                                        while (scanner.hasNext()){
                                            count++;
                                            scanner.nextLine();
                                        }
                                        int marker = 0;
                                        Scanner newamount = new Scanner(datamessage_acsess.get(filename));//сканер не возвращается к первой строке
                                            marker = count - 10;
                                            count = 0;
                                            while (count < marker){
                                                newamount.nextLine();
                                                count++;
                                            }
                                            while (newamount.hasNextLine()){
                                                SendHistoryChat(socket, newamount.nextLine());
                                            }
                                    }else { //если filename еще не добавлен в список, значит, диалог пуст
                                        File file = new File(Configs.path, filename);
                                        file.createNewFile();
                                        datamessage_acsess.put(filename, file);
                                    }
                                    this.dialog = chatting[1];

                                    break;
                                default: //это случай, когда было отправлено обычное сообщение (не команда)

                                    System.out.println(clientName + ": " + mes);
                                    System.out.println(dialog);
                                    AddAndSend(clientName, dialog, mes);
                                    break;
                            }

            }catch (IOException e){
                System.out.println("cannot sent message");
                break;
            }
        }
    }

    private String Sorted(String str) {
        char [] chars = str.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return "file"+sorted+".txt";
    }

    public  synchronized void getNickname() throws IOException { //метод авторизации
        output.writeUTF("authorize or register: ");
        String name = in.readUTF();
        String[] enter = name.split(" ");
        System.out.println(name);
        System.out.println(enter[0]);
        System.out.println(enter[1]);

        switch (enter[0]) {
            case ("/register"):
                if (enter[2].length() < 8) {
                    output.writeUTF("The password is too short");
                    getNickname();
                }else if(logg.containsKey(enter[1])){
                    output.writeUTF("This name already engaged");
                    getNickname();
                } else {
                    logg.put(enter[1], socket);
                    passwordmap.put(enter[1], enter[2].hashCode());
                    this.clientName = enter[1];
                    output.writeUTF("you are registered successfully");
                }
                break;
            case ("/auth"):
                output.writeUTF(enter[1]);
                output.writeUTF(enter[2]);

                if (logg.containsKey(enter[1])) {
                    if(passwordmap.get(enter[1]).equals(enter[2].hashCode())){

                        output.writeUTF("you are registered successfully");
                    }else {
                        output.writeUTF("wrong password. try again");
                        getNickname();
                    }
                } else {
                    output.writeUTF("you are not in the chat!");
                    getNickname();
                }

                    break;
            default:
                        output.writeUTF("try again");
                        getNickname();
                }

        }


    private synchronized void AddAndSend(String clientName, String dialog, String mes) throws IOException {

        filename = Sorted(clientName+dialog)+".txt";

        FileWriter writer = new FileWriter(datamessage_acsess.get(filename), true);

        DataOutputStream output = new DataOutputStream(logg.get(dialog).getOutputStream());
        DataOutputStream output2 = new DataOutputStream(socket.getOutputStream());

        if(mes.equals("/exit")){
            writer.close();
            output.writeUTF(clientName + " exit chat");
            output2.writeUTF("you leave chat");
        }
else {
            writer.write(clientName + ": "+ mes);
            writer.append('\n');
            writer.flush();

            output.writeUTF(clientName + ": "+ mes);

            output2.writeUTF(clientName + ": " + mes);

        }


        }
    private synchronized void SendHistoryChat(Socket reconnect, String mes) throws IOException {
        DataOutputStream output = new DataOutputStream(reconnect.getOutputStream());
            output.writeUTF(mes);
}
    }




