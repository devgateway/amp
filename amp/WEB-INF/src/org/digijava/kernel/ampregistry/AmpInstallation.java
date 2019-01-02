package org.digijava.kernel.ampregistry;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes an AMP Installation. Used by AMP Registry for AMP discovery.
 *
 * @author Octavian Ciubotaru
 */
public class AmpInstallation {

    private Long id;

    private Map<String, String> name;

    private String iso2;

    @JsonProperty("server-id")
    private String serverId;

    private List<String> urls;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public AmpInstallation() {
    }

    public AmpInstallation(Long id, Map<String, String> name, String iso2, String serverId,
            List<String> urls) {
        this.id = id;
        this.name = name;
        this.iso2 = iso2;
        this.serverId = serverId;
        this.urls = urls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AmpInstallation that = (AmpInstallation) o;
        return Objects.equals(name, that.name)
                && Objects.equals(iso2, that.iso2)
                && Objects.equals(urls, that.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, iso2, urls);
    }
}
