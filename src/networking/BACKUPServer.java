package networking;

import objects.objects._GameObject;
import processing.core.PApplet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BACKUPServer extends PApplet implements Runnable
{
    private static final BACKUPServer instance = new BACKUPServer();
    private static final List<BACKUPServerWaiter> clients = Collections.synchronizedList(new ArrayList<>());
    private static final int port = 15150;
    private static final ConcurrentLinkedQueue<_GameObject> inputCollection = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<_GameObject> outputCollection = new ConcurrentLinkedQueue<>();

    /*
    *   singleton private constructor
    */
    private BACKUPServer() {
    }

    /*
     *   Run the server independent from the game.
     */
    public static void main(String[] args)
    {
        BACKUPServer.getInstance().run();
    }

    /*
     *   Creates the server socket to accept incoming requests.
     *   Create threads for incoming clients.
     */
    @Override
    public void run()
    {
        try {
            ServerSocket ss = new ServerSocket(port);
            //System.out.println("The server has started, accepting clients...");

            //noinspection InfiniteLoopStatement
            while(true) {

                Socket s = ss.accept();

                ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream dis = new ObjectInputStream(s.getInputStream());

                Thread t = new Thread(new BACKUPServerWaiter(s, dis, dos));
                t.start();

            }
        } catch(BindException e) {
            // Server on this port is presumed to already be running
            //System.out.println("Another server is already running...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    *   Returns the singleton instance of the server.
    */
    public static BACKUPServer getInstance()
    {
        return instance;
    }

    /*
     *   Returns the list of clients.
     */
    public static List<BACKUPServerWaiter> getClients()
    {
        return clients;
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
}

