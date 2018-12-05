package objects.objects;

import core.GameEngine;
import core.ReplayManager;
import events.Event;
import events.EventArg;
import events.EventManager;
import objects.components.*;
import processing.core.PApplet;
import scripting.ScriptManager_JS;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;

public class PlayerCharacter extends _GameObject implements Bounded, Collidable, Controllable, Gravitized, Displayable, Serializable {

    private PApplet app = _GameObject.getApp();
    private int x, y;
    public int xReplay, yReplay;
    public int xBackup, yBackup;
    private int w, h;
    private double speedX, speedY;
    private double speedReplayX, speedReplayY;
    private double speedBackupX, speedBackupY;
    private double speedYold = -1;
    private boolean grounded = false;
    private int r, b, g;

    public PlayerCharacter() {
        x = 100;
        y = 300;
        w = 50;
        h = 50;
        r = 250;
        g = 250;
        b = 250;
        speedX = 0;
        speedY = 0;

        EventManager.registerEvent("MoveRight", this);
        EventManager.registerEvent("MoveLeft", this);
        EventManager.registerEvent("Jump", this);
    }

    public PlayerCharacter(PlayerCharacter other) {
        this.app = other.app;
        this.x = other.x;
        this.y = other.y;
        this.w = other.w;
        this.h = other.h;
        this.speedX = other.speedX;
        this.speedY = other.speedY;
        this.speedYold = other.speedYold;
        this.grounded = other.grounded;
        this.r = other.r;
        this.b = other.b;
        this.g = other.g;

        EventManager.registerEvent("MoveRight", this);
        EventManager.registerEvent("MoveLeft", this);
        EventManager.registerEvent("Jump", this);
        EventManager.registerEvent("PlayerCollision", this);
    }

    @Override
    public void bound() {

        if(x<=(w/2)) x = (w/2);
        else if (x>=(GameEngine.width-w/2)) x = (GameEngine.width-w/2);

        if(y<=0) y = (0);
        else if (y>=(GameEngine.height-h)) y = (GameEngine.height-(h));
    }

    @Override
    public boolean detectCollision() {

        Rectangle r1 = new Rectangle(x, y, w, h);
        Rectangle r2;
        for( _GameObject o : GameEngine.getSpace()) {
            try {
                if(o != this) {
                    Collidable c = (Collidable) o;
                    int[] rdata = c.getRectangleData();
                    r2 = new Rectangle(rdata[0], rdata[1], rdata[2], rdata[3]);
                    if(r1.intersects(r2)) {
                        try {
                            PlayerCharacter p2 = (PlayerCharacter) c;
                            EventManager.raiseEvent("PlayerCollision", new EventArg("p2", p2.getUUID()));
                        } catch (ClassCastException e1) {
                            // This block intentionally left empty
                        }
                        return true;
                    }
                }
            } catch(ClassCastException e) {
                // This block intentionally left empty
            }
        }

        return false;
    }

    @Override
    public int[] getRectangleData() {
        return new int[]{x, y, w, h};
    }

    @Override
    public void fall() {

        y += speedY;
        speedY += GameEngine.gravity;
        speedY *= 0.95;
    }

    @Override
    public void display() {
        //app.rectMode(app.CENTER);
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
    public void replayPositionSave() {
        this.xReplay = this.x;
        this.yReplay = this.y;
    }

    @Override
    public void replayPositionRestore() {
        this.x = this.xReplay;
        this.y = this.yReplay;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    public int getW() {
        return this.w;
    }

    public int getH() {
        return this.h;
    }

    public int getR() {
        return this.r;
    }

    public int getG() {
        return this.g;
    }

    public int getB() {
        return this.b;
    }

    public double getSpeedX() {
        return this.speedX;
    }

    public double getSpeedY() {
        return this.speedY;
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
    public void move() {
        int backupX = x;
        int backupY = y;
        double backupSpeedX = speedX;
        double backupSpeedY = speedY;

        fall();
        if(detectCollision()) {
            y = backupY;
            speedY = backupSpeedY;
        }

        x += speedX;
        speedX = 0;
        if(detectCollision()) {
            x = backupX;
            speedX = backupSpeedX;
        }

        grounded = (int) speedY == (int) speedYold;
        speedYold = speedY;
    }

    @Override
    public void moveRight() {
        speedX += 5;
    }

    @Override
    public void moveLeft() {
        speedX -= 5;
    }

    @Override
    public void jump() {
        if(grounded) {
            speedY -=10;
        }
    }

    public boolean getGrounded() {
        return grounded;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void playerCollision(String p2UUID) {

    }

    @Override
    public void update() {
        bound();
        move();
    }

    @Override
    public void onEvent(Event e) {
        HashMap<String, Object> args = e.getArgs();

        switch (e.getEventType()) {
            case "MoveRight":
                moveRight();
                break;
            case "MoveLeft":
                moveLeft();
                break;
            case "Jump":
                //jump();
                synchronized(ScriptManager_JS.getInstance()) {
                    ScriptManager_JS.loadScript("src\\scripting\\scripts\\jump.js");
                    ScriptManager_JS.bindArgument("player", this);
                    ScriptManager_JS.executeScript();
                }
                break;
            case "PlayerCollision":
                String p2 = (String) args.get("p2");
                playerCollision(p2);
                break;
        }
    }

    @Override
    public PlayerCharacter clone() {
        return new PlayerCharacter(this);
    }
}
