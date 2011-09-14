package org.digijava.module.gis.dbentity;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: George
 * Date: 4/2/11
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class GisSettings implements Serializable {
    public static int SECTOR_SCHEME_CONFIGURABLE = 0;
    public static int SECTOR_SCHEME_AUTOMATIC = 1;

    private String siteId;
    private String instanceId;

    private int sectorSchemeFilterMode;

    private String selectedPreset;

    public GisSettings() {

    }

    public GisSettings(String siteId, String instanceId) {
        this.siteId = siteId;
        this.instanceId = instanceId;

    }

    public String getSelectedPreset() {
        return selectedPreset;
    }

    public void setSelectedPreset(String selectedPreset) {
        this.selectedPreset = selectedPreset;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public int getSectorSchemeFilterMode() {
        return sectorSchemeFilterMode;
    }

    public void setSectorSchemeFilterMode(int sectorSchemeFilterMode) {
        this.sectorSchemeFilterMode = sectorSchemeFilterMode;
    }

    public boolean equals(Object obj) {
        return this.siteId.equals(((GisSettings)obj).getSiteId()) && this.instanceId.equals(((GisSettings)obj).getInstanceId());
    }

    public int hashCode() {
        return new StringBuilder(this.siteId).append(this.instanceId).toString().hashCode();
    }
}
