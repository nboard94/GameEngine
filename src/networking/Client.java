package networking;

import events.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

public class Client implements Runnable {

    private static final int port = 15150;
    private static Client instance;
    private static final PriorityBlockingQueue<Event> eventQueue = new PriorityBlockingQueue<>();
    private static ObjectOutputStream dos;
    private static ObjectInputStream dis;


    private Client() {}

    public static Client getInstance() {
        if(instance==null) instance = new Client();
        return instance;
    }

    @Override
    public void run() {

        Socket s = null;
        try {
            InetAddress host = InetAddress.getByName("localhost");
            s = new Socket(host, port);

            dis = new ObjectInputStream(s.getInputStream());
            dos = new ObjectOutputStream(s.getOutputStream());

            Thread input = new Thread(ClientInput.getInstance());
            input.start();
            Thread output = new Thread(ClientOutput.getInstance());
            output.start();

        } catch(ConnectException e) {
            System.out.println("Could not find server to connect to.");

        } catch(Exception e) {
            System.out.println("Connection with the server has been lost.");
            e.printStackTrace();
        }

        try {
            assert s != null;
            s.close();
        } catch(IOException|NullPointerException e) {
            System.out.println("Could not close connection.");
        }
    }

    // returns server's dos
    static ObjectOutputStream getDOS() {
        return dos;
    }

    // returns server's dis
    static ObjectInputStream getDIS() {
        return dis;
    }

    static PriorityBlockingQueue<Event> getEventQueue() {
        return eventQueue;
    }

    public static void sendEvent(Event e) {
        eventQueue.add(e);
    }

    public static void main(String[] args) {
        Thread t = new Thread(new Client());
        t.start();
    }
}