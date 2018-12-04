package org.digijava.module.aim.dbentity;

public interface AuditableEntity {
    
    AuditableEntity getParent();
    
    default void touch() {
        if (getParent() != null) {
            getParent().touch();
        }
    }
    
}
