package core;

import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public class GameEngine extends PApplet {

    // Game instance ID
    private static java.util.UUID gameID;

    private Rectangle bounds;
    private ArrayList<Rectangle> walls = new ArrayList<Rectangle>();
    private Rectangle r1;

    // up down left right
    private boolean[] keys = {false, false, false, false};

    public static void main(String[] args) {
        PApplet.main("core.GameEngine");

        // Assign UUID to this game instance
        gameID = java.util.UUID.randomUUID();
        System.out.print(gameID.toString());
    }

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        bounds = new Rectangle(0, 0, width, height);
        r1 = new Rectangle(0, 450, 50, 50);

        walls.add(new Rectangle(250, 450, 100, 25));
        walls.add(new Rectangle(150, 250, 100, 25));
    }

    public void draw() {
        background(151);

        Rectangle cRec;
        for(int i = 0; i < walls.size(); i++) {
            cRec = walls.get(i);
            rect((float) cRec.getX(), (float) cRec.getY(), (float) cRec.getWidth(), (float) cRec.getHeight());
        }

        rect((float) r1.getX(), (float) r1.getY(), (float) r1.getWidth(), (float) r1.getHeight());
        moveRect();
    }

    public void keyPressed() {
        if(key==CODED) {
            if(keyCode==UP) {
                keys[0] = true;
            }
            else if(keyCode==DOWN) {
                keys[1] = true;
            }
            else if(keyCode==LEFT) {
                keys[2] = true;
            }
            else if(keyCode==RIGHT) {
                keys[3] = true;
            }
        }
    }

    public void keyReleased() {
        if(key==CODED) {
            if(keyCode==UP) {
                keys[0] = false;
            }
            else if(keyCode==DOWN) {
                keys[1] = false;
            }
            else if(keyCode==LEFT) {
                keys[2] = false;
            }
            else if(keyCode==RIGHT) {
                keys[3] = false;
            }
        }
        else if(keyCode==' ') {
            gravity();
        }
    }

    private void moveRect() {

        Rectangle fRec;
        int speed = 5;

        if(keys[0]) {
            boolean move = true;
            fRec = (Rectangle)r1.clone();
            fRec.translate(0, -speed);
            if(!bounds.contains(fRec)) move = false;

            Rectangle cRec;
            for(int i = 0; i < walls.size(); i++) {
                cRec = walls.get(i);
                if(fRec.intersects(cRec)) move = false;
            }

            if(move) r1 = fRec;
        }
        if(keys[1]) {
            boolean move = true;
            fRec = (Rectangle)r1.clone();
            fRec.translate(0, +speed);
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

    private void gravity() {
        float g = 0;

    }
}
