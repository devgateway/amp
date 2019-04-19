package org.digijava.kernel.services.sync.model;

import java.util.Date;
import java.util.List;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601TimeStampSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Octavian Ciubotaru
 */
public class SystemDiff {

    @JsonProperty
    @JsonSerialize(using = ISO8601TimeStampSerializer.class)
    private Date timestamp;

    @JsonProperty("global-settings")
    private boolean globalSettings;

    @JsonProperty
    private boolean workspaces;

    @JsonProperty("workspace-settings")
    private boolean workspaceSettings;

    @JsonProperty("workspace-members")
    private ListDiff<Long> workspaceMembers;
    
    @JsonProperty("map-tiles")
    private boolean mapTiles;
    
    @JsonProperty("locators")
    private boolean locators;
    
    @JsonProperty
    private ListDiff<Long> calendars;
    
    @JsonProperty
    private ListDiff<Long> users;

    @JsonProperty
    private ListDiff<String> activities;

    @JsonProperty("activity-possible-values-fields")
    private List<String> activityPossibleValuesFields;
    
    @JsonProperty("activity-fields-structural-changes")
    private boolean activityFieldsStructuralChanges;

    @JsonProperty
    private ListDiff<Long> contacts;
    
    @JsonProperty("contact-possible-values-fields")
    private List<String> contactPossibleValuesFields;
    
    @JsonProperty("contact-fields-structural-changes")
    private boolean contactFieldsStructuralChanges;
    
    @JsonProperty
    private ListDiff<String> resources;
    
    @JsonProperty("resource-possible-values-fields")
    private List<String> resourcePossibleValuesFields;
    
    @JsonProperty("resource-fields-structural-changes")
    private boolean resourceFieldsStructuralChanges;
    
    @JsonProperty("common-possible-values-fields")
    private List<String> commonPossibleValuesFields;

    @JsonProperty
    private boolean translations;

    @JsonProperty("exchange-rates")
    private boolean exchangeRates;

    @JsonProperty("feature-manager")
    private boolean featureManager;
    
    /**
     * Set when field definitions any entity changed.
     */
    private boolean fields;

    public void updateTimestamp(Date timestamp) {
        if (this.timestamp == null || (timestamp != null && this.timestamp.before(timestamp))) {
            this.timestamp = timestamp;
        }
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setGlobalSettings(boolean globalSettings) {
        this.globalSettings = globalSettings;
    }

    public void setWorkspaces(boolean workspaces) {
        this.workspaces = workspaces;
    }

    public void setWorkspaceSettings(boolean workspaceSettings) {
        this.workspaceSettings = workspaceSettings;
    }

    public void setWorkspaceMembers(ListDiff<Long> workspaceMembers) {
        this.workspaceMembers = workspaceMembers;
    }

    public void setUsers(ListDiff<Long> users) {
        this.users = users;
    }

    public void setActivities(ListDiff<String> activities) {
        this.activities = activities;
    }

    public void setContacts(ListDiff<Long> contacts) {
        this.contacts = contacts;
    }
    
    public void setResources(ListDiff<String> resources) {
        this.resources = resources;
    }

    public void setTranslations(boolean translations) {
        this.translations = translations;
    }

    public void setActivityPossibleValuesFields(List<String> activityPossibleValuesFields) {
        this.activityPossibleValuesFields = activityPossibleValuesFields;
    }
    
    public void setActivityFieldsStructuralChanges(boolean activityFieldsStructuralChanges) {
        this.activityFieldsStructuralChanges = activityFieldsStructuralChanges;
    }
    
    public boolean isActivityFieldsStructuralChanges() {
        return activityFieldsStructuralChanges;
    }
    
    public void setContactPossibleValuesFields(List<String> contactPossibleValuesFields) {
        this.contactPossibleValuesFields = contactPossibleValuesFields;
    }
    
    public void setContactFieldsStructuralChanges(boolean contactFieldsStructuralChanges) {
        this.contactFieldsStructuralChanges = contactFieldsStructuralChanges;
    }
    
    public boolean isContactFieldsStructuralChanges() {
        return contactFieldsStructuralChanges;
    }
    
    public void setResourcePossibleValuesFields(List<String> resourcePossibleValuesFields) {
        this.resourcePossibleValuesFields = resourcePossibleValuesFields;
    }
    
    public void setResourceFieldsStructuralChanges(boolean resourceFieldsStructuralChanges) {
        this.resourceFieldsStructuralChanges = resourceFieldsStructuralChanges;
    }
    
    public boolean isResourceFieldsStructuralChanges() {
        return resourceFieldsStructuralChanges;
    }
    
    public void setCommonPossibleValuesFields(List<String> commonPossibleValuesFields) {
        this.commonPossibleValuesFields = commonPossibleValuesFields;
    }

    public void setExchangeRates(boolean exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public void setFeatureManager(boolean featureManager) {
        this.featureManager = featureManager;
    }
    
    public void setMapTiles(boolean mapTiles) {
        this.mapTiles = mapTiles;
    }
    
    public void setLocators(boolean locators) {
        this.locators = locators;
    }

    public void setCalendars(ListDiff<Long> calendars) {
        this.calendars = calendars;
    }

    public boolean isFields() {
        return fields;
    }

    public void setFields(boolean fields) {
        this.fields = fields;
    }
}
