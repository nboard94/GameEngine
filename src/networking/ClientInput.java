package networking;

import events.Event;
import events.EventManager;

import java.io.ObjectInputStream;

public class ClientInput implements Runnable {

    private static ClientInput instance;
    private static ObjectInputStream dis;

    private ClientInput() {
        dis=Client.getDIS();
    }

    public static ClientInput getInstance() {
        if(instance==null) instance = new ClientInput();
        return instance;
    }

    @Override
    public void run() {

        Event e;
        while(true) {
            try {
                e = (Event) dis.readObject();
                EventManager.raiseEvent(e);
            } catch (Exception e1) {
                //e1.printStackTrace();
            }
        }
    }
}
