package org.digijava.module.aim.dbentity;

import java.util.Date;

/**
 * @author Octavian Ciubotaru
 */
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "amp_offline_changelog")
public class AmpOfflineChangelog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_offline_changelog_seq")
    @SequenceGenerator(name = "amp_offline_changelog_seq", sequenceName = "amp_offline_changelog_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "entity_name", length = 50, nullable = false)
    private String entityName;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "operation_name", length = 20, nullable = false)
    private String operationName;

    @Column(name = "operation_time", nullable = false)
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
