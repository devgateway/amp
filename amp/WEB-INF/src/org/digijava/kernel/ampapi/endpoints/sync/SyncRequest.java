package org.digijava.kernel.ampapi.endpoints.sync;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.activity.APIWorkspaceMemberFieldList;

import java.util.Date;
import java.util.List;

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
    
    @ApiModelProperty(value = "activity possible values fields currently known to the client")
    @JsonProperty(value = "activity-possible-values-fields")
    private List<String> activityPossibleValuesFields;

    @ApiModelProperty(value = "contact possible values fields currently known to the client")
    @JsonProperty(value = "contact-possible-values-fields")
    private List<String> contactPossibleValuesFields;

    @ApiModelProperty(value = "resource possible values fields currently known to the client")
    @JsonProperty(value = "resource-possible-values-fields")
    private List<String> resourcePossibleValuesFields;

    @ApiModelProperty(value = "common possible values fields currently known to the client")
    @JsonProperty(value = "common-possible-values-fields")
    private List<String> commonPossibleValuesFields;
    
    @ApiModelProperty(value = "activity fields currently known to the client")
    @JsonProperty(value = "activity-fields")
    private List<APIWorkspaceMemberFieldList> activityFields;
    
    @ApiModelProperty(value = "contact fields currently known to the client")
    @JsonProperty(value = "contact-fields")
    private List<APIWorkspaceMemberFieldList> contactFields;
    
    @ApiModelProperty(value = "resource fields currently known to the client")
    @JsonProperty(value = "resource-fields")
    private List<APIWorkspaceMemberFieldList> resourceFields;

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

    public List<String> getContactPossibleValuesFields() {
        return contactPossibleValuesFields;
    }

    public void setContactPossibleValuesFields(List<String> contactPossibleValuesFields) {
        this.contactPossibleValuesFields = contactPossibleValuesFields;
    }

    public List<String> getResourcePossibleValuesFields() {
        return resourcePossibleValuesFields;
    }

    public void setResourcePossibleValuesFields(List<String> resourcePossibleValuesFields) {
        this.resourcePossibleValuesFields = resourcePossibleValuesFields;
    }

    public List<String> getCommonPossibleValuesFields() {
        return commonPossibleValuesFields;
    }

    public void setCommonPossibleValuesFields(List<String> commonPossibleValuesFields) {
        this.commonPossibleValuesFields = commonPossibleValuesFields;
    }

    public List<APIWorkspaceMemberFieldList> getActivityFields() {
        return activityFields;
    }
    
    public void setActivityFields(List<APIWorkspaceMemberFieldList> activityFields) {
        this.activityFields = activityFields;
    }
    
    public List<APIWorkspaceMemberFieldList> getContactFields() {
        return contactFields;
    }
    
    public void setContactFields(List<APIWorkspaceMemberFieldList> contactFields) {
        this.contactFields = contactFields;
    }
    
    public List<APIWorkspaceMemberFieldList> getResourceFields() {
        return resourceFields;
    }
    
    public void setResourceFields(List<APIWorkspaceMemberFieldList> resourceFields) {
        this.resourceFields = resourceFields;
    }
}
