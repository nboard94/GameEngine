package objects.objects;

import core.PAppletWrap;
import objects.components.Collidable;
import objects.components.Displayable;

public class PlatformStatic extends _GameObject implements Collidable, Displayable {

    private PAppletWrap app;
    public int x, y;
    public int w, h;
    private int r, b, g;

    public PlatformStatic(PAppletWrap p) {
        app = p;
        x = 300;
        y = 300;
        w = 100;
        h = 25;
        r = 132;
        g = 62;
        b = 11;
    }

    public PlatformStatic(PAppletWrap p, int x, int y, int w, int h, int r, int g, int b) {
        this.app = p;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.g = g;
        this.b = b;
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
}
