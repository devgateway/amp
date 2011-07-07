package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

public class AmpClosingDateHistory	implements Serializable {
		
	private Long ampClosingDteHstryId;
	private AmpFunding ampFundingId;
	private Integer type;
	private Date closingDate;
	
	/**
	 * @return
	 */
	public Date getClosingDate() {
		return closingDate;
	}

	/**
	 * @param date
	 */
	public void setClosingDate(Date date) {
		closingDate = date;
	}

	/**
	 * @return
	 */
	public Long getAmpClosingDteHstryId() {
		return ampClosingDteHstryId;
	}

	/**
	 * @param long1
	 */
	public void setAmpClosingDteHstryId(Long long1) {
		ampClosingDteHstryId = long1;
	}

	/**
	 * @return
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param integer
	 */
	public void setType(Integer integer) {
		type = integer;
	}

	/**
	 * @return
	 */
	public AmpFunding getAmpFundingId() {
		return ampFundingId;
	}

	/**
	 * @param funding
	 */
	public void setAmpFundingId(AmpFunding funding) {
		ampFundingId = funding;
	}

}	
