package org.digijava.module.aim.dbentity;

/**
 * TODO document
 *
 * Ideally we should have been subclassing from something like org.springframework.data.jpa.domain.AbstractPersistable
 * and avoid defining an abstract getId method here.
 */
public abstract class AuditedEntity {

    private Long originalObjectId;

    public Long getOriginalObjectId() {
        return originalObjectId;
    }

    public void setOriginalObjectId(Long originalObjectId) {
        this.originalObjectId = originalObjectId;
    }

    public abstract Long getId();
}
