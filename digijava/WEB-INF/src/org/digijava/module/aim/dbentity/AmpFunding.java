package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date ; 
import java.util.Set ; 
import org.digijava.module.aim.dbentity.AmpOrganisation ;
import org.digijava.module.aim.dbentity.AmpActivity ;
import org.digijava.module.aim.dbentity.AmpTermsAssist ;

public class AmpFunding implements Serializable
{
	private Long ampFundingId ;
	private AmpOrganisation ampDonorOrgId ;
	private AmpActivity ampActivityId;
	private Long crsTransactionNo ;
	private String financingId ;
	private String fundingTermsCode ;
	private Date plannedStartDate;
	private Date plannedCompletionDate;
	private Date actualStartDate;
	private Date actualCompletionDate;
	private Date originalCompDate;
	private Date lastAuditDate;
	private Date reportingDate ;
	private String conditions ;
	private String language ;
	private String version ;
	private String calType;
	private String comments;
	private Date signatureDate ;
	private Set fundingDetails ;
	private AmpTermsAssist ampTermsAssistId ;
	private Set closingDateHistory;
	
	private AmpModality modalityId;
	
	
		/**
		 * @return
		 */
		public Date getActualCompletionDate() {
			return actualCompletionDate;
		}

		/**
		 * @return
		 */
		public Date getActualStartDate() {
			return actualStartDate;
		}

		/**
		 * @return
		 */
		public AmpOrganisation getAmpDonorOrgId() {
			return ampDonorOrgId;
		}

	/**
	 * @return
	 */
	public Long getAmpFundingId() {
		return ampFundingId;
	}

		/**
		 * @return
		 */
		public String getConditions() {
			return conditions;
		}

		/**
		 * @return
		 */
		public Long getCrsTransactionNo() {
			return crsTransactionNo;
		}

		/**
		 * @return
		 */
		public String getFinancingId() {
			return financingId;
		}

		/**
		 * @return
		 */
		public Set getFundingDetails() {
			return fundingDetails;
		}

		/**
		 * @return
		 */
		public String getFundingTermsCode() {
			return fundingTermsCode;
		}

		/**
		 * @return
		 */
		public Date getPlannedCompletionDate() {
			return plannedCompletionDate;
		}

		/**
		 * @return
		 */
		public Date getPlannedStartDate() {
			return plannedStartDate;
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
		public Date getSignatureDate() {
			return signatureDate;
		}

		/**
		 * @param date
		 */
		public void setActualCompletionDate(Date date) {
			actualCompletionDate = date;
		}

		/**
		 * @param date
		 */
		public void setActualStartDate(Date date) {
			actualStartDate = date;
		}

		/**
		 * @param long1
		 */
		public void setAmpDonorOrgId(AmpOrganisation ampDonorOrgId ) {
			this.ampDonorOrgId = ampDonorOrgId ;
		}

	/**
	 * @param long1
	 */
	public void setAmpFundingId(Long long1) {
		ampFundingId = long1;
	}

		/**
		 * @param string
		 */
		public void setConditions(String string) {
			conditions = string;
		}

		/**
		 * @param long1
		 */
		public void setCrsTransactionNo(Long long1) {
			crsTransactionNo = long1;
		}

		/**
		 * @param string
		 */
		public void setFinancingId(String string) {
			financingId = string;
		}

		/**
		 * @param set
		 */
		public void setFundingDetails(Set set) {
			fundingDetails = set;
		}

		/**
		 * @param string
		 */
		public void setFundingTermsCode(String string) {
			fundingTermsCode = string;
		}

		/**
		 * @param date
		 */
		public void setPlannedCompletionDate(Date date) {
			plannedCompletionDate = date;
		}

		/**
		 * @param date
		 */
		public void setPlannedStartDate(Date date) {
			plannedStartDate = date;
		}

		/**
		 * @param date
		 */
		public void setReportingDate(Date date) {
			reportingDate = date;
		}

		/**
		 * @param date
		 */
		public void setSignatureDate(Date date) {
			signatureDate = date;
		}


	/**
	 * @return
	 */
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

	public void setAmpActivityId(AmpActivity a ) {
			this.ampActivityId = a ;
		}
		
	public AmpActivity getAmpActivityId() {
			return ampActivityId;
		}
	/**
	 * @return
	 */
	public Date getOriginalCompDate() {
		return originalCompDate;
	}

	/**
	 * @param date
	 */
	public void setOriginalCompDate(Date date) {
		originalCompDate = date;
	}

	/**
	 * @return
	 */
	public Date getLastAuditDate() {
		return lastAuditDate;
	}

	/**
	 * @param date
	 */
	public void setLastAuditDate(Date date) {
		lastAuditDate = date;
	}

	/**
	 * @return
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param string
	 */
	public void setComments(String string) {
		comments = string;
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
	 * @return
	 */
	public AmpTermsAssist getAmpTermsAssistId() {
		return ampTermsAssistId;
	}

	/**
	 * @param assist
	 */
	public void setAmpTermsAssistId(AmpTermsAssist assist) {
		ampTermsAssistId = assist;
	}

	/**
	 * @return
	 */
	public Set getClosingDateHistory() {
		return closingDateHistory;
	}

	/**
	 * @param set
	 */
	public void setClosingDateHistory(Set set) {
		closingDateHistory = set;
	}

	/**
	 * @return Returns the modalityId.
	 */
	public AmpModality getModalityId() {
		return modalityId;
	}
	/**
	 * @param modalityId The modalityId to set.
	 */
	public void setModalityId(AmpModality modalityId) {
		this.modalityId = modalityId;
	}
}
	
	
	
	
