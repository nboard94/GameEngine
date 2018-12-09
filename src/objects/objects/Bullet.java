package objects.objects;

import core.GameEngine;
import events.Event;
import objects.components.Collidable;
import objects.components.Displayable;
import objects.components.EventDriven;
import objects.components.Movable;
import processing.core.PApplet;
import processing.core.PConstants;

public class Bullet extends _GameObject implements Collidable, Displayable, Movable {

    private PApplet app = _GameObject.getApp();
    private int x,y;
    private int w,h;
    private int hitW, hitY;

    public Bullet(int x, int y) {
        this.x=x;
        this.y=y;
        this.w=10;
        this.h=10;
        this.hitW=30;
        this.hitY=30;
    }

    @Override
    public boolean detectCollision() {
        return false;
    }

    @Override
    public int[] getRectangleData() {
        return new int[]{x, y, hitW, hitY};
    }

    @Override
    public void display() {
        move();

        app.fill(0,0,0);
        app.ellipseMode(PConstants.CENTER);
        app.ellipse(x, y, w, h);
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
    public void move() {
        y -= 25;
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

    }
}
