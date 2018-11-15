package objects.objects;

import objects.components.EventDriven;
import processing.core.PApplet;

import java.io.*;
import java.util.UUID;

public abstract class _GameObject implements EventDriven, Serializable{

    private static PApplet app;
    private UUID uuid = UUID.randomUUID();

    public UUID getUUID() {
        return this.uuid;
    }

    public static PApplet getApp() {
        return app;
    }

    public static void setApp(PApplet p) {
        app = p;
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
