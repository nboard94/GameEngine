package objects.objects;

import events.Event;
import objects.components.Collidable;
import objects.components.Displayable;
import objects.components.Movable;
import processing.core.PApplet;

public class PlatformMoving extends _GameObject implements Collidable, Displayable, Movable {

    private PApplet app;
    public int x, y;
    public int w, h;
    private int r, b, g;
    private double speedX, speedY;
    private boolean movesX, movesY;

    private int originX, originY;
    private int xRange, yRange;

    public PlatformMoving(PApplet p) {
        app = p;
        x = 400;
        y = 400;
        w = 100;
        h = 25;
        r = 132;
        g = 62;
        b = 11;

        originX = x;
        originY = y;

        xRange = 100;
        yRange = 50;

        speedX = 5;
        speedY = 5;
        movesX = false;
        movesY = true;
    }

    public PlatformMoving(PApplet p, int x, int y, int w, int h, int r, int g, int b, int xRange,
                          int yRange, boolean movesX, boolean movesY, double speedX, double speedY) {
        this.app = p;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.g = g;
        this.b = b;
        this.xRange = xRange;
        this.yRange = yRange;
        this.movesX = movesX;
        this.movesY = movesY;
        this.speedX = speedX;
        this.speedY = speedY;
        originX = x;
        originY = y;
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
        if(x <= (originX-xRange) || x >= (originX+xRange)) speedX *= -1;
        if(y <= (originY-yRange) || y >= (originY+yRange)) speedY *= -1;
        if(movesX) x += speedX;
        if(movesY) y += speedY;
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void onEvent(Event e) {

    }
}
