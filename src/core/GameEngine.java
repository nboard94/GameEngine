package core;

import events.EventArg;
import events.EventManager;
import networking.Client;
import networking.Server;
import objects.components.Displayable;
import objects.objects.*;
import processing.core.PApplet;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameEngine extends PApplet implements Serializable {

    java.awt.Rectangle rr;

    public static int width = 700;
    public static int height = 600;
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

    public static String getGameEnginePath() {
        return "core.GameEngine";
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

        // Initialize subsystems
        Timeline time = Timeline.getInstance();
        (new Thread(EventManager.getInstance())).start();
        //(new Thread(Server.getInstance())).start();
        (new Thread(Client.getInstance())).start();
        (new Thread(Console.getInstance())).start();

        world.add(new PlatformStatic(this, 0, 500, 200, 100, 255, 0, 0));
        world.add(new PlatformStatic(this, 250, 500, 200, 100, 0, 255, 0));
        world.add(new PlatformStatic(this, 500, 500, 200, 100, 0, 0, 255));
        world.add(new PlatformMoving(this, 600, 300, 100, 25, 132, 62, 11, 100, 100, false, true, 5, 5));
        world.add(new PlatformMoving(this, 350, 200, 100, 25, 132, 62, 11, 100, 100, true, false, -5, 5));
        world.add(new PlatformMoving(this, 150, 100, 100, 25, 132, 62, 11, 100, 50, true, true, 5, 1));

        SpawnPoint playerSpawn = new SpawnPoint(new PlayerCharacter(this));
        world.add(playerSpawn);
        playerSpawn.spawn();

        world.add(new DeathZone(this, playerSpawn, 200, 575, 50, 100, 237, 181 ,0));
        world.add(new DeathZone(this, playerSpawn, 450, 575, 50, 100, 237, 181 ,0));


        Random rand = new Random();
        int rX = rand.nextInt(25)+101;
        int rY = rand.nextInt(25)+401;
        rr = new Rectangle(rX, rY, 25, 25);

    }

    public int loopCount = 0;
    public void draw() {

        notifyOfInput();
        background(93, 188, 210);
        Client.sendOutput(rr);
        ConcurrentLinkedQueue<Object> networkInput= Client.getInput();
        //System.out.println(Client.getInput());
        for(Object o : networkInput) {
            Rectangle r = (Rectangle)o;
            rect((float)r.getX(), (float)r.getY(), r.width, r.height);
        }

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

        // Send all objects to the server
//        for(_GameObject o : world) {
//            o.resetApp(null);
//            Client.sendOutput(o);
//            o.resetApp(this);
//        }

        loopCount++;
    }

    public static void main(String[] args) {
        PApplet.main("core.GameEngine");
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

    public void notifyOfInput() {

        if(keysPressed.get(this.RIGHT) != null && keysPressed.get(this.RIGHT)) {
            EventManager.raiseEvent("MoveRight");
        }

        if(keysPressed.get(this.LEFT) != null && keysPressed.get(this.LEFT)) {
            EventManager.raiseEvent("MoveLeft");
        }

        if(keysPressed.get(32) != null && keysPressed.get(32)) {
            EventManager.raiseEvent("Jump");
        }
    }
}
