package core;

import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public  class GameEngineOLD extends PApplet {

    // Game instance (singleton)
    private static GameEngineOLD game = null;
    // Game instance ID
    private static java.util.UUID gameID;
    // Bounding rectangle for the screen
    private Rectangle bounds;
    // List of walls
    private ArrayList<Rectangle> walls = new ArrayList<>();
    // Player rectangle
    private Rectangle r1;
    int speed = 5;

    // up down left right space
    private boolean[] keys = {false, true, false, false, false};

    public GameEngineOLD() {

    }

    public static GameEngineOLD getInstance() {
        if(GameEngineOLD.game == null) game = new GameEngineOLD();
        return game;
    }

    public static void main(String[] args) {
        PApplet.main("core.GameEngineOLD");

        // Assign UUID to this game instance
        gameID = java.util.UUID.randomUUID();
        System.out.print(gameID.toString());
    }

    // Game Settings
    public void settings() {
        // Set size of game screen
        size(500, 500);
    }

    // Initial Game Setup
    public void setup() {
        // Set bounding rectangle based off of game screen size
        bounds = new Rectangle(0, 0, width, height);
        // Create player
        r1 = new Rectangle(0, 450, 50, 50);

        // Add some walls
        walls.add(new Rectangle(250, 450, 100, 25));
        walls.add(new Rectangle(350, 400, 150, 25));
    }

    // Main game loop
    public void draw() {

        // Clear screen and draw background
        background(93, 188, 210);

        // Draw all walls
        fill(204, 102, 0);
        Rectangle cRec;
        for(int i = 0; i < walls.size(); i++) {
            cRec = walls.get(i);
            rect((float) cRec.getX(), (float) cRec.getY(), (float) cRec.getWidth(), (float) cRec.getHeight());
        }

        //Draw player and update
        fill(153);
        rect((float) r1.getX(), (float) r1.getY(), (float) r1.getWidth(), (float) r1.getHeight());
        moveRect();

    }

    // Handle a key being pressed
    public void keyPressed() {
        if(key==CODED) {
            if(keyCode==UP) keys[0] = true;
            else if(keyCode==DOWN) keys[1] = true;
            else if(keyCode==LEFT) keys[2] = true;
            else if(keyCode==RIGHT)  keys[3] = true;
        }
        else if(keyCode==' ') keys[4] = true;
    }

    // Handle a key being released
    public void keyReleased() {
        if(key==CODED) {
            if(keyCode==UP) keys[0] = false;
            else if(keyCode==DOWN) keys[1] = false;
            else if(keyCode==LEFT) keys[2] = false;
            else if(keyCode==RIGHT) keys[3] = false;
        }
        else if(keyCode==' ') keys[4] = false;
    }

    private void moveRect() {

        Rectangle fRec;
        int jump = 100;

        if((keys[0] || keys[4]) && grounded(r1)) {
            boolean move = true;
            fRec = (Rectangle)r1.clone();
            fRec.translate(0, -jump);
            if(!bounds.contains(fRec)) move = false;

            Rectangle cRec;
            for(int i = 0; i < walls.size(); i++) {
                cRec = walls.get(i);
                if(fRec.intersects(cRec)) move = false;
            }

            if(move) r1 = fRec;
        }
//        if(keys[1]) {
         if(true) {
            boolean move = true;
            fRec = (Rectangle)r1.clone();
            fRec.translate(0, +5);
            if(!bounds.contains(fRec)) move = false;

            Rectangle cRec;
            for(int i = 0; i < walls.size(); i++) {
                cRec = walls.get(i);
                if(fRec.intersects(cRec)) move = false;
            }

            if(move) r1 = fRec;
        }
        if(keys[2]) {
            boolean move = true;
            fRec = (Rectangle)r1.clone();
            fRec.translate(-speed, 0);
            if(!bounds.contains(fRec)) move = false;

            Rectangle cRec;
            for(int i = 0; i < walls.size(); i++) {
                cRec = walls.get(i);
                if(fRec.intersects(cRec)) move = false;
            }

            if(move) r1 = fRec;
        }
        if(keys[3]) {
            boolean move = true;
            fRec = (Rectangle)r1.clone();
            fRec.translate(+speed, 0);
            if(!bounds.contains(fRec)) move = false;

            Rectangle cRec;
            for(int i = 0; i < walls.size(); i++) {
                cRec = walls.get(i);
                if(fRec.intersects(cRec)) move = false;
            }

            if(move) r1 = fRec;
        }
    }

    private boolean grounded(Rectangle r) {

        boolean grounded = false;
        Rectangle fRec = (Rectangle)r.clone();
        fRec.translate(0, +speed);

        //Check if touching any wall blocks
        Rectangle cRec;
        for(int i = 0; i < walls.size(); i++) {
            cRec = walls.get(i);
            if(fRec.intersects(cRec)) grounded = true;
        }

        //Check if touching ground
        if(!bounds.contains(fRec)) grounded = true;
        return grounded;
    }
}
