package org.digijava.module.message.dbentity;
import javax.persistence.*;

@Entity
@Table(name = "AMP_MESSAGE_SETTINGS")
public class AmpMessageSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_message_settings_seq")
    @SequenceGenerator(name = "amp_message_settings_seq", sequenceName = "AMP_MESSAGE_SETTINGS_seq", allocationSize = 1)
    @Column(name = "message_settings_Id")
    private Long id;

    @Column(name = "msg_refresh_time")
    private Long msgRefreshTime;

    @Column(name = "msg_storage_per_msg_type")
    private Long msgStoragePerMsgType;

    @Column(name = "days_for_advance_alerts_warnin")
    private Long daysForAdvanceAlertsWarnings;

    @Column(name = "is_emailable")
    private Long emailMsgs;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getMsgRefreshTime() {
        return msgRefreshTime;
    }
    public void setMsgRefreshTime(Long msgRefreshTime) {
        this.msgRefreshTime = msgRefreshTime;
    }
    public Long getMsgStoragePerMsgType() {
        return msgStoragePerMsgType;
    }
    public void setMsgStoragePerMsgType(Long msgStoragePerMsgType) {
        this.msgStoragePerMsgType = msgStoragePerMsgType;
    }
    public Long getDaysForAdvanceAlertsWarnings() {
        return daysForAdvanceAlertsWarnings;
    }
    public void setDaysForAdvanceAlertsWarnings(Long daysForAdvanceAlertsWarnings) {
        this.daysForAdvanceAlertsWarnings = daysForAdvanceAlertsWarnings;
    }   
    public Long getEmailMsgs() {
        return emailMsgs;
    }
    public void setEmailMsgs(Long emailMsgs) {
        this.emailMsgs = emailMsgs;
    }
    
    

}
