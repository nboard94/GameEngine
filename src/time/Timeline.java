package time;

/*
 *   Interface for dealing with both RealTime and LocalTime.
 *
 *   @author Nicholas Board
 *   @since 2018-11-11
 */
public interface Timeline {

    /*
     *   Reports the current Timeline's measure of time.
     *   @return long Measure of time.
     */
    long getTime();
}
