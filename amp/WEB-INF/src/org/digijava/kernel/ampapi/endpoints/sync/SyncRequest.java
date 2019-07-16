package org.digijava.kernel.ampapi.endpoints.sync;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncRequest {

    @JsonProperty("user-ids")
    private List<Long> userIds;

    @JsonProperty("amp-ids")
    private List<String> ampIds;

    @JsonProperty("last-sync-time")
    private Date lastSyncTime;

    public List<Long> getUserIds() {
        return userIds;
    }

    public List<String> getAmpIds() {
        return ampIds;
    }

    public Date getLastSyncTime() {
        return lastSyncTime;
    }
}
