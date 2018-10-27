package events;

import core.Timeline;

import java.io.Serializable;
import java.util.HashMap;

public class Event implements Comparable<Event>, Serializable {

    private String eventType;
    private HashMap<String, Object> args = new HashMap<>();
    private long timeStamp;

    public Event(String eventType, EventArg... arguments) {
        this.eventType = eventType;
        timeStamp = Timeline.getRealTime();

        for(EventArg e : arguments) {
            args.put(e.getArgName(), e.arg());
        }
    }

    public String getEventType() {
        return eventType;
    }

    public long getEventHash() {
        return eventType.hashCode();
    }

    public HashMap<String, Object> getArgs() {
        return args;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int compareTo(Event e) {
        return Long.compare(this.getTimeStamp(), e.getTimeStamp());
    }
}
