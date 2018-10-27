package objects.objects;

import objects.components.EventDriven;
import processing.core.PApplet;

import java.util.UUID;

public abstract class _GameObject implements EventDriven{

    private PApplet app;
    private UUID uuid = null;

    public UUID getUUID() {
        if(uuid == null) uuid = UUID.randomUUID();
        return this.uuid;
    }

    public void resetApp(PApplet p) {
        this.app = p;
    }

    public void update() {

    }
}
