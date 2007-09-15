package org.digijava.module.aim.dbentity;

import java.util.Date ; 
import org.digijava.module.aim.dbentity.AmpCurrency ;
import org.digijava.module.aim.dbentity.AmpFunding ;

public class AmpFundingMTEFProjection {

		private Long ampFundingMTEFProjectionId ;
		private Long projected; //to be  added to category manager
		private Double amount;
		private AmpCurrency ampCurrency;
		private Date projectionDate;
		private AmpFunding  ampFundingId;
		
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public AmpFunding getAmpFundingId() {
			return ampFundingId;
		}
		public void setAmpFundingId(AmpFunding ampFundingId) {
			this.ampFundingId = ampFundingId;
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
		public Long getProjected() {
			return projected;
		}
		public void setProjected(Long projected) {
			this.projected = projected;
		}
		public Date getProjectionDate() {
			return projectionDate;
		}
		public void setProjectionDate(Date projectionDate) {
			this.projectionDate = projectionDate;
		}
		
		

}
