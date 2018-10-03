package org.digijava.module.aim.helper;

import org.codehaus.jackson.annotate.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;

/** bean used for holding information about user (first and last name) who modified the activity
 * 
 * @author Viorel Chihai
 *
 */
public class ActivityHistory {

    @JsonProperty(ActivityEPConstants.AMP_ACTIVITY_ID)
    private Long activityId;

    @JsonProperty(ActivityEPConstants.MODIFIED_BY)
    private String modifiedBy;

    @JsonProperty(ActivityEPConstants.MODIFIED_DATE)
    private String modifiedDate;
    
    public ActivityHistory() {
        
    }
    
    public ActivityHistory(Long activityId, String modifiedBy, String modifiedDate) {
        super();
        this.activityId = activityId;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
