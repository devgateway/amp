package org.digijava.kernel.ampapi.endpoints.sync;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncRequest {

    @ApiModelProperty(value = "sync only the activities visible to specified users", required = true)
    @JsonProperty("user-ids")
    private List<Long> userIds;

    @ApiModelProperty("amp ids of activities currently known to the client")
    @JsonProperty("amp-ids")
    private List<String> ampIds;

    @ApiModelProperty(value = "time of the last sync", example = "2016-06-01T01:00:00.999+0000")
    @JsonProperty("last-sync-time")
    private Date lastSyncTime;
    
    @ApiModelProperty(value = "possible values fields currently known to the client")
    @JsonProperty(value = "activity-possible-values-fields")
    private List<String> activityPossibleValuesFields;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<String> getAmpIds() {
        return ampIds;
    }

    public void setAmpIds(List<String> ampIds) {
        this.ampIds = ampIds;
    }

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }
    
    public List<String> getActivityPossibleValuesFields() {
        return activityPossibleValuesFields;
    }
    
    public void setActivityPossibleValuesFields(List<String> activityPossibleValuesFields) {
        this.activityPossibleValuesFields = activityPossibleValuesFields;
    }
}
