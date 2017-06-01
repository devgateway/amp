package org.digijava.kernel.ampapi.endpoints.common;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class VersionCheckResponse {

    @JsonProperty("amp-offline-compatible")
    private boolean ampOfflineCompatible;

    @JsonProperty("amp-version")
    private String ampVersion;

    @JsonProperty("amp-offline-enabled")
    private boolean ampOfflineEnabled;

    public boolean isAmpOfflineCompatible() {
        return ampOfflineCompatible;
    }

    public void setAmpOfflineCompatible(boolean ampOfflineCompatible) {
        this.ampOfflineCompatible = ampOfflineCompatible;
    }

    public String getAmpVersion() {
        return ampVersion;
    }

    public void setAmpVersion(String ampVersion) {
        this.ampVersion = ampVersion;
    }

    public boolean getAmpOfflineEnabled() {
        return ampOfflineEnabled;
    }

    public void setAmpOfflineEnabled(boolean ampOfflineEnabled) {
        this.ampOfflineEnabled = ampOfflineEnabled;
    }
}
