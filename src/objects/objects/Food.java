package objects.objects;

import core.GameEngine;
import events.Event;
import events.EventManager;
import networking.Client;
import networking.ServerOutput;
import objects.components.Collidable;
import objects.components.Displayable;
import processing.core.PApplet;
import processing.core.PConstants;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Food extends _GameObject implements Collidable, Displayable {

    private PApplet app = _GameObject.getApp();
    private int x, y;
    private int w, h;
    private int r, g, b;

    public Food(int w, int h) {
        EventManager.registerEvent("EatFood", this);
        randomSpawn();
        this.w = w;
        this.h = h;
        this.r = 139;
        this.g = 69;
        this.b = 19;
    }

    private void randomSpawn() {
        Random rand = new Random();
        int r1 = rand.nextInt(app.width - 50) + 50;
        int r2 = rand.nextInt(app.height - 50) + 50;
        this.x = r1;
        this.y = r2;
        detectCollision();
    }


    @Override
    public void display() {
        app.fill(r, g, b);
        app.rectMode(PConstants.CENTER);
        app.rect(x, y, w, h);
    }


    @Override
    public boolean detectCollision() {
        Rectangle r1 = new Rectangle(x, y, w, h);
        Rectangle r2;

        for( _GameObject o : GameEngine.getSpace()) {
            try {
                if (o != this) {
                    Collidable c = (Collidable) o;
                    int[] rdata = c.getRectangleData();
                    r2 = new Rectangle(rdata[0], rdata[1], rdata[2], rdata[3]);
                    if (r1.intersects(r2)) {
                        try {
                            SnakeHead s1 = (SnakeHead) c;
                            SnakeBodyPart s2 = (SnakeBodyPart) c;
                            randomSpawn();
                        } catch (ClassCastException e1) {
                            // This block intentionally left empty
                        }
                        return true;
                    }
                }
            } catch (ClassCastException e) {
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
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void onEvent(Event e) {
        HashMap<String, Object> args = e.getArgs();

        switch (e.getEventType()) {

            case "EatFood":
                randomSpawn();
                Client.sendEvent(e);
                break;
        }
    }
}