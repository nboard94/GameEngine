package objects.objects;

import core.GameEngine;
import core.PAppletWrap;
import objects.components.*;
import processing.core.PApplet;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;

public class PlayerCharacter extends _GameObject implements Bounded, Collidable, Controllable, Gravitized, Displayable, Serializable {

    private PAppletWrap app;
    private int x, y;
    private int w, h;
    private double speedX, speedY;
    private double speedYold = -1;
    private boolean grounded = false;
    private int r, b, g;

    public PlayerCharacter(PAppletWrap p) {
        app = p;
        x = 100;
        y = 300;
        w = 50;
        h = 50;
        r = 250;
        g = 250;
        b = 250;
        speedX = 0;
        speedY = 0;
    }

    @Override
    public void bound() {

        if(x<=(w/2)) x = (w/2);
        else if (x>=(GameEngine.width-w/2)) x = (GameEngine.width-w/2);

        if(y<=0) y = (0);
        else if (y>=(GameEngine.height-h)) y = (GameEngine.height-(h));
    }

    @Override
    public boolean detectCollision() {

        Rectangle r1 = new Rectangle(x, y, w, h);
        Rectangle r2;
        for( _GameObject o : GameEngine.getWorld()) {
            try {
                if(o != this) {
                    Collidable c = (Collidable) o;
                    int[] rdata = c.getRectangleData();
                    r2 = new Rectangle(rdata[0], rdata[1], rdata[2], rdata[3]);
                    if(r1.intersects(r2)) return true;
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
    public void fall() {

        y += speedY;
        speedY += GameEngine.gravity;
        speedY *= 0.95;
    }

    @Override
    public void display() {
        //app.rectMode(app.CENTER);
        app.fill(r, g, b);
        app.rect(x, y, w, h);
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
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void move() {
        int backupX = x;
        int backupY = y;
        double backupSpeedX = speedX;
        double backupSpeedY = speedY;

        fall();
        if(detectCollision()) {
            y = backupY;
            speedY = backupSpeedY;
        }

        x += speedX;
        speedX = 0;
        if(detectCollision()) {
            x = backupX;
            speedX = backupSpeedX;
        }

        grounded = (int) speedY == (int) speedYold;
        speedYold = speedY;
    }

    @Override
    public void control() {
        HashMap<Integer, Boolean> keys = GameEngine.getPressedKeys();

        if(grounded && keys.get(32) != null && keys.get(32)) {
            speedY -=10;
        }

        if(keys.get(app.RIGHT) != null && keys.get(app.RIGHT)) speedX += 5;
        if(keys.get(app.LEFT) != null && keys.get(app.LEFT)) speedX -= 5;
    }

    @Override
    public void update() {
        bound();
        control();
        move();
    }
}
