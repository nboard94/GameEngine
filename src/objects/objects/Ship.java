package objects.objects;

import core.GameEngine;
import events.Event;
import events.EventManager;
import objects.components.*;
import processing.core.PApplet;
import processing.core.PConstants;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Ship extends _GameObject implements Bounded, Collidable, Controllable, Displayable, EventDriven, Movable {

    private PApplet app = _GameObject.getApp();
    public static int x, y;
    private int w, h;
    private int r, g, b;
    private int speedX, speedY;
    private int previousShot = 0;


    public Ship() {
        EventManager.registerEvent("MoveLeft", this);
        EventManager.registerEvent("MoveRight", this);
        EventManager.registerEvent("Shoot", this);
        this.x = app.width/2;
        this.y = app.height-50;
        this.w = 20;
        this.h = 20;
        this.r = 0;
        this.g = 255;
        this.b = 0;
        this.speedX = 10;
        this.speedY = 0;
    }

    public void reset() {
        this.x = app.width/2;
        this.y = app.height-50;
        this.w = 20;
        this.h = 20;
        this.r = 0;
        this.g = 255;
        this.b = 0;
        this.speedX = 10;
        this.speedY = 0;
    }

    public void setSpeed(int speed) {
        speedX = speed;
        speedY = speed;
    }

    @Override
    public void bound() {
        if(x<=(w/2)) {
            x = (w/2);
        }
        else if (x>=(GameEngine.width-w/2)) {
            x = (GameEngine.width-w/2);
        }

        if(y<=0) {
            y = (0);
        }
        else if (y>=(GameEngine.height-h)) {
            y = (GameEngine.height-(h));
        }
    }

    @Override
    public boolean detectCollision() {

        Rectangle r1 = new Rectangle(x, y, w, h);
        Rectangle r2;

        for( _GameObject o : GameEngine.getSpace()) {
            try {
                if(o != this) {
                    Collidable c = (Collidable) o;
                    int[] rdata = c.getRectangleData();
                    r2 = new Rectangle(rdata[0], rdata[1], rdata[2], rdata[3]);
                    if(r1.intersects(r2)) {
                        try {
                            Invader f = (Invader) c;
                            EventManager.raiseEvent("InvaderWin");
                        } catch (ClassCastException e1) {
                            // This block intentionally left empty
                        }
                        return true;
                    }
                }
            } catch(ClassCastException e) {
                // This block intentionally left empty
            }
        }

        return false;
    }

    @Override
    public int[] getRectangleData() {
        return new int[]{x, y, w, h};
    }

    @Override
    public void moveRight() {
        x+=speedX;
    }

    @Override
    public void moveLeft() {
        x-=speedX;
    }

    @Override
    public void moveUp() {

    }

    @Override
    public void moveDown() {

    }

    public void stop() {
        speedX = 0;
        speedY = 0;
    }

    @Override
    public void display() {
        move();
        bound();
        app.fill(r, g, b);
        //app.rectMode(PConstants.CENTER);
        app.rect(x, y, w, h);

    }

    @Override
    public void move() {
        detectCollision();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    @Override
    public void replayPositionSave() {

    }

    @Override
    public void replayPositionRestore() {

    }

    @Override
    public void savePosition() {

    }

    @Override
    public void restorePosition() {

    }

    @Override
    public void replaySpeedSave() {

    }

    @Override
    public void replaySpeedRestore() {

    }

    @Override
    public void saveSpeed() {

    }

    @Override
    public void restoreSpeed() {

    }

    @Override
    public void onEvent(Event e) {
        HashMap<String, Object> args = e.getArgs();

        switch (e.getEventType()) {

            case "MoveLeft":
                moveLeft();
                break;
            case "MoveRight":
                moveRight();
                break;
            case "Shoot":
                shoot();
                break;
            case "BodyCollision":
                //stop();
                break;
        }
    }

    private void shoot() {


        if(GameEngine.iterations > previousShot + 15) {
            previousShot = GameEngine.iterations;
            synchronized (GameEngine.getSpace()) {

                GameEngine.getSpace().add(new Bullet(x, y));

            }
        }
    }
}
