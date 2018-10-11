package events;

import java.util.HashMap;

public class Event {

    private EventType type;
    private HashMap<String, Object> args;

    public Event(EventType t) {
        this.type = t;
    }

    public Event(EventType t, HashMap<String, Object> a) {
        this.type = t;
        this.args = a;
    }

    public Event(Event e) {
        this.type = e.getType();
        this.args = e.getArgs();
    }

    public EventType getType() {
        return this.type;
    }

    public void setType(EventType type) {
        this.type=type;
    }

    public HashMap<String, Object> getArgs() {
        return this.args;
    }

    public void addArg(String s, Object o) {
        this.args.put(s, o);
    }
}
