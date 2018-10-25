package networking;

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

        while(true) {

            System.out.println(Server.getCollection().toString());

            synchronized (Server.getCollection()) {

                try {

                    for(Object s : Server.getCollection()) {

                        //dos.reset();
                        dos.writeObject(s);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
