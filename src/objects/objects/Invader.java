package objects.objects;

import core.GameEngine;
import events.Event;
import events.EventManager;
import objects.components.Bounded;
import objects.components.Collidable;
import objects.components.Displayable;
import objects.components.Movable;
import processing.core.PApplet;
import scripting.ScriptManager_JS;

import java.awt.*;

public class Invader extends _GameObject implements Bounded,Collidable, Displayable, Movable {

    private PApplet app = _GameObject.getApp();
    public int x, y, startX, startY;
    public int w, h;
    private int r, b, g;
    private double speedX, speedY;
    private int originX, originY;
    private int xRange, yRange;
    boolean alive = true;

    public Invader(int x, int y) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.w = 20;
        this.h = 30;
        this.r = 255;
        this.g = 0;
        this.b = 0;
        speedX = 10 * (GameEngine.wins);
        speedY = 25;
        this.xRange = app.width-100;
        this.yRange = app.height-100;
        originX = x;
        originY = y;
    }

    public void setSpeed(double speed) {
        speedX = 10 * (GameEngine.wins);
        speedY = speed;
    }

    public void resetInvader() {
        alive = true;
        this.x = startX;
        this.y = startY;
        this.w = 20;
        this.h = 30;
        this.r = 255;
        this.g = 0;
        this.b = 0;
        speedX = 10 * (GameEngine.wins);
        speedY = 25;
    }


    @Override
    public void replayPositionSave() {
    }

    @Override
    public void replayPositionRestore() {
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
                            Bullet f = (Bullet) c;
                            GameEngine.score += 10;
                            alive = false;
                            GameEngine.remaining--;
                            System.out.println(this.getUUID() + " alive: " + alive);
                            die();
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

    void die() {
        this.x = -10;
        this.y = -10;
        this.w = 0;
        this.h = 0;
        this.speedY = 0;
        this.speedX = 0;
    }

    @Override
    public int[] getRectangleData() {
        return new int[]{x, y, w, h};
    }

    @Override
    public void display() {
        if(this.y>Ship.y) {
            EventManager.raiseEvent("InvaderWin");
        }

        this.detectCollision();

        if(alive) {
            app.fill(r, g, b);
            app.rect(x, y, w, h);
        }
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
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
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void move() {

        if(x <= (20) || x >= (app.width-20)) {
            speedX *= -1;
            y += speedY;
        }
        x += speedX;
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public int getXrange() {
        return xRange;
    }

    public void setXrange(int xRange) {
        this.xRange = xRange;
    }

    public int getYrange() {
        return yRange;
    }

    public void setYrange(int yRange) {
        this.yRange = yRange;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    @Override
    public void update() {
        if(alive)move();
        //bound();
    }

    @Override
    public void onEvent(Event e) {

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
}