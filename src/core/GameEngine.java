package core;

import networking.Client;
import networking.Server;
import objects.components.Displayable;
import objects.objects.DeathZone;
import objects.objects.Rectangle;
import objects.objects._GameObject;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;

public class GameEngine extends PApplet {

    public static int width = 500;
    public static int height = 500;
    public static double gravity = 0.6;

    private static GameEngine instance;
    private static ArrayList<_GameObject> world;

    public GameEngine() {
        world = new ArrayList<>();
    }

    public static GameEngine getInstance() {
        if(instance==null) instance = new GameEngine();
        return instance;
    }

    public static ArrayList<_GameObject> getWorld() {
        return world;
    }

    public static double getGravity() {
        return gravity;
    }

    // Tweak Processing settings
    public void settings() {
        size(width, height);
    }

    // Spin-off initial systems and construct
    public void setup() {

        // Start the interactive console
        //Thread console = new Thread(Console.getInstance());
        //console.start();

        // Start a server if one isn't already available
        Thread server = new Thread(Server.getInstance());
        server.start();

        // Start a client and connect to server
        Thread client = new Thread(Client.getInstance());
        client.start();

        world.add(new Rectangle(this));
        world.add(new DeathZone());
    }

    public void draw() {
        //System.out.println(keysPressed.toString());

        // Update all objects
        for(_GameObject o : world) {
            o.update();
        }

        // Display all displayable objects
        for(_GameObject o : world) {

            try {
                Displayable d = (Displayable) o;
                d.display();
            } catch( ClassCastException e) {
                // This block intentionally left empty
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(GameEngine.getInstance().getClass());
    }

    /////////////////////////////////////////////////////////////////////////
    // Handle Input/Output
    /////////////////////////////////////////////////////////////////////////

    // Contains keyCode/boolean values to represent buttons pressed
    private static HashMap<Integer, Boolean> keysPressed = new HashMap<>();

    // Updates keysPressed values as true depending on button pressed
    public void keyPressed() {
        if(!keysPressed.containsKey(keyCode)) {
            keysPressed.put(keyCode, true);
        }
        else if(keysPressed.containsKey(keyCode)) {
            keysPressed.replace(keyCode, true);
        }
    }

    // Updates keysPressed values as false depending on button released
    public void keyReleased() {
        if(!keysPressed.containsKey(keyCode)) {
            keysPressed.put(keyCode, false);
        }
        else if(keysPressed.containsKey(keyCode)) {
            keysPressed.replace(keyCode, false);
        }
    }

    // Get the static keysPressed field
    public static HashMap<Integer, Boolean> getPressedKeys() {
        return keysPressed;
    }
}
