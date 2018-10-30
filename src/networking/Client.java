package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client implements Runnable {

    private static Client instance;
    private static final ConcurrentLinkedQueue<Object> inputCollection = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Object> outputCollection = new ConcurrentLinkedQueue<>();

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
            s = new Socket(host, 15150);

            ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
            Random rand = new Random();

            //noinspection InfiniteLoopStatement
            while (true) {

                // Write from OutputCollection to server
                synchronized (Client.outputCollection) {
                    dos.reset();
                    if(outputCollection.peek() != null) dos.writeObject(outputCollection.poll());
                }

                // Receive from InputCollection to server
                synchronized (Client.inputCollection) {
                    Object o = dis.readObject();
                    Client.inputCollection.add(o);
                    //System.out.println(Client.inputCollection);
                }
            }
        } catch(ConnectException e) {
            System.out.println("Could not find server to connect to.");

        } catch(Exception e) {
            System.out.println("Connection with the server has been lost.");
            e.printStackTrace();
        }

        try {
            s.close();
        } catch(IOException|NullPointerException e) {
            //System.out.println("Could not close connection.");
        }
    }

    // Add this object to the output collection
    public static void sendOutput(Object o) {
        outputCollection.remove(o);
        outputCollection.offer(o);
    }

    public static ConcurrentLinkedQueue<Object> getInput() {
        return inputCollection;
    }

    public static void main(String[] args) {
        Thread t = new Thread(new Client());
        t.start();
    }
}