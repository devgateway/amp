package org.digijava.module.fundingpledges.dbentity;

import lombok.Getter;
import lombok.Setter;

import org.digijava.module.aim.dbentity.AmpTheme;

@Getter @Setter
public class FundingPledgesProgram implements ValueWithPercentage {

	private Long id;
	private FundingPledges pledgeid;
	private AmpTheme program;
	private Float programpercentage;	
	
	public Long getDbId(){
		return id;
	}
	public Long getInternalId(){
		return program == null ? null : program.getAmpThemeId();
	}
	
	public Long getRootId(){
		return program == null ? null : program.getRootTheme().getAmpThemeId();
	}
	
	public String getRootName(){
		return program == null ? null : program.getRootTheme().getName();
	}
	
	public String getName(){
		return program == null ? null : program.getName();
	}
	public String getHierarchicalName(){
		return program == null ? null : program.getHierarchicalName();
	}
	public Float getPercentage(){
		return programpercentage;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}

}
