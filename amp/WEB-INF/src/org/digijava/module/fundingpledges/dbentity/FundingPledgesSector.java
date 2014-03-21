package org.digijava.module.fundingpledges.dbentity;

import lombok.*;

import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.SectorUtil;

@Getter @Setter
public class FundingPledgesSector 
{
	private Long id;
	private FundingPledges pledgeid;
	private AmpSector sector;
	private Float sectorpercentage;
	
	/**
	 * TODO: THIS CODE NEEDS REWRITTEN
	 * @return
	 */
	public ActivitySector createActivitySector()
	{
		ActivitySector res = new ActivitySector();
		res.setId(this.getId());
		res.setSectorId(sector.getAmpSectorId());
		res.setSectorPercentage(sectorpercentage);
		res.setSectorScheme(SectorUtil.getAmpSectorScheme(sector.getAmpSecSchemeId().getAmpSecSchemeId()).getSecSchemeName());
		res.setConfigId(sector.getAmpSecSchemeId().getAmpSecSchemeId());
		if (sector.getParentSectorId()==null) {
			res.setSectorName(sector.getName());
		} else if (sector.getParentSectorId().getParentSectorId()==null){
			res.setSectorName(sector.getParentSectorId().getName());
			res.setSubsectorLevel1Name(sector.getName());
		} else {
			res.setSectorName(sector.getParentSectorId().getParentSectorId().getName());
			res.setSubsectorLevel1Name(sector.getParentSectorId().getName());
			res.setSubsectorLevel2Name(sector.getName());
		}
		
		return res;
	}
}
