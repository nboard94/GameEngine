package objects.objects;

import core.GameEngine;
import events.Event;
import objects.components.Displayable;

public class SpawnPoint extends _GameObject  {

    private int x, y;
    private Displayable toSpawn;

    public SpawnPoint(int x, int y, Displayable toSpawn) {
        this.x = x;
        this.y = y;
        this.toSpawn = toSpawn;
        worldCheck();
    }

    public SpawnPoint(Displayable toSpawn) {
        this.x = toSpawn.getX();
        this.y = toSpawn.getY();
        this.toSpawn = toSpawn;
        worldCheck();
    }

    public void spawn() {
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

    }
}
