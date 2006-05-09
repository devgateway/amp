package org.digijava.module.aim.dbentity;

import java.util.Date;

public class AmpMECurrValHistory {
	private Long ampMECurrValHistoryId;
	private AmpMEIndicatorValue meIndValue;
	private float currValue;
	private Date currValueDate;

	/**
	 * @return Returns the ampMECurrValHistoryId.
	 */
	public Long getAmpMECurrValHistoryId() {
		return ampMECurrValHistoryId;
	}
	/**
	 * @param ampMECurrValHistoryId The ampMECurrValHistoryId to set.
	 */
	public void setAmpMECurrValHistoryId(Long ampMECurrValHistoryId) {
		this.ampMECurrValHistoryId = ampMECurrValHistoryId;
	}
	/**
	 * @return Returns the currValue.
	 */
	public float getCurrValue() {
		return currValue;
	}
	/**
	 * @param currValue The currValue to set.
	 */
	public void setCurrValue(float currValue) {
		this.currValue = currValue;
	}
	/**
	 * @return Returns the currValueDate.
	 */
	public Date getCurrValueDate() {
		return currValueDate;
	}
	/**
	 * @param currValueDate The currValueDate to set.
	 */
	public void setCurrValueDate(Date currValueDate) {
		this.currValueDate = currValueDate;
	}
	/**
	 * @return Returns the meIndValue.
	 */
	public AmpMEIndicatorValue getMeIndValue() {
		return meIndValue;
	}
	/**
	 * @param meIndValue The meIndValue to set.
	 */
	public void setMeIndValue(AmpMEIndicatorValue meIndValue) {
		this.meIndValue = meIndValue;
	}
	
}