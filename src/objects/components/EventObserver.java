package objects.components;

import events.Event;

public interface EventObserver {
    void onEvent(Event e);
}
