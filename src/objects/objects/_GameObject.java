package objects.objects;

import processing.core.PApplet;

import java.util.UUID;

public abstract class _GameObject {

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
