package org.digijava.kernel.ampregistry;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Describes an AMP Installation. Used by AMP Registry for AMP discovery.
 *
 * @author Octavian Ciubotaru
 */
public class AmpInstallation {

    private Long id;

    private Map<String, String> name;

    private String iso2;

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

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
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
