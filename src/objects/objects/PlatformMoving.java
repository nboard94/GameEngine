package objects.objects;

import core.ReplayManager;
import events.Event;
import events.EventManager;
import objects.components.Collidable;
import objects.components.Displayable;
import objects.components.Movable;
import processing.core.PApplet;

public class PlatformMoving extends _GameObject implements Collidable, Displayable, Movable {

    private PApplet app = _GameObject.getApp();
    public int x, y;
    public int xReplay, yReplay;
    public int xBackup, yBackup;
    public int w, h;
    private int r, b, g;
    private double speedX, speedY;
    private double speedReplayX, speedReplayY;
    private double speedBackupX, speedBackupY;
    private boolean movesX, movesY;

    private int originX, originY;
    private int xRange, yRange;

    public PlatformMoving(int x, int y, int w, int h, int r, int g, int b, int xRange,
                          int yRange, boolean movesX, boolean movesY, double speedX, double speedY) {
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

    public PlatformMoving(PlatformMoving other) {
        this.app = other.app;
        this.x = other.x;
        this.y = other.y;
        this.w = other.w;
        this.h = other.h;
        this.r = other.r;
        this.b = other.b;
        this.g = other.g;
        this.speedX = other.speedX;
        this.speedY = other.speedY;
        this.movesX = other.movesX;
        this.movesY = other.movesY;
        this.originX = other.originX;
        this.originY = other.originY;
        this.xRange = other.xRange;
        this.yRange = other.yRange;
    }





    @Override
    public void replayPositionSave() {
        this.xReplay = this.x;
        this.yReplay = this.y;
    }

    @Override
    public void replayPositionRestore() {
        this.x = this.xReplay;
        this.y = this.yReplay;
    }

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
    public void savePosition() {
        this.xBackup = x;
        this.yBackup = y;
    }

    @Override
    public void restorePosition() {
        this.x = xBackup;
        this.y = yBackup;
    }

    @Override
    public void replaySpeedSave() {
        speedReplayX = speedX;
        speedReplayY = speedY;
    }

    @Override
    public void replaySpeedRestore() {
        speedX = speedReplayX;
        speedY = speedReplayY;
    }

    @Override
    public void saveSpeed() {
        speedBackupX = speedX;
        speedBackupY = speedY;
    }

    @Override
    public void restoreSpeed() {
        speedX = speedBackupX;
        speedY = speedBackupY;
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
