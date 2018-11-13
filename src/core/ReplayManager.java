package core;

import events.Event;
import events.EventManager;
import objects.components.EventDriven;
import objects.objects._GameObject;
import time.LocalTime;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ReplayManager implements EventDriven, Runnable {

    private static ReplayManager instance = new ReplayManager();
    private static boolean recording;
    private static boolean playing;
    private static File eventLog;
    private static FileWriter eventLogger;
    private static BufferedWriter eventBuffer;
    private static LocalTime replayTime;
    private static ArrayList<_GameObject> replaySpace;

    private ReplayManager() {
        recording = false;
    }

    public static ReplayManager getInstance() {
        return instance;
    }

    public static boolean isRecording() {
        return recording;
    }

    private void startRecording() {

        System.out.println("RECORDING STARTED");
        recording = true;

        replaySpace = new ArrayList<>();
        for( _GameObject o : GameEngine.getSpace()) {
            o.resetApp(GameEngine.getInstance());
            replaySpace.add(o.deepClone());
        }
        replayTime = new LocalTime(TimeUnit.MILLISECONDS);

        eventBuffer = new BufferedWriter(eventLogger);
    }

    private void stopRecording() {
        System.out.println("RECORDING STOPPED");
        recording = false;
        try {
            eventBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void replay() {

        GameEngine.getTime().pause();
        LocalTime backupTime = GameEngine.getTime();
        ArrayList<_GameObject> backupSpace = new ArrayList<>();
        for( _GameObject o : GameEngine.getSpace() ) {
            o.resetApp(GameEngine.getInstance());
            backupSpace.add(o.deepClone());
        }

        //GameEngine.setTime(replayTime);
        //GameEngine.setSpace(replaySpace);

        System.out.println("REPLAY STARTED");
        playing = true;
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
                eventDelimited = eventLine.split(" ");
                time = Long.parseLong(eventDelimited[0]);
                eventType = eventDelimited[1];
                event = new Event(time, eventType);
                events.add(event);
            }

            int i = 0;
            replayTime.reset();

            for(Event e : events) {
                if(replayTime.isPaused())replayTime.play();

                if(replayTime.getTime() >= e.getTimeStamp()) {
                    System.out.println(e.toString());
                    EventManager.raiseEvent(e);
                }
            }
//            while(i!=events.size()) {
//
//                if(events.get(i).getTimeStamp()>=replayTime.getTime()) {
//                    System.out.println(events.get(i).getEventType());
//                    i++;
//                }
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        playing = false;
        System.out.println("REPLAY STOPPED");

        //GameEngine.setSpace(backupSpace);
        //GameEngine.setTime(backupTime);
    }

    @Override
    public void onEvent(Event e) {
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
                if(!recording) {
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
        EventManager.registerEvent("RecordStart", instance);
        EventManager.registerEvent("RecordStop", instance);
        EventManager.registerEvent("Replay", instance);
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