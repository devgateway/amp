package org.dgfoundation.amp.newreports;

import java.util.Objects;

public class ReportWarning implements Comparable<ReportWarning> {
    
    /**
     * an entityId which is not linked to a specific entity, but is instead relevant for the whole report
     */
    public final static long ENTITY_GENERIC = -1;
    
    public final long entityId;
    public final String message;

    public ReportWarning(String message) {
        this(ENTITY_GENERIC, message);
    }
    
    public ReportWarning(long entityId, String message) {
        this.entityId = entityId;
        this.message = Objects.requireNonNull(message);
    }

    public long getEntityId() {
        return entityId;
    }

    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return String.format("entityId: %d, message: %s", entityId, message);
    }
    
    @Override
    public int compareTo(ReportWarning oth) {
        int delta = Long.compare(entityId, oth.entityId);
        if (delta != 0) return 0;
        
        delta = message.compareTo(oth.message);
        return delta;
    }
    
}
