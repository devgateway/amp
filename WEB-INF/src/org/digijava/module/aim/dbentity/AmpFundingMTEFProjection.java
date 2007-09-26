package org.digijava.module.aim.dbentity;

import java.util.Date ; 
import org.digijava.module.aim.dbentity.AmpCurrency ;
import org.digijava.module.aim.dbentity.AmpFunding ;

public class AmpFundingMTEFProjection {

		private Long ampFundingMTEFProjectionId ;
		private AmpCategoryValue projected; 
		private Double amount;
		private AmpCurrency ampCurrency;
		private Date projectionDate;
		private AmpFunding  ampFunding;
		
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
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
