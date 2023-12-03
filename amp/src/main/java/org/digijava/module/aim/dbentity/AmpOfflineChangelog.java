package org.digijava.module.aim.dbentity;

import java.util.Date;

/**
 * @author Octavian Ciubotaru
 */
public class AmpOfflineChangelog {
    
    private Long id;
    private String entityName;
    private String entityId;
    private String operationName;
    private Date operationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public Long getEntityIdAsLong() {
        return Long.parseLong(entityId);
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
}
