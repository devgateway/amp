package org.digijava.module.aim.helper;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorTheme;

public class AmpIndSectors implements Serializable
{

private Long ampIndicatorSectorId;
	
	private AmpSector sectorId;
	
	private IndicatorTheme themeIndicatorId;

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

	public IndicatorTheme getThemeIndicatorId() {
		return themeIndicatorId;
	}

	public void setThemeIndicatorId(IndicatorTheme themeIndicatorId) {
		this.themeIndicatorId = themeIndicatorId;
	}
	
}
