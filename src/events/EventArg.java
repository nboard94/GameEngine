package events;

public class EventArg {

    private String argName;
    private Object arg;

    public EventArg(String argName, Object arg) {
        this.argName = argName;
        this.arg = arg;
    }

    public String getArgName() {
        return argName;
    }

    public Object arg() {
        return arg;
    }
}
