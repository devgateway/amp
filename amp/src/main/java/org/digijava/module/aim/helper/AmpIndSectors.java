package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;

import java.io.Serializable;

public class AmpIndSectors implements Serializable
{

private Long ampIndicatorSectorId;
    
    private AmpSector sectorId;
    
    private AmpThemeIndicators themeIndicatorId;

    public Long getAmpIndicatorSectorId() {
        return ampIndicatorSectorId;
    }

    public void setAmpIndicatorSectorId(Long ampIndicatorSectorId) {
        this.ampIndicatorSectorId = ampIndicatorSectorId;
    }

    public AmpSector getSectorId() {
        return sectorId;
    }

    public void setSectorId(AmpSector sectorId) {
        this.sectorId = sectorId;
    }

    public AmpThemeIndicators getThemeIndicatorId() {
        return themeIndicatorId;
    }

    public void setThemeIndicatorId(AmpThemeIndicators themeIndicatorId) {
        this.themeIndicatorId = themeIndicatorId;
    }
    
}
