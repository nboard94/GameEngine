package core;

import events.EventManager;
import networking.Client;
import objects.components.Displayable;
import objects.objects.*;
import processing.core.PApplet;
import time.LocalTime;
import time.Timeline;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class GameEngine extends PApplet implements Serializable {

    java.awt.Rectangle rr;

    public static int width = 700;
    public static int height = 600;
    public static double gravity = 0.6;

    private static GameEngine instance = new GameEngine();
    private static LocalTime time;
    private static ArrayList<_GameObject> space;

    public GameEngine() {
        time = new LocalTime(TimeUnit.MILLISECONDS);
        space = new ArrayList<>();
    }

    public static GameEngine getInstance() {
        return instance;
    }

    public static LocalTime getTime() {
        return GameEngine.time;
    }

    public static void setTime(LocalTime time) {
        GameEngine.time = time;
    }

    public static ArrayList<_GameObject> getSpace() {
        return space;
    }
    public static void setSpace(ArrayList<_GameObject> space) {
        GameEngine.space = space;
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
        (new Thread(ReplayManager.getInstance())).start();
        (new Thread(EventManager.getInstance())).start();
        //(new Thread(Server.getInstance())).start();
        (new Thread(Client.getInstance())).start();
        (new Thread(Console.getInstance())).start();

        space.add(new PlatformStatic(this, 0, 500, 200, 100, 255, 0, 0));
        space.add(new PlatformStatic(this, 250, 500, 200, 100, 0, 255, 0));
        space.add(new PlatformStatic(this, 500, 500, 200, 100, 0, 0, 255));
        space.add(new PlatformMoving(this, 600, 300, 100, 25, 132, 62, 11, 100, 100, false, true, 5, 5));
        space.add(new PlatformMoving(this, 350, 200, 100, 25, 132, 62, 11, 100, 100, true, false, -5, 5));
        space.add(new PlatformMoving(this, 150, 100, 100, 25, 132, 62, 11, 100, 50, true, true, 5, 1));

        SpawnPoint playerSpawn = new SpawnPoint(new PlayerCharacter(this));
        space.add(playerSpawn);
        playerSpawn.spawn();

        space.add(new DeathZone(this, playerSpawn, 200, 575, 50, 100, 237, 181 ,0));
        space.add(new DeathZone(this, playerSpawn, 450, 575, 50, 100, 237, 181 ,0));
    }

    public int loopCount = 0;
    public void draw() {

        notifyOfInput();
        background(93, 188, 210);
        ConcurrentLinkedQueue<Object> networkInput= Client.getInput();
        //System.out.println(Client.getInput());
        for(Object o : networkInput) {
            Rectangle r = (Rectangle)o;
            rect((float)r.getX(), (float)r.getY(), r.width, r.height);
        }

        //System.out.println(keysPressed.toString());

        // Update all objects
        for(_GameObject o : space) {
            o.update();
        }

        // Display all displayable objects
        for(_GameObject o : space) {

            try {
                Displayable d = (Displayable) o;
                d.display();
            } catch( ClassCastException e) {
                // This block intentionally left empty
            }
        }

        if(ReplayManager.isRecording()) {
            fill(255, 0 ,0);
            ellipse(width-25, 25, 25, 25);
        }

        // Send all objects to the server
//        for(_GameObject o : space) {
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
        //System.out.println(keyCode);
        if(!keysPressed.containsKey(keyCode)) {
            keysPressed.put(keyCode, true);
        }
        else if(keysPressed.containsKey(keyCode)) {
            keysPressed.replace(keyCode, true);
        }
    }

    // Updates keysPressed values as false depending on button released
    public void keyReleased() {
        //System.out.println(keyCode);
        if(!keysPressed.containsKey(keyCode)) {
            keysPressed.put(keyCode, false);
        }
        else if(keysPressed.containsKey(keyCode)) {
            keysPressed.replace(keyCode, false);
        }

        if(keyCode==80) {
            EventManager.raiseEvent("Replay");
        }
    }

    // Get the static keysPressed field
    public static HashMap<Integer, Boolean> getPressedKeys() {
        return keysPressed;
    }

    public void notifyOfInput() {

        // 'right'
        if(keysPressed.get(this.RIGHT) != null && keysPressed.get(this.RIGHT)) {
            EventManager.raiseEvent("MoveRight");
        }

        // 'left'
        if(keysPressed.get(this.LEFT) != null && keysPressed.get(this.LEFT)) {
            EventManager.raiseEvent("MoveLeft");
        }

        // 'space'
        if(keysPressed.get(32) != null && keysPressed.get(32)) {
            EventManager.raiseEvent("Jump");
        }

        // 'p'
//        if(keysPressed.get(80) != null && keysPressed.get(80)) {
//            EventManager.raiseEvent("Replay");
//        }

        // 'r'
        if(keysPressed.get(82) != null && keysPressed.get(82)) {
            EventManager.raiseEvent("RecordStart");
        }

        // 't'
        if(keysPressed.get(84) != null && keysPressed.get(84)) {
            EventManager.raiseEvent("RecordStop");
        }
    }
}
