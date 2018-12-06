package networking;

import events.Event;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerOutput implements Runnable {

    private static ServerOutput instance;
    private static ObjectOutputStream dos;

    private ServerOutput() {
        dos = Server.getDOS();
    }

    public static ServerOutput getInstance() {
        if(instance==null) instance = new ServerOutput();
        return instance;
    }

    @Override
    public void run() {

        Event e;
        while(true) {
            try {
                e = Server.getEventQueue().take();
                dos.writeObject(e);
            } catch (InterruptedException | IOException e1) {
                //e1.printStackTrace();
            }
        }
    }

}
