package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.services.AmpOfflineVersion;

/**
 * @author Octavian Ciubotaru
 */
public class AmpOfflineCompatibleVersionRange {

    private Long id;

    @JsonProperty("from-version")
    private AmpOfflineVersion fromVersion;

    @JsonProperty("to-version")
    private AmpOfflineVersion toVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpOfflineVersion getFromVersion() {
        return fromVersion;
    }

    public void setFromVersion(AmpOfflineVersion fromVersion) {
        this.fromVersion = fromVersion;
    }

    public AmpOfflineVersion getToVersion() {
        return toVersion;
    }

    public void setToVersion(AmpOfflineVersion toVersion) {
        this.toVersion = toVersion;
    }
}
