package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * 
 * @deprecated now indicator itself has sector.
 *
 */
@Deprecated
public class AmpIndicatorSector implements  Serializable {

    private Long ampIndicatorSectorId;
    
    private AmpSector sectorId;
    
    private AmpThemeIndicators themeIndicatorId;

    public AmpSector getSectorId() {
        return sectorId;
    }

    public void setSectorId(AmpSector sectorId) {
        this.sectorId = sectorId;
    }
    
    public String toString() {
        return sectorId.getName();
    }

    public AmpThemeIndicators getThemeIndicatorId() {
        return themeIndicatorId;
    }

    public void setThemeIndicatorId(AmpThemeIndicators themeIndicatorId) {
        this.themeIndicatorId = themeIndicatorId;
    }

    public Long getAmpIndicatorSectorId() {
        return ampIndicatorSectorId;
    }

    public void setAmpIndicatorSectorId(Long ampIndicatorSectorId) {
        this.ampIndicatorSectorId = ampIndicatorSectorId;
    }

}
