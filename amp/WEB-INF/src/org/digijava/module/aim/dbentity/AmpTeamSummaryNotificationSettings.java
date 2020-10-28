package org.digijava.module.aim.dbentity;


import java.io.Serializable;

/**
 *
 */
public class AmpTeamSummaryNotificationSettings implements Serializable {

    private static final long serialVersionUID = 1898518611418974995L;

    public AmpTeamSummaryNotificationSettings() {

    }
    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpTeam getAmpTeam() {
        return ampTeam;
    }

    public void setAmpTeam(AmpTeam ampTeam) {
        this.ampTeam = ampTeam;
    }
}
