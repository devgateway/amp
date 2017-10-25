package org.digijava.module.aim.helper ;

import java.util.Collection;

public class AllActivities
{
    Long activityId;
    String activityName;
    Collection allMEIndicators;
    /**
     * @return Returns the activityId.
     */
    public Long getActivityId() {
        return activityId;
    }
    /**
     * @param activityId The activityId to set.
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    /**
     * @return Returns the activityName.
     */
    public String getActivityName() {
        return activityName;
    }
    /**
     * @param activityName The activityName to set.
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    /**
     * @return Returns the allMEIndicators.
     */
    public Collection getAllMEIndicators() {
        return allMEIndicators;
    }
    /**
     * @param allMEIndicators The allMEIndicators to set.
     */
    public void setAllMEIndicators(Collection allMEIndicators) {
        this.allMEIndicators = allMEIndicators;
    }
}
