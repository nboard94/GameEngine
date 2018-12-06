package networking;

import events.Event;

import java.io.ObjectInputStream;

public class ServerInput implements Runnable {

    private static ServerInput instance;
    private static ObjectInputStream dis;

    private ServerInput() {
        dis=Server.getDIS();
    }

    public static ServerInput getInstance() {
        if(instance==null) instance = new ServerInput();
        return instance;
    }

    @Override
    public void run() {

        Event e;
        while(true) {
            try {
                e = (Event) dis.readObject();
                System.out.println("HERE" + e.toString());
                Server.getEventQueue().add(e);
            } catch (Exception e1) {
                //e1.printStackTrace();
            }
        }
    }
}
