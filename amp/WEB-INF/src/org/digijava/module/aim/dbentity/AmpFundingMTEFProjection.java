package org.digijava.module.aim.dbentity;

import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpFundingMTEFProjection {

		private Long ampFundingMTEFProjectionId ;
		private AmpCategoryValue projected; 
		private Double amount;
		private AmpCurrency ampCurrency;
		private Date projectionDate;
		private AmpFunding  ampFunding;
		
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
		
		

}
