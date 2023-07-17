package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_DATA_FREEZE_EXCLUSION")
public class AmpDataFreezeExclusion implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 802280723317216053L;
    @Id
    @GeneratedValue(generator = "ampDataFreezeExclusionSeq")
    @SequenceGenerator(name = "ampDataFreezeExclusionSeq", sequenceName = "AMP_DATA_FREEZE_EXCLUSION_seq", allocationSize = 1)
    @Column(name = "amp_data_freeze_exclusion_id")
    private Long ampDataFreezeExclusionId;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false, referencedColumnName = "amp_activity_id")
    private AmpActivityVersion activity;

    @ManyToOne
    @JoinColumn(name = "data_freeze_event_id", nullable = false, referencedColumnName = "id")
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
