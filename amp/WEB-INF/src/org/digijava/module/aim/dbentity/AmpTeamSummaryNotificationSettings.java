package org.digijava.module.aim.dbentity;


import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "amp_team_summary_notification_settings")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpTeamSummaryNotificationSettings implements Serializable {

    private static final long serialVersionUID = 1898518611418974995L;

    public AmpTeamSummaryNotificationSettings() {

    }
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private AmpTeam ampTeam;

    @Column(name = "notify_manager")
    private Boolean notifyManager;

    @Column(name = "notify_approver")
    private Boolean notifyApprover;
    private Long id;

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
