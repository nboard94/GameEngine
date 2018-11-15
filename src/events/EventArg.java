package events;

/*
*   The EventArg class represents arguments in Events.
*   Each EventArg has String representing the type of argument,
*   and an Object that is the argument itself.  Exists so that
*   we can pass a variable number of arguments to a new Event.
*
*   @author Nicholas Board
*   @since 2018-10-27
*/
public class EventArg {

    private String argName;         // String represents the type of argument.
    private Object arg;             // The Event argument itself.

    /*
    *   Construct an Event's argument with name and object.
    *   @param argName The know what type of argument this is.
    *   @param arg The argument Object itself.
    */
    public EventArg(String argName, Object arg) {
        this.argName = argName;
        this.arg = arg;
    }

    /*
    *   Gets the EventArg's argument type.
    *   @return String The type of argument.
    */
    String getArgName() {
        return argName;
    }

    /*
    *   Gets the EventArg's argument.
    *   @return Object The EventArg's argument.
    */
    Object arg() {
        return arg;
    }
}
