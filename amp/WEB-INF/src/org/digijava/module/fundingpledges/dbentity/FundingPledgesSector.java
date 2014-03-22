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
}
