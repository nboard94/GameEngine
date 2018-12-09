package core;

import events.Event;
import events.EventArg;
import events.EventManager;
import networking.Client;
import networking.Server;
import objects.components.Displayable;
import objects.components.EventDriven;
import objects.components.Movable;
import objects.objects.*;
import processing.core.PApplet;
import scripting.ScriptManager_JS;
import time.LocalTime;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameEngine extends PApplet implements EventDriven, Serializable {

    private double previous, lag, current, elapsed;
    public static int width = 500;
    public static int height = 500;
    private static GameEngine instance = new GameEngine();
    private static LocalTime time;
    private static List<_GameObject> space;
    public static int score = 0;
    public static int hiscore = 0;
    boolean dead = false;
    public static int iterations = 0;
    public static double wins = 1;
    public static int remaining = 24;

    public GameEngine() {
        time = new LocalTime(TimeUnit.MILLISECONDS);
        space = Collections.synchronizedList(new ArrayList<>());
    }

    public static GameEngine getInstance() {
        return instance;
    }

    public static List<_GameObject> getSpace() {
        return space;
    }

    // Tweak Processing settings
    public void settings() {
        size(width, height);
    }

    // Spin-off initial systems and construct
    public void setup() {

        EventManager.registerEvent("PausePlayGame", this);
        EventManager.registerEvent("InvaderWin", this);
        EventManager.registerEvent("NewGame", this);
        for( Object o : GameEngine.getSpace() ) {
            Movable m = (Movable) o;
        }

        // Initialize subsystems
        (new Thread(ReplayManager.getInstance())).start();
        (new Thread(EventManager.getInstance())).start();
        //(new Thread(Server.getInstance())).start();
        //(new Thread(Client.getInstance())).start();
        _GameObject.setApp(this);

        space.add(new Ship());
        spawnFleet();

        previous = time.getTime();
        lag = 0.0;
    }

    void pauseGame() {
        if(GameEngine.time.pause()) noLoop();
        else if(GameEngine.time.play()) loop();
    }

    void spawnFleet() {
        space.add(new Invader(50, 50));
        space.add(new Invader(100, 50));
        space.add(new Invader(150, 50));
        space.add(new Invader(200, 50));
        space.add(new Invader(250, 50));
        space.add(new Invader(300, 50));
        space.add(new Invader(350, 50));
        space.add(new Invader(400, 50));
        space.add(new Invader(70, 80));
        space.add(new Invader(120, 80));
        space.add(new Invader(170, 80));
        space.add(new Invader(220, 80));
        space.add(new Invader(270, 80));
        space.add(new Invader(320, 80));
        space.add(new Invader(370, 80));
        space.add(new Invader(420, 80));
        space.add(new Invader(50, 110));
        space.add(new Invader(100, 110));
        space.add(new Invader(150, 110));
        space.add(new Invader(200, 110));
        space.add(new Invader(250, 110));
        space.add(new Invader(300, 110));
        space.add(new Invader(350, 110));
        space.add(new Invader(400, 110));
    }

    void newGame() {
        if(score > hiscore) hiscore = score;
        score = 0;
        wins=0;
        dead = false;
        for(_GameObject g : space) {
            ScriptManager_JS.loadScript("src\\scripting\\scripts\\reset_object.js");
            ScriptManager_JS.bindArgument("o", g);
            ScriptManager_JS.executeScript();

//            if(g.getClass() == Ship.class) {
//                ((Ship)g).reset();
//            } else if(g.getClass() == Invader.class) {
//                ((Invader)g).reset();
//            }
        }
        loop();
    }

    public void draw() {

        if(remaining==0) {
            wins++;
            for(_GameObject g : space) {
                if(g.getClass() == Invader.class) {
                    ScriptManager_JS.loadScript("src\\scripting\\scripts\\reset_object.js");
                    ScriptManager_JS.bindArgument("o", g);
                    ScriptManager_JS.executeScript();
                }
            }
            remaining = 24;
        }

        // used to control throttling
        current = time.getTime();
        elapsed = current - previous;
        previous = current;
        lag += elapsed;

        notifyOfInput();

        while(lag >= 50) {
            // Update all objects
            synchronized (space) {

                for(_GameObject o : space) {
                    o.update();
                }

            }

            background(93, 188, 210);
            fill(0, 0, 0);
            textSize(26);
            text("Score: " + score, 10, 32);
            text("Hi-Score: " + hiscore, 250, 32);
            if(dead) text("Game Over: Press Enter to Restart", 10, 65);
            // Display all displayable objects
            for (_GameObject o : space) {
                try {
                    Displayable d = (Displayable) o;
                    d.display();
                } catch (ClassCastException e) {
                    // This block intentionally left empty
                }
            }

            lag -= 50;
        }



        if(ReplayManager.isRecording()) {
            fill(255, 0 ,0);
            ellipse(width-25, 25, 25, 25);
        }
        else if(ReplayManager.isPlaying()) {
            fill(0, 255,0);
            triangle(width-30, 10, width-30, 30, width-10, 20);
        }

        iterations++;
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

    private void notifyOfInput() {

        // 'up'
        if(keysPressed.get(this.UP) != null && keysPressed.get(this.UP)) {
            EventManager.raiseEvent("MoveUp");
        }

        // 'down'
        if(keysPressed.get(this.DOWN) != null && keysPressed.get(this.DOWN)) {
            EventManager.raiseEvent("MoveDown");
        }

        // 'left'
        if(keysPressed.get(this.LEFT) != null && keysPressed.get(this.LEFT)) {
            EventManager.raiseEvent("MoveLeft");
        }

        // 'right'
        if(keysPressed.get(this.RIGHT) != null && keysPressed.get(this.RIGHT)) {
            EventManager.raiseEvent("MoveRight");
        }

        // 'enter'
        if(keysPressed.get(10) != null && keysPressed.get(10)) {
            EventManager.raiseEvent("NewGame");
        }

        // 'space'
        if(keysPressed.get(32) != null && keysPressed.get(32)) {
            EventManager.raiseEvent("Shoot");
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
            EventManager.raiseEvent("Replay", new EventArg("ReplaySpeed", 10));
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
            case "InvaderWin":
                dead = true;
                for(_GameObject g : space) {
                    if(g.getClass() == Ship.class) {
                        ((Ship)g).setSpeed(0);
                    } else if(g.getClass() == Invader.class) {
                        ((Invader)g).setSpeed(0);
                    }
                }
                break;
            case "NewGame":
                if(dead)newGame();
                break;
        }
    }
}
