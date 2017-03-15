package org.digijava.kernel.services.sync.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

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
    private ListDiff<Long> workspaceMembers;

    @JsonProperty
    private ListDiff<Long> users;

    @JsonProperty
    private ListDiff<String> activities;

    @JsonProperty
    private boolean translations;

    public void updateTimestamp(Date timestamp) {
        if (this.timestamp == null || (timestamp != null && this.timestamp.before(timestamp))) {
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

    public void setWorkspaceMembers(ListDiff<Long> workspaceMembers) {
        this.workspaceMembers = workspaceMembers;
    }

    public void setUsers(ListDiff<Long> users) {
        this.users = users;
    }

    public void setActivities(ListDiff<String> activities) {
        this.activities = activities;
    }

    public void setTranslations(boolean translations) {
        this.translations = translations;
    }

    public String getTimestamp() {
        if (timestamp != null) {
            return new SimpleDateFormat(EPConstants.ISO8601_DATE_FORMAT).format(timestamp);
        } else {
            return null;
        }
    }
}
