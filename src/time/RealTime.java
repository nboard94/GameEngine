package time;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/*
 *   The RealTime class is the game's measure of persistent
 *   system time.  Only one measure of real time is needed, so
 *   RealTime is a singleton.  Simply reports the time.
 *
 *   @author Nicholas Board
 *   @since 2018-11-11
 */
public class RealTime implements Timeline{

    private static RealTime instance = new RealTime();                  // singleton instance of RealTime
    private static long realTimeStart = System.currentTimeMillis();     // the start of real time

    /*
     *  Private constructor of RealTime for singleton.
     */
    private RealTime() {
        // this constructor intentionally left empty
    }

    /*
     *   Returns the singleton instance of RealTime.
     *   @return RealTime The instance of RealTime.
     */
    public static RealTime getInstance() {
        return instance;
    }

    /*
     *   Reports RealTime's current measure of time.
     *   Makes seemingly redundant call to getRealTime()
     *   in order to implement Timeline interface.
     *   @return long Measure of real time.
     */
    public long getTime() {
        return getRealTime();
    }

    /*
     *   Reports RealTime's current measure of time.
     *   @return long Measure of real time.
     */
    public static long getRealTime() {
        return (System.currentTimeMillis() - realTimeStart);
    }

    /*
     *   Prints the current real timestamp in a readable format
     *   including hours, minutes, seconds, and milliseconds.
     *   @return String A readable version of real time.
     */
    public static String getRealTimeReadable() {
        long current = getRealTime();
        long millis = current % 1000;
        long seconds = (current / 1000) % 60;
        long minutes = (current / 60000) % 60;
        long hours = current / 3600000;
        return hours + ":" + minutes + ":" + seconds + " " + millis + " (real time)";
    }

    public static void main(String[] args) {
        RealTime rt = RealTime.getInstance();
        LocalTime lt = new LocalTime(TimeUnit.MILLISECONDS);
        Scanner scan = new Scanner(System.in);

        while(true) {

            String command = scan.nextLine();
            switch (command) {
                case "time":
                    System.out.println(RealTime.getRealTimeReadable() + "\n" + lt.getLocalTimeReadable());
                    break;
                case "play":
                    System.out.println(lt.play());
                    break;
                case "pause":
                    System.out.println(lt.pause());
                    break;
                case "pausetime":
                    System.out.println(lt.getPausedTimeReadable());
                    break;
                case "reset":
                    lt.reset();
                    break;
            }
        }
    }
}
