package objects.components;

import events.Event;

public interface EventDriven {

    void onEvent(Event e);
}
