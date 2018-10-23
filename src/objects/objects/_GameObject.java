package objects.objects;

import java.util.UUID;

public abstract class _GameObject {

    private UUID uuid = null;

    public UUID getUUID() {
        if(uuid == null) uuid = UUID.randomUUID();
        return this.uuid;
    }

    public void update() {

    }
}
