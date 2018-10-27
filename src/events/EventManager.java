package events;

import core.GameEngine;
import objects.components.EventDriven;
import objects.objects.PlayerCharacter;

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
    *   @param eventType The type of event this object wants to register for.
    *   @param o The object that wants to register for the type of event.
    */
    public static void registerEvent(String eventType, EventDriven o) {
        if(!eventRegistrants.containsKey(eventType)) {
            eventRegistrants.put(eventType, new ArrayList<>());
        }
        eventRegistrants.get(eventType).add(o);
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
                for( EventDriven o : eventRegistrants.get(e.getEventType())) {
                    o.onEvent(e);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(EventManager.getInstance());
        t.start();

        PlayerCharacter test = new PlayerCharacter(GameEngine.getInstance());
        EventManager.registerEvent("TestEvent2", test);
        EventManager.registerEvent("TestEvent1", test);

        EventManager.raiseEvent("TestEvent1", new EventArg("TestArg1", "cat"),
                new EventArg("TestArg2", "dog"));
        EventManager.raiseEvent("TestEvent2", new EventArg("TestArg1", "cat"),
                new EventArg("TestArg2", "dog"));
    }
}
