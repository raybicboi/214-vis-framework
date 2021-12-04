package edu.cs.visframework.utils;

/**
 * Stopwatch class to be used as a timer.
 *
 * @author Shicheng Huang
 */
public class Stopwatch {

    /**
     * Start time.
     */
    private long start;

    /**
     * Construct to set current time in milliseconds to start time.
     */
    public Stopwatch() {
        start = System.currentTimeMillis();
    }

    /**
     * Returns time (in milliseconds) since this object was created.
     * @return time in milliseconds
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return now - start;
    }

    public void reset() {
        start = System.currentTimeMillis();
    }

}
