package events;

import objects.components.EventDriven;

public class ReplayManager implements EventDriven {

    private static final ReplayManager instance = new ReplayManager();

    private ReplayManager() {

    }

    public static ReplayManager getInstance() {
        return instance;
    }

    @Override
    public void onEvent(Event e) {
        switch (e.getEventType()) {
            case "ReplayStart":
                break;
            case "ReplayEnd":
                break;
            default:
                break;
        }


    }
}
