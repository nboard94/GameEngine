package objects.objects;

import core.GameEngine;
import events.Event;
import events.EventManager;
import objects.components.Displayable;

import java.util.HashMap;

public class SpawnPoint extends _GameObject  {

    private int x, y;
    private Displayable toSpawn;

    public SpawnPoint(int x, int y, Displayable toSpawn) {
        this.x = x;
        this.y = y;
        this.toSpawn = toSpawn;
        worldCheck();
        EventManager.registerEvent("PlayerSpawn", this);
    }

    public SpawnPoint(Displayable toSpawn) {
        this.x = toSpawn.getX();
        this.y = toSpawn.getY();
        this.toSpawn = toSpawn;
        worldCheck();
        EventManager.registerEvent("PlayerSpawn", this);
    }

    public SpawnPoint(SpawnPoint other) {
        this.x = other.x;
        this.y = other.y;
        this.toSpawn = other.toSpawn;
    }

    public void spawn() {
        toSpawn.setX(this.x);
        toSpawn.setY(this.y);
        toSpawn.display();
    }

    public void spawn(String id) {
        toSpawn.setX(this.x);
        toSpawn.setY(this.y);
        toSpawn.display();
    }

    private void worldCheck() {
        if(!GameEngine.getSpace().contains(toSpawn)) GameEngine.getSpace().add((_GameObject) toSpawn);
    }

    public _GameObject getObject() {
        return (_GameObject) this.toSpawn;
    }

    @Override
    public void onEvent(Event e) {
        HashMap<String, Object> args = e.getArgs();

        switch (e.getEventType()) {
            case "PlayerSpawn":
                String p2 = (String) args.get("player");
                spawn(p2);
                break;
        }
    }
}
