package objects.objects;

import events.Event;
import objects.components.Collidable;
import objects.components.Displayable;
import processing.core.PApplet;

import java.awt.Rectangle;

public class DeathZone extends _GameObject implements  Displayable {

    private PApplet app;
    private SpawnPoint s;
    private int x, y, w, h, r, g, b;

    public DeathZone(PApplet p, SpawnPoint s, int x, int y, int w, int h, int r, int g, int b) {
        this.app = p;
        this.s=s;
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.r=r;
        this.g=g;
        this.b=b;
    }

    public boolean detectCollision() {
        Rectangle r1 = new java.awt.Rectangle(x, y, w, h);

        Collidable c = (Collidable) s.getObject();
        int[] rdata = c.getRectangleData();
        Rectangle r2 = new Rectangle(rdata[0], rdata[1], rdata[2], rdata[3]);
        return r1.intersects(r2);
    }

    public int[] getRectangleData() {
        return new int[]{x, y, w, h};
    }

    @Override
    public void update() {
        if(detectCollision()) s.spawn();
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
    }
}
