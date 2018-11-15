package core;

import events.Event;
import events.EventArg;
import events.EventManager;
import objects.components.EventDriven;
import objects.components.Movable;
import objects.objects._GameObject;
import time.LocalTime;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ReplayManager implements EventDriven, Runnable {

    private static ReplayManager instance = new ReplayManager();
    private static boolean recordingExists;
    private static boolean recording;
    private static boolean playing;
    private static File eventLog;
    private static FileWriter eventLogger;
    private static BufferedWriter eventBuffer;
    private static LocalTime replayTime;
    private static ArrayList<_GameObject> replaySpace;
    private static int replaySpeed = 1;

    private ReplayManager() {
        recording = false;
    }

    public static ReplayManager getInstance() {
        return instance;
    }

    public static int getReplaySpeed() {
        return replaySpeed;
    }

    public static boolean isRecording() {
        return recording;
    }

    public static boolean isPlaying() {
        return playing;
    }

    private void startRecording() {

        System.out.println("RECORDING STARTED");
        recording = true;
        recordingExists = true;

        replayTime = new LocalTime(TimeUnit.MILLISECONDS);
        for( Object o : GameEngine.getSpace() ) {
            try {
                Movable m = (Movable) o;
                m.replayPositionSave();
                m.replaySpeedSave();
            } catch (ClassCastException e) {
                // Do Nothing
            }
        }

        try {
            eventLogger.close();
            eventLogger = new FileWriter(eventLog);
            eventBuffer = new BufferedWriter(eventLogger);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        System.out.println("RECORDING STOPPED");
        recording = false;
        try {
            eventBuffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void replay() {
        if(!recordingExists) return;

        playing = true;
        GameEngine.getInstance().pauseGame();

        //LocalTime backupTime = GameEngine.getTime();
//        ArrayList<_GameObject> backupSpace = new ArrayList<>();
//        for( _GameObject o : GameEngine.getSpace() ) {
//            backupSpace.add(o.deepClone());
//        }
        //GameEngine.setTime(replayTime);
        //GameEngine.setSpace(replaySpace);

        for( Object o : GameEngine.getSpace() ) {
            try {
                Movable m = (Movable) o;
                m.savePosition();
                m.saveSpeed();
                m.replayPositionRestore();
                m.replaySpeedRestore();
            } catch (ClassCastException e) {
                // Do Nothing
            }
        }

        System.out.println("REPLAY STARTED: Speed=" + replaySpeed);
        try {
            FileReader bufferReader = new FileReader(eventLog);
            BufferedReader replayBuffer = new BufferedReader(bufferReader);
            ArrayList<Event> events = new ArrayList<>();

            String eventLine;
            String[] eventDelimited;
            long time;
            String eventType;
            Event event;

            while((eventLine = replayBuffer.readLine()) != null) {
                //System.out.println("Eventline: " + eventLine);
                eventDelimited = eventLine.split(" ");
                time = (Long.parseLong(eventDelimited[0])) / replaySpeed;
                eventType = eventDelimited[1];
                event = new Event(time, eventType);

                String eventArg;
                String[] eventArgDelimited;
                for(int i=2; i < eventDelimited.length; i++) {
                    //System.out.println("---EventArg: " + eventDelimited[i]);
                    eventArg = eventDelimited[i];
                    eventArgDelimited = eventArg.split(":");
                    event.addArg(new EventArg(eventArgDelimited[0], eventArgDelimited[1]));
                }

                events.add(event);
            }

            if(events.size()>0){
                Thread t = new Thread(EventManager.getInstance());
                t.start();

                int i = 0;
                Event e;
                replayTime.reset();
                long start = events.get(0).getTimeStamp();
                replayTime.addTrackedTime(start);
                while(i < events.size()) {
                    if(replayTime.isPaused())replayTime.play();
                    e = events.get(i);
                    if (e.getTimeStamp() <= replayTime.getTime()) {
                        EventManager.raiseEvent(e);
                        //System.out.println("REPLAY EVENT (" + replayTime.getTime() + "): " + e.toString());
                        i++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //GameEngine.setSpace(backupSpace);
        //GameEngine.setTime(backupTime);

        for( Object o : GameEngine.getSpace() ) {
            try {
                Movable m = (Movable) o;
                m.restorePosition();
                m.restoreSpeed();
            } catch (ClassCastException e) {
                // Do Nothing
            }
        }

        GameEngine.getInstance().pauseGame();
        System.out.println("REPLAY STOPPED");
        playing = false;
    }

    @Override
    public void onEvent(Event e) {
        HashMap<String, Object> args = e.getArgs();

        switch (e.getEventType()) {
            case "RecordStart":
                if(!recording && !playing) {
                    startRecording();
                }
                break;
            case "RecordStop":
                if(recording) {
                    stopRecording();
                }
                break;
            case "Replay":
                ReplayManager.replaySpeed = (Integer) args.get("ReplaySpeed");
                if(!recording && !playing) {
                    replay();
                }
                break;
            default:
                if(recording) {
                    try {
                        String replayLine = e.toString();
                        eventBuffer.write(replayLine);
                        eventBuffer.newLine();
                        System.out.println(replayLine);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void run() {
        EventManager.registerEvent("WILD", instance);

        try {
            eventLog = File.createTempFile("temp", ".txt");
            eventLogger = new FileWriter(eventLog);
            System.out.println(eventLog.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}