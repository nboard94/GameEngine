package objects.objects;

import events.Event;
import objects.components.Collidable;
import processing.core.PApplet;

public class SnakeBodyPart extends _GameObject implements Collidable {

    private PApplet app = _GameObject.getApp();
    private int x, y;
    private int w, h;

    SnakeBodyPart(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public void onEvent(Event e) {

    }

    @Override
    public boolean detectCollision() {
        return false;
    }

    @Override
    public int[] getRectangleData() {
        return new int[]{x, y, w, h};
    }
}
