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

public class SnakeHead extends _GameObject implements Bounded, Collidable, Controllable, Displayable, EventDriven, Movable {

    private PApplet app = _GameObject.getApp();
    private int x, y;
    private int w, h;
    private int r, g, b;
    private int speedX, speedY;
    private int length;
    private LinkedList<SnakeBodyPart> body;
    public static boolean dead = false;


    public SnakeHead() {
        EventManager.registerEvent("MoveLeft", this);
        EventManager.registerEvent("MoveRight", this);
        EventManager.registerEvent("MoveUp", this);
        EventManager.registerEvent("MoveDown", this);
        EventManager.registerEvent("EatFood", this);
        EventManager.registerEvent("BodyCollision", this);
        this.x = 250;
        this.y = 250;
        this.w = 20;
        this.h = 20;
        this.r = 0;
        this.g = 255;
        this.b = 0;
        this.speedX = 0;
        this.speedY = 0;
        this.length = 2;
        body = new LinkedList<>();
    }

    public void reset() {
        this.x = 250;
        dead = false;
        this.y = 250;
        this.w = 20;
        this.h = 20;
        this.r = 0;
        this.g = 255;
        this.b = 0;
        this.speedX = 0;
        this.speedY = 0;
        this.length = 2;
        body = new LinkedList<>();
    }


    @Override
    public void bound() {
        if(x<=(w)) {
            x = (w);
            //kill();
        }
        else if (x>=(GameEngine.width-w)) {
            x = (GameEngine.width-w);
            //kill();
        }

        if(y<=0) {
            y = (0);
            //kill();
        }
        else if (y>=(GameEngine.height-h)) {
            y = (GameEngine.height-(h));
            //kill();
        }
    }

    private void kill() {
        dead = true;
        stop();
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
                            Food f = (Food) c;
                            EventManager.raiseEvent("EatFood");
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

        for( SnakeBodyPart sbp : body) {
            if(body.indexOf(sbp) != (body.size()-1)) {
                int[] rdata = sbp.getRectangleData();
                r2 = new Rectangle(rdata[0], rdata[1], rdata[2], rdata[3]);
                if(r1.intersects(r2)) {

                    if(body.size() - body.indexOf(sbp) > 3) {
                        EventManager.raiseEvent("BodyCollision");
                        System.out.println(body.indexOf(sbp));
                    }
                    return true;
                }

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
        if( speedX!=-20 ) {
            speedX = 20;
            speedY = 0;
        }
    }

    @Override
    public void moveLeft() {
        if(speedX!=20) {
            speedX = -20;
            speedY = 0;
        }
    }

    @Override
    public void moveDown() {
        if(speedY!=-20){
            speedX = 0;
            speedY = 20;
        }
    }

    @Override
    public void moveUp() {
        if(speedY!=20) {
            speedX = 0;
            speedY = -20;
        }
    }

    public void stop() {
        speedX = 0;
        speedY = 0;
    }

    @Override
    public void display() {
        if(!dead)move();
        bound();
        app.fill(r, g, b);
        //app.rectMode(PConstants.CENTER);
        app.rect(x, y, w, h);
        for(SnakeBodyPart b : body) {
            app.rect(b.getX(), b.getY(), w, h);
        }

    }

    @Override
    public void move() {
        x += speedX;
        y += speedY;

        if(speedX!=0 || speedY!=0) {
            SnakeBodyPart snp = new SnakeBodyPart(x, y, w, h);
            body.add(snp);
            if(body.size() >= length) {
                body.pop();
            }
        }

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

    public void increaseLength() {
        length++;
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
            case "MoveUp":
                moveUp();
                break;
            case "MoveDown":
                moveDown();
                break;
            case "EatFood":
                increaseLength();
                break;
            case "BodyCollision":
                kill();
                break;
        }
    }
}
