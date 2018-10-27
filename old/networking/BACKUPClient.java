package networking;

import objects.objects._GameObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BACKUPClient implements Runnable
{

    private static BACKUPClient instance;
    private static final ConcurrentLinkedQueue<_GameObject> inputCollection = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<_GameObject> outputCollection = new ConcurrentLinkedQueue<>();

    private BACKUPClient() {}

    public static BACKUPClient getInstance() {
        if(instance==null) instance = new BACKUPClient();
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
                synchronized (BACKUPClient.outputCollection) {
                    dos.writeObject(BACKUPClient.outputCollection);
                }

                // Receive from InputCollection to server
                synchronized (BACKUPClient.inputCollection) {
                    BACKUPClient.inputCollection.add((_GameObject) dis.readObject());
                }



//                Integer i1 = rand.nextInt(1000) + 1;
//                dos.writeObject(i1);
//                //System.out.println("Sent: " + i1);
//                Integer i2 = (Integer) dis.readObject();
//                //System.out.println("Received: " + i2);
//                TimeUnit.SECONDS.sleep(5);
            }
        } catch(ConnectException e) {
            System.out.println("Could not find server to connect to.");

        } catch(Exception e) {
            System.out.println("Connection with the server has been lost.");
            e.printStackTrace();
        }

        try {
            s.close();
        } catch(IOException e) {
            System.out.println("Could not close connection.");
        }
    }

    // Flush and return the input collection
    public static ConcurrentLinkedQueue<_GameObject> getInput() {
        ConcurrentLinkedQueue<_GameObject> temp = new ConcurrentLinkedQueue<>(inputCollection);
        inputCollection.clear();
        return temp;
    }

    // Add this object to the output collection
    public static void sendOutput(_GameObject o) {
        outputCollection.add(o);
    }

    public static void main(String[] args) {
        Thread t = new Thread(new BACKUPClient());
        t.start();
    }
}