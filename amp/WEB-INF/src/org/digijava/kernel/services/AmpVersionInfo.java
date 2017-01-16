package org.digijava.kernel.services;

/**
 * @author Octavian Ciubotaru
 */
public class AmpVersionInfo {

    private String ampVersion;
    private String releaseDate;
    private String buildSource;
    private String buildDate;

    public String getAmpVersion() {
        return ampVersion;
    }

    public void setAmpVersion(String ampVersion) {
        this.ampVersion = ampVersion;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBuildSource() {
        return buildSource;
    }

    public void setBuildSource(String buildSource) {
        this.buildSource = buildSource;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }
}
