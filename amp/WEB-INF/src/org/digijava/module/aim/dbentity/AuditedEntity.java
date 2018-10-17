package org.digijava.module.aim.dbentity;

public abstract class AuditedEntity {

    private Long originalObjectId;

    public Long getOriginalObjectId() {
        return originalObjectId;
    }

    public void setOriginalObjectId(Long originalObjectId) {
        this.originalObjectId = originalObjectId;
    }
}
