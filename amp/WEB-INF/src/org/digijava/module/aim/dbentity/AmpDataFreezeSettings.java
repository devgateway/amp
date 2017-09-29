package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AmpDataFreezeSettings implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2203566029781790548L;
    private Long ampDataFreezeSettingsId;
    /**
     * if true feature enabled
     */
    private Boolean enabled = Boolean.FALSE;
    private Boolean executed=Boolean.FALSE;
    private Integer gracePeriod;
    private Date freezingDate;
    private Date openPeriodStart;
    private Date openPeriodEnd;
    private Boolean sendNotification = Boolean.FALSE;
    private FreezeOptions freezeOption;
    private Integer notificationDays;

    public enum FreezeOptions {
        ENTIRE_ACTIVITY, FUNDING
    };

    private String filters;

    public Long getAmpDataFreezeSettingsId() {
        return ampDataFreezeSettingsId;
    }

    public void setAmpDataFreezeSettingsId(Long ampDataFreezeSettingsId) {
        this.ampDataFreezeSettingsId = ampDataFreezeSettingsId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public Date getFreezingDate() {
        return freezingDate;
    }

    public void setFreezingDate(Date freezingDate) {
        this.freezingDate = freezingDate;
    }

    public Date getOpenPeriodStart() {
        return openPeriodStart;
    }

    public void setOpenPeriodStart(Date openPeriodStart) {
        this.openPeriodStart = openPeriodStart;
    }

    public Date getOpenPeriodEnd() {
        return openPeriodEnd;
    }

    public void setOpenPeriodEnd(Date openPeriodEnd) {
        this.openPeriodEnd = openPeriodEnd;
    }

    public Boolean getSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public FreezeOptions getFreezeOption() {
        return freezeOption;
    }

    public void setFreezeOption(FreezeOptions freezeOption) {
        this.freezeOption = freezeOption;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Integer getNotificationDays() {
        return notificationDays;
    }

    public void setNotificationDays(Integer notificationDays) {
        this.notificationDays = notificationDays;
    }

}
