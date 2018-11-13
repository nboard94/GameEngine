package objects.objects;

import objects.components.EventDriven;
import processing.core.PApplet;

import java.io.*;
import java.util.UUID;

public abstract class _GameObject implements EventDriven, Serializable{

    private PApplet app;
    private UUID uuid = null;

    public UUID getUUID() {
        if(uuid == null) uuid = UUID.randomUUID();
        return this.uuid;
    }

    public void resetApp(PApplet p) {
        this.app = p;
    }

    public _GameObject deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (_GameObject) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update() {}
}
