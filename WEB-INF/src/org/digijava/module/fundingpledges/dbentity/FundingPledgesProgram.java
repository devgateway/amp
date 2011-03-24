package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.dbentity.AmpTheme;

public class FundingPledgesProgram {

	private Long id;
	private FundingPledges pledgeid;
	private AmpTheme program;
	private Float programpercentage;
	
	public Long getId() {
		return id;
	}
	public Float getProgrampercentage() {
		return programpercentage;
	}
	public void setProgrampercentage(Float programpercentage) {
		this.programpercentage = programpercentage;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public FundingPledges getPledgeid() {
		return pledgeid;
	}
	public void setPledgeid(FundingPledges pledgeid) {
		this.pledgeid = pledgeid;
	}
	
	public AmpTheme getProgram() {
		return program;
	}
	public void setProgram(AmpTheme program) {
		this.program = program;
	}
}
