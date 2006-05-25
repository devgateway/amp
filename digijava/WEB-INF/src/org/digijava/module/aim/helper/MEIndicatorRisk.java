package org.digijava.module.aim.helper;

public class MEIndicatorRisk {
	
	private String risk;
	private byte riskRating;
	private int riskCount;
	/**
	 * @return Returns the risk.
	 */
	public String getRisk() {
		return risk;
	}
	/**
	 * @param risk The risk to set.
	 */
	public void setRisk(String risk) {
		this.risk = risk;
	}
	/**
	 * @return Returns the riskCount.
	 */
	public int getRiskCount() {
		return riskCount;
	}
	/**
	 * @param riskCount The riskCount to set.
	 */
	public void setRiskCount(int riskCount) {
		this.riskCount = riskCount;
	}
	/**
	 * @return Returns the riskRating.
	 */
	public byte getRiskRating() {
		return riskRating;
	}
	/**
	 * @param riskRating The riskRating to set.
	 */
	public void setRiskRating(byte riskRating) {
		this.riskRating = riskRating;
	}		
}