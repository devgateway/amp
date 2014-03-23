package org.digijava.module.fundingpledges.dbentity;

import lombok.Getter;
import lombok.Setter;

import org.digijava.module.aim.dbentity.AmpTheme;

@Getter @Setter
public class FundingPledgesProgram{

	private Long id;
	private FundingPledges pledgeid;
	private AmpTheme program;
	private Float programpercentage;	

	public int hashCode(){return program.getAmpThemeId().hashCode();}
	public boolean equals(Object oth){
		return program.getAmpThemeId().equals(((FundingPledgesProgram) oth).program.getAmpThemeId());
	}

}
