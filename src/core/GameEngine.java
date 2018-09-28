package core;

import processing.core.PApplet;

import java.awt.*;

public class GameEngine extends PApplet {

    // Game instance ID
    private static java.util.UUID gameID;

    private Rectangle bounds;
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
    }

    public void draw() {
        background(151);
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

        Rectangle fRec = (Rectangle) r1.clone();
        int speed = 5;

        if(keys[0]) {
            fRec.translate(0, -speed);
        }
        if(keys[1]) {
            fRec.translate(0, +speed);
        }
        if(keys[2]) {
            fRec.translate(-speed, 0);
        }
        if(keys[3]) {
            fRec.translate(+speed, 0);
        }

        if(bounds.contains(fRec)) r1 = fRec;
    }

    private void gravity() {
        float g = 0;

    }
}
