package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;

public class MTEFProjection implements Serializable{
	
	private long indexId;
	private String projected; //to be  added to category manager
	private String amount;
	private String currencyCode;
	private String currencyName;
	private int index;
	
	private String reportingOrganizationName;
	private Long reportingOrganizationId;
	
	private Date projectionDate;
	private AmpFunding  ampFundingId;
	
	
		public MTEFProjection() {}

		public MTEFProjection(long id) {
			this.indexId = id;
		}
	
		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public String getCurrencyName() {
			return currencyName;
		}

		public void setCurrencyName(String currencyName) {
			this.currencyName = currencyName;
		}


		public Date getProjectionDate() {
			return projectionDate;
		}

		public void setProjectionDate(Date projectionDate) {
			this.projectionDate = projectionDate;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String transactionAmount) {
			this.amount = transactionAmount;
		}

		/**
		 * @return Returns the index.
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * @param index
		 *            The index to set.
		 */
		public void setIndex(int index) {
			this.index = index;
		}

		/**
		 * @return Returns the indexId.
		 */
		public long getIndexId() {
			return indexId;
		}
		/**
		 * @param indexId The indexId to set.
		 */
		public void setIndexId(long indexId) {
			this.indexId = indexId;
		}
		
		public boolean equals(Object obj) {
			if (obj == null) throw new NullPointerException();
			if (!(obj instanceof MTEFProjection)) throw new ClassCastException();
			
			MTEFProjection mtefPrj = (MTEFProjection) obj;
			return (this.indexId == mtefPrj.indexId);
		}

		public String getProjected() {
			return projected;
		}

		public void setProjected(String projected) {
			this.projected = projected;
		}

		public AmpFunding getAmpFundingId() {
			return ampFundingId;
		}

		public void setAmpFundingId(AmpFunding ampFundingId) {
			this.ampFundingId = ampFundingId;
		}

		public Long getReportingOrganizationId() {
			return reportingOrganizationId;
		}

		public void setReportingOrganizationId(Long reportingOrganizationId) {
			this.reportingOrganizationId = reportingOrganizationId;
		}

		public String getReportingOrganizationName() {
			return reportingOrganizationName;
		}

		public void setReportingOrganizationName(String reportingOrganizationName) {
			this.reportingOrganizationName = reportingOrganizationName;
		}


}
