package events;

import objects.components.EventDriven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class ReplayManager implements EventDriven {

    private static final ReplayManager instance = new ReplayManager();
    private static boolean replayState = false;
    private static BufferedWriter writer;
    private static File replayFile;

    private ReplayManager() {
        EventManager.registerEvent("Replay", this);
        try {
            replayFile = File.createTempFile("replayFile", ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ReplayManager getInstance() {
        return instance;
    }

    private static void replayOnOff() {
        replayState = !replayState;
    }

    @Override
    public void onEvent(Event e) {
        switch (e.getEventType()) {
            case "Replay":
                replayOnOff();
                break;
            default:

                break;
        }
    }


}
