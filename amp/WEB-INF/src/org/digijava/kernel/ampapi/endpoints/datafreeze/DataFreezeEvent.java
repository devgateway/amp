package org.digijava.kernel.ampapi.endpoints.datafreeze;

import org.digijava.module.aim.dbentity.AmpDataFreezeSettings.FreezeOptions;

public class DataFreezeEvent {
    private Long id;
    private Long cid;
    private Boolean enabled = Boolean.TRUE;
    private Integer gracePeriod;
    private String freezingDate;
    private String openPeriodStart;
    private String openPeriodEnd;
    private Boolean sendNotification = Boolean.FALSE;
    private FreezeOptions freezeOption;
    private String filters;
    private Integer count; // number of affected activities
    private Integer notificationDays;
    private Boolean executed = Boolean.FALSE;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public DataFreezeEvent() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public String getFreezingDate() {
        return freezingDate;
    }

    public void setFreezingDate(String freezingDate) {
        this.freezingDate = freezingDate;
    }

    public String getOpenPeriodStart() {
        return openPeriodStart;
    }

    public void setOpenPeriodStart(String openPeriodStart) {
        this.openPeriodStart = openPeriodStart;
    }

    public String getOpenPeriodEnd() {
        return openPeriodEnd;
    }

    public void setOpenPeriodEnd(String openPeriodEnd) {
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

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
    
    public Integer getNotificationDays() {
        return notificationDays;
    }

    public void setNotificationDays(Integer notificationDays) {
        this.notificationDays = notificationDays;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }
}
