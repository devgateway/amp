package org.digijava.kernel.services.sync.model;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Octavian Ciubotaru
 */
public class SystemDiff {

    @JsonProperty @JsonSerialize(using = ISO8601TimeStampSerializer.class)
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

    @JsonProperty
    private ListDiff<Long> contacts;
    
    @JsonProperty("contact-possible-values-fields")
    private List<String> contactPossibleValuesFields;
    
    @JsonProperty
    private ListDiff<String> resources;
    
    @JsonProperty("resource-possible-values-fields")
    private List<String> resourcePossibleValuesFields;
    
    @JsonProperty("common-possible-values-fields")
    private List<String> commonPossibleValuesFields;

    @JsonProperty
    private boolean translations;

    @JsonProperty("exchange-rates")
    private boolean exchangeRates;

    @JsonProperty("feature-manager")
    private boolean featureManager;

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

    public void setContactPossibleValuesFields(List<String> contactPossibleValuesFields) {
        this.contactPossibleValuesFields = contactPossibleValuesFields;
    }
    
    public void setResourcePossibleValuesFields(List<String> resourcePossibleValuesFields) {
        this.resourcePossibleValuesFields = resourcePossibleValuesFields;
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
    
}
