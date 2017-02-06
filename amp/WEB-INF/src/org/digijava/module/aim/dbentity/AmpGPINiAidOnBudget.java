package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

public class AmpGPINiAidOnBudget implements Serializable {
	
	private static final long serialVersionUID = -8747493117052602462L;
	private Long ampGPINiAidOnBudgetId;
	private AmpCurrency currencyId;
	private AmpOrganisation donorId;
	private Double amount;
	private String remark;
	private Date date;
	
	public Long getAmpGPINiAidOnBudgetId() {
		return ampGPINiAidOnBudgetId;
	}
	public void setAmpGPINiAidOnBudgetId(Long ampGPINiAidOnBudgetId) {
		this.ampGPINiAidOnBudgetId = ampGPINiAidOnBudgetId;
	}
	public AmpCurrency getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(AmpCurrency currencyId) {
		this.currencyId = currencyId;
	}
	public AmpOrganisation getDonorId() {
		return donorId;
	}
	public void setDonorId(AmpOrganisation donorId) {
		this.donorId = donorId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
