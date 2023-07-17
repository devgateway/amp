package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/*
 * if the activity is present here it is frozen under the given event
 * we will use the associated freezing event to freeze a transaction or
 * to enable adding a new one after that (to be used by the validator
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_ACTIVITY_FROZEN")
public class AmpActivityFrozen implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 4970281121028892306L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ACTIVITY_FROZEN_seq")
    @SequenceGenerator(name = "AMP_ACTIVITY_FROZEN_seq", sequenceName = "AMP_ACTIVITY_FROZEN_seq", allocationSize = 1)    @Column(name = "amp_data_freeze_exclusion_id")
    private Long ampActivityFrozenid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_activity_group_id", nullable = false, insertable = false, updatable = false)
    private AmpActivityGroup activityGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_freeze_event_id", nullable = false, insertable = false, updatable = false)
    private AmpDataFreezeSettings dataFreezeEvent;

    @Column(name = "frozen")
    private Boolean frozen;

    @Column(name = "deleted")
    private Boolean deleted;

    public AmpActivityFrozen(){
        
    }
    public Long getAmpActivityFrozenid() {
        return ampActivityFrozenid;
    }
    public void setAmpActivityFrozenid(Long ampActivityFrozenid) {
        this.ampActivityFrozenid = ampActivityFrozenid;
    }
    public AmpActivityGroup getActivityGroup() {
        return activityGroup;
    }
    public void setActivityGroup(AmpActivityGroup activityGroup) {
        this.activityGroup = activityGroup;
    }
    public AmpDataFreezeSettings getDataFreezeEvent() {
        return dataFreezeEvent;
    }
    public void setDataFreezeEvent(AmpDataFreezeSettings dataFreezeEvent) {
        this.dataFreezeEvent = dataFreezeEvent;
    }
    public Boolean getFrozen() {
        return frozen;
    }
    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
}
