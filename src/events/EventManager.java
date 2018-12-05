package events;

import objects.components.EventDriven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
*   The EventManager class allows for the basic functionality required for
*   registering, raising, and handling events throughout the GameEngine.
*   The EventManager follows the singleton pattern, so there is only one instance,
*   and implements Runnable so that it can continuously handle queued Events.
*
*   @author Nicholas Board
*   @since 2018-10-27
*/
public class EventManager implements Runnable{

    private static final EventManager instance = new EventManager();                                    // Singleton instance of EventManager.
    private static final HashMap<String, ArrayList<EventDriven>> eventRegistrants = new HashMap<>();    // Each Event string acts as a key to get an ArrayList of registered EventDriven Objects.
    private static final PriorityBlockingQueue<Event> eventQueue = new PriorityBlockingQueue<>();       // The queue of Events awaiting handling, prioritized by age in real time.
    private static final ArrayList<EventDriven> wildRegistrants = new ArrayList<>();                    // List of EventDriven Objects that register to all events.
    private static boolean stop = false;                                                                // The EventManager is stopped when this value is set to true.

    /*
    *   This private constructor for EventManager enforces the Singleton Pattern.
    */
    private EventManager() { }

    /*
    *   This method is used to access the Singleton instance of EventManager.
    *   @return EventManager The single instance of the EventManager.
    */
    public static EventManager getInstance() {
        return instance;
    }

    /*
    *   This method registers any given object with some type of event.
    *   Creates an ArrayList when a new event type is passed in, and
    *   objects registered to that event are stored in the associated list.
    *   If a registrant wants to be wild, it is added to the list of wild
    *   registrants which is then inserted into all existing registrant lists.
    *   It is also added each time a new registrant list is created.
    *   @param eventType The type of event this object wants to register for.
    *   @param o The object that wants to register for the type of event.
    */
    public static void registerEvent(String eventType, EventDriven o) {
        if(eventType.equals("WILD")) {
            wildRegistrants.add(o);
        } else {
            if(!eventRegistrants.containsKey(eventType)) {
                eventRegistrants.put(eventType, new ArrayList<>());
            }
            eventRegistrants.get(eventType).add(o);
        }
    }

    /*
    *    Adds an event of eventType and associated variable number of EventArgs
    *    to the eventQueue.
    *    @param eventType The type of event to raise.
    *    @param arguments Variable number of arguments to associate with this Event.
    */
    public static void raiseEvent(String eventType, EventArg... arguments) {
        eventQueue.put(new Event(eventType, arguments));
    }


    public static void raiseEvent(Event e) {
        eventQueue.put(e);
    }
    /*
    *   When called, the EventManager thread is stopped.
    */
    public static void stopEventManager() {
        stop = true;
    }

    /*
    *    Continuously grabs the highest priority Event from the head of the
    *    eventQueue, gets the list of all EventDriven objects for that eventType
    *    from the HashMap, and iterates through all of those EventDriven objects
    *    to call their onEvent() method.
    */
    @Override
    public void run() {
        Event e;
        while(!stop) {
            try {
                e = eventQueue.take();
                if(eventRegistrants.containsKey(e.getEventType())){
                    for( EventDriven o : eventRegistrants.get(e.getEventType())) {
                        o.onEvent(e);
                    }
                }
                for( EventDriven o : wildRegistrants) {
                    o.onEvent(e);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
