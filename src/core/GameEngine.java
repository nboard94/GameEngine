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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        EventManager.registerEvent("EatFood", this);
        EventManager.registerEvent("NewGame", this);
        for( Object o : GameEngine.getSpace() ) {
            Movable m = (Movable) o;
        }

        // Initialize subsystems
        (new Thread(ReplayManager.getInstance())).start();
        (new Thread(EventManager.getInstance())).start();
        (new Thread(Server.getInstance())).start();
        (new Thread(Client.getInstance())).start();
        _GameObject.setApp(this);

        space.add(new SnakeHead());
        space.add(new Food(20, 20));

        previous = time.getTime();
        lag = 0.0;
    }

    void pauseGame() {
        if(GameEngine.time.pause()) noLoop();
        else if(GameEngine.time.play()) loop();
    }

    public void newGame() {
        if(score > hiscore) hiscore = score;
        score = 0;
        for(_GameObject g : space) {
            if(g.getClass() == SnakeHead.class) {
                ScriptManager_JS.loadScript("src\\scripting\\scripts\\reset_object.js");
                ScriptManager_JS.bindArgument("o", g);
                ScriptManager_JS.executeScript();
            }
        }
    }


    public void draw() {

        // used to control throttling
        current = time.getTime();
        elapsed = current - previous;
        previous = current;
        lag += elapsed;

        notifyOfInput();

        while(lag >= 50) {
            // Update all objects
            for(_GameObject o : space) {
                o.update();
            }

            background(93, 188, 210);
            fill(0, 0, 0);
            textSize(26);
            text("Score: " + score, 10, 32);
            text("Hi-Score: " + hiscore, 250, 32);
            if(SnakeHead.dead) text("Game Over: Press Enter to Restart", 10, 65);
            // Display all displayable objects
            for(_GameObject o : space) {
                try {
                    Displayable d = (Displayable) o;
                    d.display();
                } catch( ClassCastException e) {
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
            EventManager.raiseEvent("EatFood");
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
            case "EatFood":
                score++;
                break;
            case "NewGame":
                newGame();
                System.out.println("HERE:");
                break;
        }
    }
}
