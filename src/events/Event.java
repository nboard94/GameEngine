package events;

import core.Timeline;

import java.io.Serializable;
import java.util.HashMap;

/*
*   The Event class represents the message that is created, handled
*   by the EventManager, and passed on to all registered objects to
*   handle the Event.  Events are serialized to be sent over the
*   network and Comparable using a real time timestamp to determine
*   priority.
*
*   @Author Nicholas Board
*   @since 2018-10-27
*/
public class Event implements Comparable<Event>, Serializable {

    private String eventType;                                       // String representing the type of Event.
    private HashMap<String, Object> args = new HashMap<>();         // Holds all Event arguments.  String tells what type each Object is.
    private long timeStamp;                                         // A timestamp created during event raising that using real time.

    /*
    *   Creates an event of eventType with a variable number of arguments.
    *   Constructor adds timestamp and unpacks EventArgs to add to
    *   the arguments HashMap.
    *   @param eventType The type of event this represents.
    *   @param arguments A variable amount of EventArgs that will be used during event handling.
    */
    public Event(String eventType, EventArg... arguments) {
        this.eventType = eventType;
        timeStamp = Timeline.getRealTime();

        for(EventArg e : arguments) {
            args.put(e.getArgName(), e.arg());
        }
    }

    /*
    *   Returns this Event's type.
    *   @return String The string representing the type of Event.
    */
    public String getEventType() {
        return eventType;
    }

    /*
    *   Returns the HashMap containing the Event's arguments.
    *   @return HashMap They key is a String representing the type of Object stored as the value.
    */
    public HashMap<String, Object> getArgs() {
        return args;
    }

    /*
    *   Returns the real time timestamp for this Event to determine priority.
    *   @return long The real time timestamp.
    */
    private long getTimeStamp() {
        return timeStamp;
    }

    /*
    *   Overrides Comparable's default implementation of compareTo() to use the Event timestamps.
    *   @param e The event to compare this Event to.
    *   @return int Returns -1 if this Event should be before that event, 1 if vice-versa, and 0 if somehow the same.
    */
    @Override
    public int compareTo(Event e) {
        return Long.compare(this.getTimeStamp(), e.getTimeStamp());
    }
}
