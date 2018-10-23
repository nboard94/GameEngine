package objects.objects;

import core.GameEngine;
import objects.components.Displayable;
import processing.core.PApplet;

import java.util.HashMap;

public class Rectangle extends _GameObject implements Displayable {

    private PApplet parent;
    private int c;
    private int x,y;
    private boolean left, right, jump;

    public Rectangle(PApplet p){
        parent = p;
        c = parent.color(127);
        y = parent.height/2;
        x = 100;
        confineToEdges();
    }

    public void inital(int x){
        this.x = x;
        y = parent.height/2;
    }

    public int getPosx(){
        return x;
    }

    public int getPosy(){
        return y;
    }

    public void setPosX(int x){
        this.x = x;
    }

    public void setPosY(int y){
        this.y = y;
    }

    public void update() {
        setDirection();
        move();
        fall();
    }

    public void display(){
        parent.rectMode(parent.CENTER);
        parent.fill(c);
        parent.rect(x, y, 25, 25);
    }

    public void setDirection(){
        HashMap<Integer, Boolean> keys = GameEngine.getPressedKeys();

        if ( keys.get(parent.LEFT) != null ) left = keys.get(parent.LEFT);
        else if ( keys.get(parent.RIGHT) != null ) right = keys.get(parent.RIGHT);
        else if ( keys.get(' ') != null ) jump = keys.get(' ');
    }

    public void move(){
        if ( jump ){
            y -= 180;
            jump = !jump;
        }
        x -= (left?  5 : 0);
        x += (right? 5 : 0);

        left = false;
        right = false;
    }

    public void fall(){
        if ( y < parent.height - 25 ){
            y += 20;
        }
    }

    public void confineToEdges(){
        x = PApplet.constrain(x, 0+25, parent.width+25);
    }

    public void changeColor(boolean col){
        if (col){
            c = 255;
        } else {
            c = 127;
        }
    }
}
