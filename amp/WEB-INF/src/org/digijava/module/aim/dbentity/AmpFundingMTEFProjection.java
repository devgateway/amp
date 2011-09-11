package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpFundingMTEFProjection implements Cloneable, Serializable, Comparable<AmpFundingMTEFProjection> {
	
	
	public static class FundingMTEFProjectionComparator implements Comparator<AmpFundingMTEFProjection>, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(AmpFundingMTEFProjection arg0, AmpFundingMTEFProjection arg1) {
			if(arg0.getReportingDate()!=null && arg1.getReportingDate()!=null) 
				return arg0.getReportingDate().compareTo(arg1.getReportingDate());
			if(arg0.getAmpFundingMTEFProjectionId()!=null && arg1.getAmpFundingMTEFProjectionId()!=null) 
				return arg0.getAmpFundingMTEFProjectionId().compareTo(arg1.getAmpFundingMTEFProjectionId());
			if(arg0.getAmpFundingMTEFProjectionId()!=null && arg1.getAmpFundingMTEFProjectionId()==null) 
				return -1;
			if(arg0.getAmpFundingMTEFProjectionId()==null && arg1.getAmpFundingMTEFProjectionId()!=null) 
				return 1;
			return arg0.hashCode()-arg1.hashCode();
		}
		
	}
	

		private Long ampFundingMTEFProjectionId ;
		private AmpCategoryValue projected; 
		private Double amount;
		private AmpCurrency ampCurrency;
		private Date projectionDate;
		private AmpFunding  ampFunding;
		private Date reportingDate;
		
		public Date getReportingDate() {
			return reportingDate;
		}
		public void setReportingDate(Date reportingDate) {
			this.reportingDate = reportingDate;
		}
		public Double getAmount() {
			return FeaturesUtil.applyThousandsForVisibility(amount);
		}
		public void setAmount(Double amount) {
			this.amount = FeaturesUtil.applyThousandsForEntry(amount);
		}
		public AmpFunding getAmpFunding() {
			return ampFunding;
		}
		public void setAmpFunding(AmpFunding ampFundingId) {
			this.ampFunding = ampFundingId;
		}
		public Long getAmpFundingMTEFProjectionId() {
			return ampFundingMTEFProjectionId;
		}
		public void setAmpFundingMTEFProjectionId(Long ampFundingMTEFProjectionId) {
			this.ampFundingMTEFProjectionId = ampFundingMTEFProjectionId;
		}
		public AmpCurrency getAmpCurrency() {
			return ampCurrency;
		}
		public void setAmpCurrency(AmpCurrency currency) {
			this.ampCurrency = currency;
		}

		public AmpCategoryValue getProjected() {
			return projected;
		}
		public void setProjected(AmpCategoryValue projected) {
			this.projected = projected;
		}
		public Date getProjectionDate() {
			return projectionDate;
		}
		public void setProjectionDate(Date projectionDate) {
			this.projectionDate = projectionDate;
		}
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}
		@Override
		public int compareTo(AmpFundingMTEFProjection o) {
			return o.getAmpFundingMTEFProjectionId().compareTo(getAmpFundingMTEFProjectionId());
		}
}