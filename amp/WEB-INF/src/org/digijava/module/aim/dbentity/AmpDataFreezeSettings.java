package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

@Entity
@Table(name = "AMP_DATA_FREEZE_SETTINGS")
public class AmpDataFreezeSettings implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2203566029781790548L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_data_freeze_settings_seq_generator")
    @SequenceGenerator(name = "amp_data_freeze_settings_seq_generator", sequenceName = "AMP_DATA_FREEZE_SETTINGS_seq", allocationSize = 1)
    @Column(name = "id")
    private Long ampDataFreezeSettingsId;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "executed")
    private Boolean executed;

    @Column(name = "grace_period")
    @ApiModelProperty(example = "20")
    private Integer gracePeriod;

    @Column(name = "freezing_date")
    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-05-15")
    private Date freezingDate;

    @Column(name = "open_period_start")
    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-06-18")
    private Date openPeriodStart;

    @Column(name = "open_period_end")
    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-09-20")
    private Date openPeriodEnd;

    @Column(name = "send_notification")
    private Boolean sendNotification;

    @Column(name = "notification_days")
    private Integer notificationDays;

    @Column(name = "filters")
    @ApiModelProperty(value = "filters used to filter activities",
            example = "\"type-of-assistance\":[80],\"status\":[64],\"primary-sector\":[7610]")
    private String filters;

    @Enumerated(EnumType.STRING)
    @Column(name = "freeze_option")
    @ApiModelProperty(value = "freeze option")
    private FreezeOptions freezeOption;

    // Constructors, getters, and setters





    public enum FreezeOptions {
        ENTIRE_ACTIVITY, FUNDING
    }

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
