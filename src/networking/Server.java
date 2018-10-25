package networking;

import processing.core.PApplet;

import java.io.*;
import java.util.*;
import java.net.*;

public class Server extends PApplet implements Runnable {

    private static final int port = 15150;
    private static final Server instance = new Server();
    private static final List<ServerWaiter> clients = Collections.synchronizedList(new ArrayList<>());
    private static final List<Object> collection = Collections.synchronizedList(new ArrayList<>());
    private static ObjectOutputStream dos;
    private static ObjectInputStream dis;

    // singleton constructor
    private Server() {}

    // listen for incoming connections and spin off a new thread to handle each client
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);

            //noinspection InfiniteLoopStatement
            while(true) {

                Socket s = ss.accept();
                dos = new ObjectOutputStream(s.getOutputStream());
                dis = new ObjectInputStream(s.getInputStream());

                Thread t = new Thread(new ServerWaiter(s, dis, dos));
                t.start();
            }
        } catch(BindException e) {
            System.out.println("Another server is already running...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // returns singleton instance of server
    public static Server getInstance() {
        return instance;
    }

    // returns server's objectoutputstream
    public static ObjectOutputStream getDOS() {
        return dos;
    }

    // returns server's objectinputstream
    public static ObjectInputStream getDIS() {
        return dis;
    }

    // returns list of clients
    public static List<ServerWaiter> getClients()  {
        return clients;
    }

    // return list of collected objects
    static List<Object> getCollection() {
        return collection;
    }

    // main to run server individually
    public static void main(String[] args) {
        Server.getInstance().run();
    }
}

