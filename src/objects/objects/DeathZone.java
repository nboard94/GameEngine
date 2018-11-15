package objects.objects;

import events.Event;
import events.EventArg;
import events.EventManager;
import objects.components.Collidable;
import objects.components.Displayable;
import processing.core.PApplet;

import java.awt.Rectangle;
import java.util.HashMap;

public class DeathZone extends _GameObject implements  Displayable {

    private PApplet app = _GameObject.getApp();
    private SpawnPoint s;
    private int x, y, w, h, r, g, b;

    public DeathZone(SpawnPoint s, int x, int y, int w, int h, int r, int g, int b) {
        this.s=s;
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.r=r;
        this.g=g;
        this.b=b;
        EventManager.registerEvent("PlayerDeath", this);
    }

    public DeathZone(DeathZone other) {
        this.app = other.app;
        this.s = other.s;
        this.x = other.x;
        this.y = other.y;
        this.w = other.w;
        this.h = other.h;
        this.r = other.r;
        this.g = other.g;
        this.b = other.b;
        EventManager.registerEvent("PlayerDeath", this);
    }

    public void detectCollision() {
        Rectangle r1 = new java.awt.Rectangle(x, y, w, h);

        Collidable c = (Collidable) s.getObject();
        int[] rdata = c.getRectangleData();
        Rectangle r2 = new Rectangle(rdata[0], rdata[1], rdata[2], rdata[3]);
        if( r1.intersects(r2)) {
            EventManager.raiseEvent("PlayerDeath", new EventArg("player", ((_GameObject) c).getUUID().toString()));
        }
    }

    public int[] getRectangleData() {
        return new int[]{x, y, w, h};
    }

    @Override
    public void update() {

        detectCollision();
        //s.spawn();
    }

    public void spawn(String id) {

        EventManager.raiseEvent("PlayerSpawn", new EventArg("player", id));
        //s.spawn();
    }

    @Override
    public void display() {
        app.fill(r, g, b);
        app.rect(x, y, w, h);
    }

    @Override
    public void setX(int x) {
        this.x=x;
    }

    @Override
    public void setY(int y) {
        this.y=y;
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
    public void onEvent(Event e) {
        HashMap<String, Object> args = e.getArgs();

        switch (e.getEventType()) {
            case "PlayerDeath":
                String p2 = (String) args.get("player");
                spawn(p2);
                break;
        }
    }
}
