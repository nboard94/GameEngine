package objects.objects;

import core.GameEngine;
import objects.components.*;

import java.awt.Rectangle;

public class PlayerCharacter extends _GameObject implements Bounded, Collidable, Controllable, Gravitized, Displayable {

    private int x, y;
    private int w, h;
    private double speed;

    public PlayerCharacter() {
        x = 100;
        y = 100;
        w = 50;
        h = 50;
        speed = 0;

        bound();
    }


    @Override
    public void bound() {
        x = GameEngine.constrain(x, (this.w / 2), GameEngine.width - (this.w/2));
        y = GameEngine.constrain(x, (this.h / 2), GameEngine.height - (this.h/2));
    }

    @Override
    public boolean detectCollision() {
        for( _GameObject o : GameEngine.getWorld()) {
            try {
                Collidable c = (Collidable) o;
            } catch(ClassCastException e) {
                // This block intentionally left empty
            }
        }

        return true;
    }

    @Override
    public void fall() {

        y += speed;
        speed += GameEngine.gravity;
        speed *= 0.95;
    }

    @Override
    public void display() {
        //Rectangle javaRec = new Rectangle(x, y, );
    }

    @Override
    public void move() {

    }

    @Override
    public void control() {

    }

    @Override
    public void update() {

    }
}
