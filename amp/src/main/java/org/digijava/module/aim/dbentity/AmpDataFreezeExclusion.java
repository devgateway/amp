package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpDataFreezeExclusion implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 802280723317216053L;
    private Long  ampDataFreezeExclusionId;    
    private AmpActivityVersion activity;
    private AmpDataFreezeSettings dataFreezeEvent;
    
    public AmpActivityVersion getActivity() {
        return activity;
    }
    
    public Long getAmpDataFreezeExclusionId() {
        return ampDataFreezeExclusionId;
    }
    public void setAmpDataFreezeExclusionId(Long ampDataFreezeExclusionId) {
        this.ampDataFreezeExclusionId = ampDataFreezeExclusionId;
    }
    
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }
    public AmpDataFreezeSettings getDataFreezeEvent() {
        return dataFreezeEvent;
    }
    public void setDataFreezeEvent(AmpDataFreezeSettings dataFreezeEvent) {
        this.dataFreezeEvent = dataFreezeEvent;
    }
 
}
