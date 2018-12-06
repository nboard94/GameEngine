package networking;

import events.Event;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientOutput implements Runnable {

    private static ClientOutput instance;
    private static ObjectOutputStream dos;

    private ClientOutput() {
        dos = Client.getDOS();
    }

    public static ClientOutput getInstance() {
        if(instance==null) instance = new ClientOutput();
        return instance;
    }

    @Override
    public void run() {

        Event e;
        while(true) {
            try {
                e = Client.getEventQueue().take();
                dos.writeObject(e);
            } catch (InterruptedException | IOException e1) {
               // e1.printStackTrace();
            }
        }
    }
}
