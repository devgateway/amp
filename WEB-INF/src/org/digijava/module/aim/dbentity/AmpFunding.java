package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpFunding implements Serializable {
	private Long ampFundingId;
	private AmpOrganisation ampDonorOrgId;
	private AmpActivity ampActivityId;
	private Long crsTransactionNo;
	private String financingId;
	private String fundingTermsCode;
	private Date plannedStartDate;
	private Date plannedCompletionDate;
	private Date actualStartDate;
	private Date actualCompletionDate;
	private Date originalCompDate;
	private Date lastAuditDate;
	private Date reportingDate;
	private String conditions;
	private String donorObjective;
	private String language;
	private String version;
	private String calType;
	private String comments;
	private Date signatureDate;
	private Set fundingDetails;
	private Set<AmpFundingMTEFProjection> mtefProjections;
	// private AmpTermsAssist ampTermsAssistId ;
	private Set closingDateHistory;

	/*
	 * tanzania adds funding amp-1707
	 */
	private Boolean active;
	private Boolean delegatedCooperation;
	private Boolean delegatedPartner;

	private ArrayList<Boolean> activeList;
	// private AmpModality modalityId;

	private AmpCategoryValue typeOfAssistance;
	private AmpCategoryValue financingInstrument;
	private AmpCategoryValue fundingStatus;
	private AmpCategoryValue modeOfPayment;

	// private Set survey;

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
	public void setAmpDonorOrgId(AmpOrganisation ampDonorOrgId) {
		this.ampDonorOrgId = ampDonorOrgId;
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

	public void setAmpActivityId(AmpActivity a) {
		this.ampActivityId = a;
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
	public Set getClosingDateHistory() {
		return closingDateHistory;
	}

	/**
	 * @param set
	 */
	public void setClosingDateHistory(Set set) {
		closingDateHistory = set;
	}

	public AmpCategoryValue getFinancingInstrument() {
		return financingInstrument;
	}

	public void setFinancingInstrument(AmpCategoryValue financingInstrument) {
		this.financingInstrument = financingInstrument;
	}

	public AmpCategoryValue getTypeOfAssistance() {
		return typeOfAssistance;
	}

	public void setTypeOfAssistance(AmpCategoryValue typeOfAssistence) {
		this.typeOfAssistance = typeOfAssistence;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getDelegatedCooperation() {
		return delegatedCooperation;
	}

	public void setDelegatedCooperation(Boolean delegatedCooperation) {
		this.delegatedCooperation = delegatedCooperation;
	}

	public Boolean getDelegatedPartner() {
		return delegatedPartner;
	}

	public void setDelegatedPartner(Boolean delegatedPartner) {
		this.delegatedPartner = delegatedPartner;
	}

	public ArrayList<Boolean> getActiveList() {
		return activeList;
	}

	public void setActiveList(ArrayList<Boolean> activeList) {
		this.activeList = activeList;
	}

	public Set<AmpFundingMTEFProjection> getMtefProjections() {
		return mtefProjections;
	}

	public void setMtefProjections(Set<AmpFundingMTEFProjection> mtefProjections) {
		this.mtefProjections = mtefProjections;
	}

	/**
	 * @return the donorObjective
	 */
	public String getDonorObjective() {
		return this.donorObjective;
	}

	/**
	 * @param donorObjective
	 *            the donorObjective to set
	 */
	public void setDonorObjective(String donorObjective) {
		this.donorObjective = donorObjective;
	}

	/**
	 * @return the fundingStatus
	 */
	public AmpCategoryValue getFundingStatus() {
		return fundingStatus;
	}

	/**
	 * @param fundingStatus
	 *            the fundingStatus to set
	 */
	public void setFundingStatus(AmpCategoryValue fundingStatus) {
		this.fundingStatus = fundingStatus;
	}

	/**
	 * @return the modeOfPayment
	 */
	public AmpCategoryValue getModeOfPayment() {
		return modeOfPayment;
	}

	/**
	 * @param modeOfPayment
	 *            the modeOfPayment to set
	 */
	public void setModeOfPayment(AmpCategoryValue modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

}
