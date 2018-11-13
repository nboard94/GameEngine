package core;

/*
*   The LocalTime class allows game to keep track of real time and
*   local time.  Provides functionality to pause, unpause, and
*   reset time.
*
*   @author Nicholas Board
*   @since 2018-10-27
*/
public class Timeline{

    private static Timeline instance = new Timeline();          // singleton instance of LocalTime
    private static long realTimeStart;                          // timestamp for the start of real time
    private static long localTimeStart;                         // timestamp for the start of local time
    private static long localRecordedTime;                      // total recorded local time
    private static boolean paused;                              // is local time paused or not

    /*
    *  Private constructor of LocalTime for singleton.
    *  Sets realTimeStart to the current millisecond timestamp and makes sure
    *  that the local time starts at the same time.
    */
    private Timeline() {
        realTimeStart = System.currentTimeMillis();
        localTimeStart = realTimeStart;
        localRecordedTime = 0;
        paused = false;
    }

    /*
    *   Returns the singleton instance of LocalTime.
    */
    public static Timeline getInstance() {
        return instance;
    }

    /*
    *   Currently realTime timestamp.
    *   Just the difference between realTimeStart and current system time.
    */
    public static long getRealTime() {
        return (System.currentTimeMillis() - realTimeStart);
    }

    /*
    *   Prints the current realTime timestamp in a readable format
    *   including hours, minutes, seconds, and milliseconds.
    */
    public static String getRealTimeReadable() {
        long current = getRealTime();
        long millis = current % 1000;
        long seconds = (current / 1000) % 60;
        long minutes = (current / 60000) % 60;
        long hours = current / 3600000;
        return hours + ":" + minutes + ":" + seconds + " " + millis + " (real time)";
    }

    /*
    *   If local time isn't paused, then local time elapsed is the difference
    *   between the current time and the localTimeStart timestamp.
    *   If local time is paused, local time elapsed is often zero.
    */
    private static long getLocalTimeElapsed() {
        if(!paused)
            return System.currentTimeMillis() - localTimeStart;
        else
            return 0;
    }

    /*
    *   Local time is always localRecodedTime summed with the current local time elapsed.
    */
    public static long getLocalTime() {
        return localRecordedTime + getLocalTimeElapsed();
    }

    public static String getLocalTimeReadable() {
        long current = getLocalTime();
        long millis = current % 1000;
        long seconds = (current / 1000) % 60;
        long minutes = (current / 60000) % 60;
        long hours = current / 3600000;
        return hours + ":" + minutes + ":" + seconds + " " + millis + " (local time)";
    }

    /*
    *   Pause local time.
    *   Add the current local time elapsed to the localRecordedTime
    */
    public static void pause() {
        paused = true;
        localRecordedTime += getLocalTimeElapsed();
    }

    /*
    *   Unpause local time.
    *   Reset the localTimeStart timestamp.
    */
    public static void unpause() {
        paused = false;
        localTimeStart = System.currentTimeMillis();
    }

    /*
    *   Returns if the LocalTime is paused or not.
    */
    public static boolean isPaused() {
        return paused;
    }

    /*
    *   Reset local time by erasing the current value of localRecorded time and
    *   resetting the localTimeStart timestamp.
    */
    public static void resetLocalTime() {
        localTimeStart = System.currentTimeMillis();
        localRecordedTime = 0;
    }

    /*
    *   Reset local and real time by assigning instance to a new LocalTime.
    */
    public static void resetAllTime() {
        instance = new Timeline();
    }

}
