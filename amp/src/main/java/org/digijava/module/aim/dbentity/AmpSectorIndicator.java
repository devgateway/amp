package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpSectorIndicator implements Serializable
{
    private Long ampThemeIndValId;
    private Long sectorId;
    private String sectorName;
    
    public Long getAmpThemeIndValId() {
        return ampThemeIndValId;
    }
    public void setAmpThemeIndValId(Long ampThemeIndValId) {
        this.ampThemeIndValId = ampThemeIndValId;
    }
    public Long getSectorId() {
        return sectorId;
    }
    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }
    public String getSectorName() {
        return sectorName;
    }
    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }
}
