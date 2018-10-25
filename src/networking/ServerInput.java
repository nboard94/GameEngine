package networking;

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

        Object o;
        while(true) {
            try {
                o = dis.readObject();

                synchronized (Server.getCollection()) {
                    Server.getCollection().remove(o);
                    Server.getCollection().add(o);
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
