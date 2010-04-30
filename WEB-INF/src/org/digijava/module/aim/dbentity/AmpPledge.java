package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;

public class AmpPledge implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2427620222411238018L;
	private Long ampPledgeId;
	private Date date;
	private Double amount;
	private AmpCurrency currency;
	private Integer adjustmentType ;
	private String program;
	public Integer getAdjustmentType() {
		return adjustmentType;
	}
	public void setAdjustmentType(Integer adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	public Double getAmount() {
		return FeaturesUtil.applyThousandsForVisibility(amount);
	}
	public void setAmount(Double amount) {
		this.amount = FeaturesUtil.applyThousandsForEntry(amount);
	}
	public Long getAmpPledgeId() {
		return ampPledgeId;
	}
	public void setAmpPledgeId(Long ampPledgeId) {
		this.ampPledgeId = ampPledgeId;
	}
	public AmpCurrency getCurrency() {
		return currency;
	}
	public void setCurrency(AmpCurrency currency) {
		this.currency = currency;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	
	


}
