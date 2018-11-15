package core;

import networking.Client;
import networking.Server;

import java.util.Scanner;

public class Console implements Runnable{

    private static Console instance;
    private static final Server server = Server.getInstance();
    private static final Client client = Client.getInstance();

    private Console() {}

    public static Console getInstance() {
        if(instance==null) instance = new Console();
        return instance;
    }

    public void run() {

        Scanner scan = new Scanner(System.in);
        String command = "";

        System.out.println("Enter \"Help\" to see available commands.");
        while(!command.equals("Exit")) {

            command = scan.nextLine().toLowerCase();

            if(command.equals("help")) {
                System.out.println("Available commands: \n\"Help\"\n\"server count\"");
            }
            else if(command.equals("server count")) {
                System.out.println("Number of clients connected: " + Server.getClients().size());
            }
        }
    }
}
