package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientSide extends Thread {
    Scanner scanner;
    DataOutputStream output;
    DataInputStream input;

    ClientSide(Scanner in, DataOutputStream output, DataInputStream input){
        this.input = input;
        this.scanner = in;
        this.output = output;
    }
    @Override
    public void run(){
        while (true){
            String ans = null;
            try {
                ans = input.readUTF();
                System.out.println(ans);
            } catch (IOException e) {
                break;
            }


        }

    }
}
