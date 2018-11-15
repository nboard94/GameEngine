package core;

import events.Event;
import events.EventArg;
import events.EventManager;
import networking.Client;
import objects.components.Displayable;
import objects.components.EventDriven;
import objects.components.Movable;
import objects.objects.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import time.LocalTime;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class GameEngine extends PApplet implements EventDriven, Serializable {

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

    public static _GameObject getByUUID(String id) {
        for(_GameObject g : GameEngine.getSpace()) {
            if (g.getUUID().toString().equals(id) ) return g;
        }
        return null;
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

        EventManager.registerEvent("PausePlayGame", this);

        ArrayList<_GameObject> positions = new ArrayList<>();
        for( Object o : GameEngine.getSpace() ) {

            Movable m = (Movable) o;
        }

        // Initialize subsystems
        (new Thread(ReplayManager.getInstance())).start();
        (new Thread(EventManager.getInstance())).start();
        //(new Thread(Server.getInstance())).start();
        (new Thread(Client.getInstance())).start();
        (new Thread(Console.getInstance())).start();

        _GameObject.setApp(this);

        System.out.println("This: " + this.toString());
        System.out.println("Instance: " + GameEngine.getInstance().toString());
        this.createPrimaryGraphics();

        space.add(new PlatformStatic(0, 500, 200, 100, 255, 0, 0));
        space.add(new PlatformStatic(250, 500, 200, 100, 0, 255, 0));
        space.add(new PlatformStatic(500, 500, 200, 100, 0, 0, 255));
        space.add(new PlatformMoving(600, 300, 100, 25, 132, 62, 11, 100, 100, false, true, 5, 5));
        space.add(new PlatformMoving(350, 200, 100, 25, 132, 62, 11, 100, 100, true, false, -5, 5));
        space.add(new PlatformMoving(150, 100, 100, 25, 132, 62, 11, 100, 50, true, true, 5, 1));

        SpawnPoint playerSpawn = new SpawnPoint(new PlayerCharacter());
        space.add(playerSpawn);
        playerSpawn.spawn();

        space.add(new DeathZone(playerSpawn, 200, 575, 50, 100, 237, 181 ,0));
        space.add(new DeathZone(playerSpawn, 450, 575, 50, 100, 237, 181 ,0));
    }

    public void pauseGame() {
        if(GameEngine.time.pause()) noLoop();
        else if(GameEngine.time.play()) loop();
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
        else if(ReplayManager.isPlaying()) {
            fill(0, 255,0);
            triangle(width-30, 10, width-30, 30, width-10, 20);
        }

        // Send all objects to the server
//        for(_GameObject o : space) {
//            o.setApp(null);
//            Client.sendOutput(o);
//            o.setApp(this);
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

        if(GameEngine.time.isPaused() && keyCode==80) {
            notifyOfInput();
            keysPressed.replace(keyCode, false);
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

        if(GameEngine.time.isPaused() && keyCode==80) {
            notifyOfInput();
            keysPressed.replace(keyCode, false);
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

        // '1'
        if(keysPressed.get(49) != null && keysPressed.get(49)) {
            EventManager.raiseEvent("Replay", new EventArg("ReplaySpeed", 1));
        }

        // '2'
        if(keysPressed.get(50) != null && keysPressed.get(50)) {
            EventManager.raiseEvent("Replay", new EventArg("ReplaySpeed", 2));
        }

        // '3'
        if(keysPressed.get(51) != null && keysPressed.get(51)) {
            EventManager.raiseEvent("Replay", new EventArg("ReplaySpeed", 3));
        }

        // 'p'
        if(keysPressed.get(80) != null && keysPressed.get(80)) {
            EventManager.raiseEvent("PausePlayGame");
        }

        // 'r'
        if(keysPressed.get(82) != null && keysPressed.get(82)) {
            EventManager.raiseEvent("RecordStart");
        }

        // 't'
        if(keysPressed.get(84) != null && keysPressed.get(84)) {
            EventManager.raiseEvent("RecordStop");
        }
    }

    @Override
    public void onEvent(Event e) {
        switch (e.getEventType()) {
            case "PausePlayGame":
                pauseGame();
                break;
        }
    }
}
