package org.digijava.module.aim.util.time;

import java.util.Hashtable;

import org.apache.log4j.Logger;

public class StopWatch {
    private static Logger logger = Logger.getLogger(StopWatch.class);
    
    private static Hashtable<String, StopWatch> watches     = new Hashtable<String, StopWatch>();
    
    public static void reset(String watchName) {
        StopWatch.watches.remove(watchName);
    }
    
    public static long next(String watchName, boolean logDifference ) {
        return StopWatch.next(watchName, logDifference, null);
    }
    
    public static long next(String watchName, boolean logDifference, String marker ) {
        StopWatch watch     = StopWatch.watches.get(watchName);
        if ( watch == null ) {
            watch   = new StopWatch(watchName);
        }
        else
            watch.next();
        
        if ( logDifference ) {
            String info = "Stopwatch " + watchName + " at iteration " + watch.getIteration() + " with total time " + watch.totalDifference() 
                        + " has time: " + watch.difference();
            if ( marker != null && marker.length() != 0) {
                info += "( " + marker + " )";
            }
            logger.info( info );
        }
        return watch.difference();
        
        
    }
    
    
    
    private String name;
    private long startTime;
    private long lastTime;
    private long prevTime;
    private int iteration;
    
    private StopWatch(String name) {
        this.name   = name;
        this.startTime  = System.currentTimeMillis();
        this.lastTime   = this.startTime;
        this.prevTime   = this.startTime;
        this.iteration  = 0;
        StopWatch.watches.put(name, this);
    }
    public void next() {
        this.prevTime   = this.lastTime;
        this.lastTime   = System.currentTimeMillis();
        this.iteration++;
    }
    
    public long difference () {
        return this.lastTime - this.prevTime;
    }
    public long totalDifference () {
        return this.lastTime - this.startTime;
    }

    private int getIteration() {
        return iteration;
    }

    private void setIteration(int iteration) {
        this.iteration = iteration;
    }
    
    
}
