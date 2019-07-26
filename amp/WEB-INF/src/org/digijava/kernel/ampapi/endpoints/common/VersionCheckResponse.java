package org.digijava.kernel.ampapi.endpoints.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;

/**
 * @author Octavian Ciubotaru
 */
public class VersionCheckResponse {

    @ApiModelProperty("Is AMP Offline App compatible with AMP?")
    @JsonProperty("amp-offline-compatible")
    private boolean ampOfflineCompatible;

    @ApiModelProperty("AMP version")
    @JsonProperty("amp-version")
    private String ampVersion;

    @ApiModelProperty("Is AMP Offline enabled?")
    @JsonProperty("amp-offline-enabled")
    private boolean ampOfflineEnabled;

    @ApiModelProperty("Latest AMP Offline release")
    @JsonProperty("latest-amp-offline")
    private AmpOfflineRelease latestAmpOffline;
    
    @JsonProperty("server-id")
    private String serverId;
    
    @JsonProperty("server-id-match")
    private boolean serverIdMatch;

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

    public AmpOfflineRelease getLatestAmpOffline() {
        return latestAmpOffline;
    }

    public void setLatestAmpOffline(AmpOfflineRelease latestAmpOffline) {
        this.latestAmpOffline = latestAmpOffline;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public boolean isServerIdMatch() {
        return serverIdMatch;
    }

    public void setServerIdMatch(boolean serverIdMatch) {
        this.serverIdMatch = serverIdMatch;
    }
    
}
