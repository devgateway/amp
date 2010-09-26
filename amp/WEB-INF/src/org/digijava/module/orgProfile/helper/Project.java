package org.digijava.module.orgProfile.helper;

import java.util.List;

/**
 * Project helper bean.
 * @author medea
 */
public class Project {
    // truncated title, 14 characters only
    private String title;
    private String fullTitle;
    private String amount;
    private List<String> sectorNames;
    private Long activityId;
    private  String disbAmount;

    public String getDisbAmount() {
        return disbAmount;
    }

    public void setDisbAmount(String disbAmount) {
        this.disbAmount = disbAmount;
    }

    public List<String> getSectorNames() {
        return sectorNames;
    }

    public void setSectorNames(List<String> sectorNames) {
        this.sectorNames = sectorNames;
    }


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
     public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }
}
