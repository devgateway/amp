package org.dgfoundation.amp.ar.amp212;

public class Pair {
    public final long id;
    public final Double perc;
    
    public Pair(long id, Double perc) {
        this.id = id;
        this.perc = perc;
    }
    
    @Override public String toString() {
        return String.format("(%d, %s)", id, perc);
    }
}
