package org.digijava.kernel.services.sync.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;

/**
 * @author Octavian Ciubotaru
 */
public class SystemDiff {

    @JsonProperty
    private Date timestamp;

    @JsonProperty("global-settings")
    private boolean globalSettings;

    @JsonProperty
    private boolean workspaces;

    @JsonProperty("workspace-members")
    private IncrementalListDiff<Long> workspaceMembers;

    @JsonProperty
    private IncrementalListDiff<Long> users;

    @JsonProperty
    private IncrementalListDiff<String> activities;

    @JsonProperty
    private ListDiff<String> translations;

    public void updateTimestamp(Date timestamp) {
        if (this.timestamp == null || this.timestamp.before(timestamp)) {
            this.timestamp = timestamp;
        }
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setGlobalSettings(boolean globalSettings) {
        this.globalSettings = globalSettings;
    }

    public void setWorkspaces(boolean workspaces) {
        this.workspaces = workspaces;
    }

    public void setWorkspaceMembers(IncrementalListDiff<Long> workspaceMembers) {
        this.workspaceMembers = workspaceMembers;
    }

    public void setUsers(IncrementalListDiff<Long> users) {
        this.users = users;
    }

    public void setActivities(IncrementalListDiff<String> activities) {
        this.activities = activities;
    }

    public void setTranslations(ListDiff<String> translations) {
        this.translations = translations;
    }

    public String getTimestamp() {
        if (timestamp != null) {
            return new SimpleDateFormat(InterchangeUtils.ISO8601_DATE_FORMAT).format(timestamp);
        } else {
            return null;
        }
    }
}
