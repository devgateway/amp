package org.digijava.module.aim.dbentity;

public abstract class AbstractAuditLogger {

    private Long previousObjectId;

    public Long getPreviousObjectId() {
        return previousObjectId;
    }

    public void setPreviousObjectId(Long previousOjectId) {
        this.previousObjectId = previousOjectId;
    }
}
