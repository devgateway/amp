package org.digijava.module.message.dbentity;

public class AmpMessageSettings {
    
    private Long id;
    private Long msgRefreshTime;
    private Long msgStoragePerMsgType;
    private Long daysForAdvanceAlertsWarnings;  
    private Long emailMsgs;                     //will messages be emailable or not
    
    
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
