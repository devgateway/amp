package org.digijava.module.fundingpledges.dbentity;

import lombok.Getter;
import lombok.Setter;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpTheme;

@Getter @Setter
public class FundingPledgesLocation
{
	private Long id;
	private FundingPledges pledgeid;
	private AmpCategoryValueLocations location;
	private Float locationpercentage;
}
