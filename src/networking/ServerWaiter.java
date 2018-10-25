package networking;

import java.io.*;
import java.net.Socket;

class ServerWaiter implements Runnable {

    private final Socket s;
    private final ObjectInputStream dis;
    private final ObjectOutputStream dos;

    ServerWaiter(Socket s, ObjectInputStream dis, ObjectOutputStream dos) {

        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {

        synchronized(Server.getClients()) {

            Server.getClients().add(this);
            System.out.println("Client Connected: " + s);
            System.out.println("Current Amt of Clients: " + Server.getClients().size());
        }

        Thread inputHandler = new Thread(ServerInput.getInstance());
        inputHandler.start();

        Thread outputHandler = new Thread(ServerOutput.getInstance());
        outputHandler.start();

        try {
            inputHandler.join();
            outputHandler.join();

            dis.close();
            dos.close();
        } catch (Exception e) {
            return;
        }

        synchronized(Server.getClients())
        {
            Server.getClients().remove(this);
            System.out.println("Client Disconnected: " + s);
            System.out.println("Current Amt of Clients: " + Server.getClients().size());
        }
    }
}
