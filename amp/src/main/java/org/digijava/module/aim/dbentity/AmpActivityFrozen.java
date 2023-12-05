package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/*
 * if the activity is present here it is frozen under the given event
 * we will use the associated freezing event to freeze a transaction or
 * to enable adding a new one after that (to be used by the validator
 */
public class AmpActivityFrozen implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 4970281121028892306L;
    private Long  ampActivityFrozenid;    
    private AmpActivityGroup activityGroup;
    private AmpDataFreezeSettings dataFreezeEvent;
    // the frozen field is to be able to freeze and unfreeze and activity 
    // under a given period
    private Boolean frozen;
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
