package chat;


import java.util.Scanner;

public class Helper {
    private static Scanner scanner = new Scanner(System.in);

    public static void write(String msg) {
        System.out.println(msg);
    }


    public static String read() {
        return scanner.nextLine();
    }

    public static String getAddress() {
        return "127.0.0.1";
    }

    public static int getPort() {
        return 23456;
    }
}
