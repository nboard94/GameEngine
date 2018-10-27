package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class BACKUPServerWaiter implements Runnable
{
    private final Socket s;
    private final ObjectInputStream dis;
    private final ObjectOutputStream dos;

    BACKUPServerWaiter(Socket s, ObjectInputStream dis, ObjectOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run()
    {
        synchronized(BACKUPServer.getClients())
        {
            BACKUPServer.getClients().add(this);
            System.out.println("Client Connected: " + s);
            System.out.println("Current Amt of Clients: " + BACKUPServer.getClients().size());
        }

        while(true)
        {
            try {

            } catch(Exception e) {
                e.printStackTrace();
                break;
            }


//            try {
//                Integer i1 = (Integer)dis.readObject();
//                dos.writeObject(i1 + 1);
//            } catch(Exception e) {
//                break;
//            }
        }

        try {
            dis.close();
            dos.close();
        } catch (IOException e) {
            return;
        }

        synchronized(BACKUPServer.getClients())
        {
            BACKUPServer.getClients().remove(this);
            System.out.println("Client Disconnected: " + s);
            System.out.println("Current Amt of Clients: " + BACKUPServer.getClients().size());
        }
    }
}
