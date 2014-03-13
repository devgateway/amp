package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.orgProfile.util.OrgProfileUtil;

@Getter @Setter
public class AmpFundingDetail implements Serializable, Cloneable, FundingInformationItem {
	
	public static class FundingDetailComparator implements Comparator<AmpFundingDetail>, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(AmpFundingDetail arg0, AmpFundingDetail arg1) {
			
			if (arg0.getTransactionDate() != null && arg0.getTransactionAmount() != null && arg0.getAdjustmentType() != null){
				if(arg0.getTransactionDate()!=null && arg1.getTransactionDate()!=null && arg0.getTransactionDate().compareTo(arg1.getTransactionDate()) != 0) 
					return arg0.getTransactionDate().compareTo(arg1.getTransactionDate());
				if(arg0.getTransactionDate()!=null && arg1.getTransactionDate()==null) 
					return -1;
				if(arg0.getTransactionDate()==null && arg1.getTransactionDate()!=null) 
					return 1;
			}
			if(arg0.getReportingDate()!=null && arg1.getReportingDate()!=null)
				return arg0.getReportingDate().compareTo(arg1.getReportingDate());
			if(arg0.getAmpFundDetailId()!=null && arg1.getAmpFundDetailId()!=null) 
				return arg0.getAmpFundDetailId().compareTo(arg1.getAmpFundDetailId());
			if(arg0.getAmpFundDetailId()!=null && arg1.getAmpFundDetailId()==null) 
				return -1;
			if(arg0.getAmpFundDetailId()==null && arg1.getAmpFundDetailId()!=null) 
				return 1;
			return arg0.hashCode()-arg1.hashCode();
		}
	}
	
	private Long ampFundDetailId ;
	private Integer fiscalYear ;
	private Integer fiscalQuarter;
	/**
	 * values of transactionType
	 * public static final int COMMITMENT = 0 ;
	public static final int DISBURSEMENT = 1 ;
	public static final int EXPENDITURE = 2 ;
    public static final int DISBURSEMENT_ORDER = 4 ;
	public static final int MTEFPROJECTION = 3 ;
	 */
	private Integer transactionType ;
	
	private AmpCategoryValue adjustmentType ;

	private Date transactionDate ;
	private Date transactionDate2 ;
	private Date reportingDate;
	private Double transactionAmount;
	private Double thousandsTransactionAmount;
	private String language ;
	private String version ;
	private String calType ;
	private String orgRoleCode ; // defunct
	private AmpCurrency ampCurrencyId ;
	private AmpOrganisation reportingOrgId;
	private AmpFunding  ampFundingId;
	private Double fixedExchangeRate;
	private AmpCurrency fixedRateBaseCurrency;
	private Boolean disbursementOrderRejected;
	private FundingPledges pledgeid;
    private Float capitalSpendingPercentage;

	private AmpOrganisation recipientOrg;
	private AmpRole recipientRole;
    
	private String expCategory;
    private String disbOrderId;
    private IPAContract contract;
        
        
        public AmpFundingDetail(){}
        
        public AmpFundingDetail(Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId, Float percent,Double fixedExchangeRate){
            this(null,transactionType, adjustmentType, transactionAmount, transactionDate, ampCurrencyId,  percent, fixedExchangeRate);
        }
        public AmpFundingDetail(Long ampFundDetailId, Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId, Float percent,Double fixedExchangeRate){
            this(ampFundDetailId,transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            if (percent == null) {
                percent = 0f;
            }
            this.transactionAmount = transactionAmount * percent / 100;
        }
        
        public AmpFundingDetail(Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId, Float percent, Float percent2,Double fixedExchangeRate){
            this(null,transactionType, adjustmentType, transactionAmount, transactionDate, ampCurrencyId,  percent, percent2,fixedExchangeRate);
        }
        public AmpFundingDetail(Long ampFundDetailId, Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId, Float percent1, Float percent2,Double fixedExchangeRate){
            this(ampFundDetailId,transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            if((percent1==null||percent1==0)&&(percent2==null||percent2==0)){
            this.transactionAmount=transactionAmount;
            }
            else{
            	//Check if the the percentage is null before dividing. If it's null, the calculation cannot be done, so return 0
            	if(percent1 != null && percent2 != null)
            		if (percent1.compareTo(0.0f) == 0 || percent2.compareTo(0.0f) == 0) 
            			this.transactionAmount= 0d;
            		else
            			this.transactionAmount=(transactionAmount*percent1/100)*percent2/100;
            	else
            		this.transactionAmount= 0d;
            		
            }
        }
        
        // used in dashborads when there is a filter by location, sector and program
        public AmpFundingDetail(Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId, Float percent1, Float percent2, Float percent3,Double fixedExchangeRate){
        	this(null,transactionType, adjustmentType, transactionAmount, transactionDate, ampCurrencyId,  percent1, percent2,percent3,fixedExchangeRate);
        }
        public AmpFundingDetail(Long ampFundDetailId, Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId, Float percent1, Float percent2, Float percent3,Double fixedExchangeRate){
                this(ampFundDetailId,transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            if((percent1==null||percent1==0)&&(percent2==null||percent2==0)&&(percent3==null||percent3==0)){
            	this.transactionAmount=transactionAmount;
            }
            else{
            	//Check if the the percentage is null before dividing. If it's null, the calculation cannot be done, so return 0
            	if(percent1 != null && percent2 != null && percent3 != null)
            		if (percent1.compareTo(0.0f) == 0 || percent2.compareTo(0.0f) == 0 || percent3.compareTo(0.0f) == 0) 
            			this.transactionAmount= 0d;
            		else
            			this.transactionAmount= ((transactionAmount*percent1/100)*percent2/100)*percent3/100;
            	else
            		this.transactionAmount= 0d;
            		
            }
        }
        
        // used in org profile for indicator 4
         public AmpFundingDetail(Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId,Double fixedExchangeRate, Long ahsureyId){
            this(null, transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            this.transactionAmount=transactionAmount*OrgProfileUtil.getQ4Value(ahsureyId);
     
        }
          // used in org profile 
        public AmpFundingDetail(Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId,Double fixedExchangeRate){
         	this(null,transactionType, adjustmentType, transactionAmount, transactionDate, ampCurrencyId, fixedExchangeRate);
		}
        public AmpFundingDetail(Long ampFundDetailId, Integer transactionType,AmpCategoryValue adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId,Double fixedExchangeRate){
            this(ampFundDetailId, transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            this.transactionAmount=transactionAmount;
            
        }
         
        public AmpFundingDetail(Long ampFundDetailId, Integer transactionType,AmpCategoryValue adjustmentType,Date transactionDate,AmpCurrency ampCurrencyId,Double fixedExchangeRate){
        	this.ampFundDetailId=ampFundDetailId;
        	this.transactionType=transactionType;
             this.adjustmentType=adjustmentType;
            this.transactionDate=transactionDate;
            this.ampCurrencyId=ampCurrencyId;
            this.fixedExchangeRate=fixedExchangeRate;
            
        }

		@Override
		public Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}

		public Double getAbsoluteTransactionAmount() {
			return transactionAmount;
		}

		@Override
		public String toString()
		{
			String currency = this.getAmpCurrencyId() == null ? "NOCUR" : this.getAmpCurrencyId().getCurrencyCode();
			String recipient = this.getRecipientOrg() == null ? "NOORG" : this.getRecipientOrg().getName();
			String trTypeName = "NOTRTYPE";
			switch(getTransactionType().intValue())
			{
			case 0:
				trTypeName = "Commitment";
				break;
				
			case 1:
				trTypeName = "Disbursement";
				break;
				
			case 2:
				trTypeName = "Expenditure";
				break;
				
			case 3:
				trTypeName = "MTEF Projection";
				break;
				
			default:
				trTypeName = String.format("trType %d", getTransactionType());
				break;
			}
			
			String transText = (this.getAdjustmentType() == null ? "NOADJUST" : this.getAdjustmentType().getLabel()) + " " + trTypeName;			
			
			return String.format("%s %s %s to %s", transText, this.getAbsoluteTransactionAmount(), currency, recipient);
		}

	public boolean isSscTransaction()
	{
		if (this.getAdjustmentType() == null)
			return false;
		
		if (this.getAdjustmentType().getAmpCategoryClass() == null)
			return false;
		
		return this.getAdjustmentType().getAmpCategoryClass().getKeyName().equals(CategoryConstants.SSC_ADJUSTMENT_TYPE_KEY);
	}
}
