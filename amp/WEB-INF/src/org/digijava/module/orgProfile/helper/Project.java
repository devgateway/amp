
package org.digijava.module.orgProfile.helper;

import java.util.Set;
import org.digijava.module.aim.dbentity.AmpActivitySector;

/**
 * Project helper bean.
 * @author medea
 */
public class Project {
   private String title;
   private String amount;
   private Set<AmpActivitySector> sectors;
   private Long activityId;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Set<AmpActivitySector> getSectors() {
        return sectors;
    }

    public void setSectors(Set<AmpActivitySector> sectors) {
        this.sectors = sectors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    }
