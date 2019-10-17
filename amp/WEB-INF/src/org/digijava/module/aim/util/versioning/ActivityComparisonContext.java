package org.digijava.module.aim.util.versioning;

public class ActivityComparisonContext {

    private String siteName;
    private String lang;
    private Long siteId;

    public ActivityComparisonContext(Long siteId, String siteName, String lang) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.lang = lang;
    }
    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}