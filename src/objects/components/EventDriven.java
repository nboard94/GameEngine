package objects.components;

import events.Event;

/*
*   This interface guarantees that an object will implement
*   the onEvent method.  Also required to be implemented if the object
*   wants to interact with the EventManager.
*
*   @author Nicholas Board
*   @since 2018-10-27
*/
public interface EventDriven {

    /*
    *   The Event handling method.
    *   @param e The Event to handle.
    */
    void onEvent(Event e);
}
