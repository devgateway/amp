package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

public class AmpFundingDetail implements Serializable
{
	private Long ampFundDetailId ;
	private Integer fiscalYear ;
	private Integer fiscalQuarter;
	private Integer transactionType ;
	private Integer adjustmentType ;
	private Date transactionDate ;
	private Date transactionDate2 ;
	private Date reportingDate;
	private Double transactionAmount;
	private String language ;
	private String version ;
	private String calType ;
	private String orgRoleCode ; // defunct
	private AmpCurrency ampCurrencyId ;
	private AmpOrganisation reportingOrgId;
	private AmpFunding  ampFundingId;
	private Double fixedExchangeRate;

	private String expCategory;
        private String disbOrderId;
        private IPAContract contract;

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
			return transactionAmount;
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
			transactionAmount = d;
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

}




