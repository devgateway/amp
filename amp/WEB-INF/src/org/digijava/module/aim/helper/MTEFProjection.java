package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.dbentity.AmpFunding;

public class MTEFProjection implements Serializable, Comparable<MTEFProjection>{

	private Long projected; 
	private String amount;
	private String currencyCode;
	private String currencyName;
	private int index;
	
	private String reportingOrganizationName;
	private Long reportingOrganizationId;
	
	private String projectionDate;
	private String projectionDateLabel;
	private AmpFunding  ampFunding;
	
	
		public MTEFProjection() {}
	
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


		public String getProjectionDate() {
			return projectionDate;
		}

		public void setProjectionDate(String projectionDate) {
			this.projectionDate = projectionDate;
			
			String [] dateElements	= projectionDate.split("/");
			int year				= Integer.parseInt( dateElements[2] );
			String label			= year + "/" + (year+1);
			
			this.setProjectionDateLabel( label );
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

		public Long getProjected() {
			return projected;
		}

		public void setProjected(Long projected) {
			this.projected = projected;
		}

		public AmpFunding getAmpFunding() {
			return ampFunding;
		}

		public void setAmpFunding(AmpFunding ampFundingId) {
			this.ampFunding = ampFundingId;
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

		public int compareTo(MTEFProjection o) {
			Date d1 = DateConversion.getDate(this.projectionDate);
			Date d2 = DateConversion.getDate(o.projectionDate);
			
			return d1.compareTo(d2);
		}

		public String getProjectionDateLabel() {
			return projectionDateLabel;
		}

		public void setProjectionDateLabel(String projectionDateLabel) {
			this.projectionDateLabel = projectionDateLabel;
		}


}
