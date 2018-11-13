package time;

import java.util.concurrent.TimeUnit;

/*
 *   The LocalTime class is an anchored timeline that allows functionality
 *   such as pausing and playing.  It can report the current local time
 *   according to an adjustable ticSize.
 *
 *   @author Nicholas Board
 *   @since 2018-11-11
 */
public class LocalTime implements Timeline{

    private Timeline anchor;            // timeline that this one is anchored to
    private TimeUnit ticSize;           // size used to report the time
    private long timeGenesis;
    private long timeStart;             // timestamp of current start
    private long timeTracked;           // current amount of previous time elapsed
    private long pauseStart;            // timestamp of current pause
    private boolean paused;             // is the timeline paused or not

    /*
    *    Constructs an instance of LocalTime using the
    *    chosen ticSize, and uses real time as the
    *    anchored timeline.
    *    @param ticSize The TimeUnit used to indicate the current tic size for this LocalTime.
    */
    public LocalTime(TimeUnit ticSize) {
        this.anchor = RealTime.getInstance();
        this.ticSize = ticSize;
        this.timeGenesis = anchor.getTime();
        this.timeStart = timeGenesis;
        this.timeTracked = 0;
        this.pauseStart = 0;
        this.paused = false;
    }

    /*
     *    Constructs an instance of LocalTime using the
     *    chosen ticSize, and using the chosen timeline
     *    as the anchor
     *    @param ticSize The TimeUnit used to indicate the current tic size for this LocalTime.
     *    @param anchor The timeline to anchor this one to.
     */
    public LocalTime(TimeUnit ticSize, Timeline anchor) {
        this.anchor = anchor;
        this.ticSize = ticSize;
        this.timeStart = anchor.getTime();
        this.timeTracked = 0;
        this.pauseStart = 0;
        this.paused = false;
    }

    /*
    *   Adjust this LocalTime's tic size.
    *   @param ticSize The TimeUnit used to indicate the tic size for this LocalTime.
    */
    public void setTicSize(TimeUnit ticSize) {
        this.ticSize = ticSize;
    }

    /*
    *
    */
    public boolean pause() {
        long pauseTime = anchor.getTime();
        if(!paused) {
            paused = true;
            timeTracked += (pauseTime - timeStart);
            timeStart = 0;
            pauseStart = pauseTime;
            return true;
        } else return false;
    }

    public boolean play() {
        long playTime = anchor.getTime();
        if(paused) {
            paused = false;
            timeStart = playTime;
            pauseStart = 0;
            return true;
        } else return false;
    }

    public void reset() {
        this.timeStart = timeGenesis;
        this.timeTracked = 0;
        this.pauseStart = 0;
        this.paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public long getTime() {
        long time;
        if(paused)
            time = timeTracked;
        else
            time = timeTracked + (anchor.getTime() - timeStart);
        return ticSize.convert(time, ticSize);
    }

    public long getPausedTime() {
        long time;
        if(paused)
            time = anchor.getTime() - pauseStart;
        else
            time = 0;
        return ticSize.convert(time, ticSize);
    }

    public String getLocalTimeReadable() {
        long current = getTime();
        long millis = current % 1000;
        long seconds = (current / 1000) % 60;
        long minutes = (current / 60000) % 60;
        long hours = current / 3600000;
        return hours + ":" + minutes + ":" + seconds + " " + millis + " (local time)";
    }

    public String getPausedTimeReadable() {
        long current = getPausedTime();
        long millis = current % 1000;
        long seconds = (current / 1000) % 60;
        long minutes = (current / 60000) % 60;
        long hours = current / 3600000;
        return hours + ":" + minutes + ":" + seconds + " " + millis + " (paused time)";
    }
}
