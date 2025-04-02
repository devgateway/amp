package org.digijava.kernel.ampapi.endpoints.datafreeze;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601DateSerializer;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings.FreezeOptions;

public class DataFreezeEvent {
    
    @ApiModelProperty("client side id")
    private Long id;
    
    private Long cid;
    
    private Boolean enabled = Boolean.TRUE;
    
    @ApiModelProperty(example = "30")
    private Integer gracePeriod;
    
    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-06-18")
    private Date freezingDate;
    
    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-06-18")
    private Date openPeriodStart;
    
    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-07-18")
    private Date openPeriodEnd;
    
    private Boolean sendNotification = Boolean.FALSE;
    
    @ApiModelProperty(value = "freeze option")
    private FreezeOptions freezeOption;
    
    @ApiModelProperty(value = "filters used to filter activities",
            example = "\"type-of-assistance\":[80],\"status\":[64],\"primary-sector\":[7610]")
    private String filters;
    
    @ApiModelProperty("number of affected activities")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer count;
    
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
