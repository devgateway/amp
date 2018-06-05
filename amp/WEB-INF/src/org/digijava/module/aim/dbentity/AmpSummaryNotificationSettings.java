package org.digijava.module.aim.dbentity;


import java.io.Serializable;

/**
 *
 */
public class AmpSummaryNotificationSettings implements Serializable {

    private static final long serialVersionUID = 1898518611418974995L;

    public AmpSummaryNotificationSettings() {

    }
    private Long ampSummaryNotificationSettings;
    private Boolean notifyManager;
    private Boolean notifyApprover;
    private AmpTeam ampTeam;


    public Boolean getNotifyApprover() {
        return notifyApprover != null ? notifyApprover : false;
    }

    public void setNotifyApprover(Boolean notifyApprover) {
        this.notifyApprover = notifyApprover;
    }

    public Boolean getNotifyManager() {
        return notifyManager != null ? notifyManager : false;
    }

    public void setNotifyManager(Boolean notifyManager) {
        this.notifyManager = notifyManager;
    }

    public Long getAmpSummaryNotificationSettings() {
        return ampSummaryNotificationSettings;
    }

    public void setAmpSummaryNotificationSettings(Long ampSummaryNotificationSettings) {
        this.ampSummaryNotificationSettings = ampSummaryNotificationSettings;
    }

    public AmpTeam getAmpTeam() {
        return ampTeam;
    }

    public void setAmpTeam(AmpTeam ampTeam) {
        this.ampTeam = ampTeam;
    }
}
