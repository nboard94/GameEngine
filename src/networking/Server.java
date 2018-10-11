// Resources used:
// The provided client/server example
// https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
package networking;

import processing.core.PApplet;

import java.io.*;
import java.util.*;
import java.net.*;

// Server class 
public class Server extends PApplet
{
    // The only instance of server
    private static Server instance = null;
    // List of client threads
    static List<Thread> clients = null;
    // Port to listen on
    static final int port = 1510;

    // private constructor for singleton
    public Server() {
        clients = Collections.synchronizedList(new ArrayList<>());
    }

    // returns the singleton server instance
    public static Server getInstance() {
        if(instance==null) instance = new Server();
        return instance;
    }

    public static void main(String[] args)
    {
        // maintain list of all threads handing clients.
        ArrayList<Thread> clients = new ArrayList<>();
        // server is listening on port 5056
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // running infinite loop for getting
        // client request
        while (true)
        {
            Thread listener = new ServerClientListener(ss);
            listener.run();

        }
    }

}

