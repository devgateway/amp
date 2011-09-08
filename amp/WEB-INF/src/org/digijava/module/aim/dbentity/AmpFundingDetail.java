package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.orgProfile.util.OrgProfileUtil;

public class AmpFundingDetail implements Serializable, Cloneable, Comparable<AmpFundingDetail>
{
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
	private Integer adjustmentType ;
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
	
	public FundingPledges getPledgeid() {
		return pledgeid;
	}


	public void setPledgeid(FundingPledges pledgeid) {
		this.pledgeid = pledgeid;
	}


	private String expCategory;
        private String disbOrderId;
        private IPAContract contract;
        private Double transactionAmountInBaseCurrency;
        
        public AmpFundingDetail(){}
        
        
        public AmpFundingDetail(Integer transactionType,Integer adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId, Float percent,Double fixedExchangeRate){
            this(transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            if(percent==null||percent==0){
            this.transactionAmount=transactionAmount;
            }
            else{
                this.transactionAmount=transactionAmount*percent/100;
            }
        }
        
        public AmpFundingDetail(Integer transactionType,Integer adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId, Float percent1, Float percent2,Double fixedExchangeRate){
            this(transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            if((percent1==null||percent1==0)&&(percent2==null||percent2==0)){
            this.transactionAmount=transactionAmount;
            }
            else{
                this.transactionAmount=transactionAmount/percent1/percent2*100;
            }
        }
        
        // used in org profile for indicator 4
         public AmpFundingDetail(Integer transactionType,Integer adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId,Double fixedExchangeRate, Long ahsureyId){
            this(transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            this.transactionAmount=transactionAmount*OrgProfileUtil.getQ4Value(ahsureyId);
     
        }
          // used in org profile 
         public AmpFundingDetail(Integer transactionType,Integer adjustmentType,Double transactionAmount,Date transactionDate,AmpCurrency ampCurrencyId,Double fixedExchangeRate){
            this(transactionType,adjustmentType,transactionDate,ampCurrencyId,fixedExchangeRate); 
            this.transactionAmount=transactionAmount;
            
        }
         
        public AmpFundingDetail(Integer transactionType,Integer adjustmentType,Date transactionDate,AmpCurrency ampCurrencyId,Double fixedExchangeRate){
            this.transactionType=transactionType;
            this.adjustmentType=adjustmentType;
            this.transactionDate=transactionDate;
            this.ampCurrencyId=ampCurrencyId;
            this.fixedExchangeRate=fixedExchangeRate;
            
        }
        

        /**
		 * @return the transactionAmountInBaseCurrency
		 */
		public Double getTransactionAmountInBaseCurrency() {
			return transactionAmountInBaseCurrency;
		}


		/**
		 * @param transactionAmountInBaseCurrency the transactionAmountInBaseCurrency to set
		 */
		public void setTransactionAmountInBaseCurrency(
				Double transactionAmountInBaseCurrency) {
			this.transactionAmountInBaseCurrency = transactionAmountInBaseCurrency;
		}


		public IPAContract getContract() {
            return contract;
        }

        public void setContract(IPAContract contract) {
            this.contract = contract;
        }

        /**
		 * @return
		 */
		public Integer getAdjustmentType() {
			return adjustmentType;
		}

		/**
		 * @return
		 */
		public Long getAmpFundDetailId() {
			return ampFundDetailId;
		}

/*		public Long getAmpFundingId() {
			return ampFundingId;
		}*/

		/**
		 * @return
		 */
		public Integer getFiscalQuarter() {
			return fiscalQuarter;
		}

		/**
		 * @return
		 */
		public Integer getFiscalYear() {
			return fiscalYear;
		}

		/**
		 * @return
		 */
		public String getOrgRoleCode() {
			return orgRoleCode;
		}

		/**
		 * @return
		 */
		public Date getReportingDate() {
			return reportingDate;
		}

		/**
		 * @return
		 */
		public Double getTransactionAmount() {
			return FeaturesUtil.applyThousandsForVisibility(transactionAmount);
		}

		/**
		 * @return
		 */
		public Date getTransactionDate() {
			return transactionDate;
		}

		/**
		 * @return
		 */
		public Integer getTransactionType() {
			return transactionType;
		}

		/**
		 * @param i
		 */
		public void setAdjustmentType(Integer i) {
			adjustmentType = i;
		}

		/**
		 * @param long1
		 */
		public void setAmpFundDetailId(Long long1) {
			ampFundDetailId = long1;
		}

/*		public void setAmpFundingId(Long long1) {
			ampFundingId = long1;
		}*/

		/**
		 * @param string
		 */
		public void setFiscalQuarter(Integer i) {
			fiscalQuarter = i;
		}

		/**
		 * @param i
		 */
		public void setFiscalYear(Integer i) {
			fiscalYear = i;
		}

		/**
		 * @param string
		 */
		public void setOrgRoleCode(String string) {
			orgRoleCode = string;
		}

		/**
		 * @param date
		 */
		public void setReportingDate(Date date) {
			reportingDate = date;
		}

		/**
		 * @param d
		 */
		public void setTransactionAmount(Double d) {
			transactionAmount =FeaturesUtil.applyThousandsForEntry(d);
		}

		/**
		 * @param date
		 */
		public void setTransactionDate(Date date) {
			transactionDate = date;
		}

		/**
		 * @param i
		 */
		public void setTransactionType(Integer i) {
			transactionType = i;
		}

	public String getLanguage() {
		return language;
	}

	public String getVersion() {
		return version;
	}


	public void setLanguage(String string) {
		language = string;
	}

	public void setVersion(String string) {
		version = string;
	}


	/**reportingOrgId
	 * @return
	 */
	public AmpCurrency getAmpCurrencyId() {
		return ampCurrencyId;
	}

	/**
	 * @param long1
	 */
	public void setAmpCurrencyId(AmpCurrency ampCurrencyId) {
		this.ampCurrencyId = ampCurrencyId;
	}

	public AmpOrganisation getReportingOrgId() {
		return reportingOrgId;
	}

	/**
	 * @param long1
	 */
	public void setReportingOrgId(AmpOrganisation reportingOrgId) {
		this.reportingOrgId = reportingOrgId;
	}

	public AmpFunding getAmpFundingId() {
		return ampFundingId;
	}

	public void setAmpFundingId(AmpFunding f ) {
		this.ampFundingId = f;
	}


	/**
	 * @return
	 */
	public Date getTransactionDate2() {
		return transactionDate2;
	}

	/**
	 * @param date
	 */
	public void setTransactionDate2(Date date) {
		transactionDate2 = date;
	}

	/**
	 * @return
	 */
	public String getCalType() {
		return calType;
	}

	/**
	 * @param string
	 */
	public void setCalType(String string) {
		calType = string;
	}

	/**
	 * @return Returns the expCategory.
	 */
	public String getExpCategory() {
		return expCategory;
	}
	/**
	 * @param expCategory The expCategory to set.
	 */
	public void setExpCategory(String expCategory) {
		this.expCategory = expCategory;
	}

	public Double getFixedExchangeRate() {
		return fixedExchangeRate;
	}


        public String getDisbOrderId() {
                return disbOrderId;
        }

        public void setFixedExchangeRate(Double fixedExchangeRate) {
		this.fixedExchangeRate = fixedExchangeRate;
	}


        public void setDisbOrderId(String disbOrderId) {
                this.disbOrderId = disbOrderId;
        }

		public Double getThousandsTransactionAmount() {
			return thousandsTransactionAmount;
		}

		public void setThousandsTransactionAmount(Double thousandsTransactionAmount) {
			this.thousandsTransactionAmount = thousandsTransactionAmount;
		}


		public void setDisbursementOrderRejected(
				Boolean disbursementOrderRejected) {
			this.disbursementOrderRejected = disbursementOrderRejected;
		}


		public Boolean getDisbursementOrderRejected() {
			return disbursementOrderRejected;
		}


		/**
		 * @return the fixedRateBaseCurrency
		 */
		public AmpCurrency getFixedRateBaseCurrency() {
			return fixedRateBaseCurrency;
		}


		/**
		 * @param fixedRateBaseCurrency the fixedRateBaseCurrency to set
		 */
		public void setFixedRateBaseCurrency(AmpCurrency fixedRateBaseCurrency) {
			this.fixedRateBaseCurrency = fixedRateBaseCurrency;
		}


		@Override
		public Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}


		@Override
		public int compareTo(AmpFundingDetail o) {
			if (o.getAmpFundDetailId() == null) 
				return -1;
			return o.getAmpFundDetailId().compareTo(getAmpFundDetailId());
		}

}