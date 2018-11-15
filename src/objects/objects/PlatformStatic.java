package objects.objects;

import events.Event;
import objects.components.Collidable;
import objects.components.Displayable;
import processing.core.PApplet;
import processing.core.PGraphics;

public class PlatformStatic extends _GameObject implements Collidable, Displayable {

    private PApplet app = _GameObject.getApp();
    public int x, y;
    public int w, h;
    private int r, b, g;

    public PlatformStatic(int x, int y, int w, int h, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public PlatformStatic(PlatformStatic other) {
        this.app = other.app;
        this.x = other.x;
        this.y = other.y;
        this.w = other.w;
        this.h = other.h;
        this.r = other.r;
        this.b = other.b;
        this.g = other.g;
    }

    @Override
    public boolean detectCollision() {
        return false;
    }

    @Override
    public int[] getRectangleData() {
        return new int[]{x, y, w, h};
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
