package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * @author jose
 *
 */
public class Funding implements Serializable 
{
	private static Logger logger		= Logger.getLogger(Funding.class);
	
    private long fundingId;
	//private AmpTermsAssist ampTermsAssist;
    private AmpCategoryValue typeOfAssistance;
    private AmpCategoryValue financingInstrument;
    private AmpCategoryValue fundingStatus;
    private AmpCategoryValue modeOfPayment;
    
	private String orgFundingId;
	private String signatureDate;
	//private AmpModality modality;
	private Collection fundingDetails;	// Collection of Funding Details
	private Collection<MTEFProjection> mtefProjections;
   	private String currentFunding;
   	private String propStartDate;
   	private String propCloseDate;
   	private String actStartDate;
   	private String actCloseDate;
   	private String reportingDate;
   	private String conditions;
   	private String donorObjective;

	private Collection ampFundingDetails;

	private String subtotalActualCommitments;
	private String subtotalPlannedCommitments;
	private String subtotalPipelineCommitments;
	private String subtotalPlannedDisbursements;
	private String subtotalDisbursements;
	private String subtotalPlannedExpenditures;
	private String subtotalExpenditures;
	private String subtotalActualDisbursementsOrders;
	private String undisbursementbalance;

    private float capitalSpendingPercentage;

    public float getCapitalSpendingPercentage() {
        return capitalSpendingPercentage;
    }

    public void setCapitalSpendingPercentage(float capitalSpendingPercentage) {
        this.capitalSpendingPercentage = capitalSpendingPercentage;
    }

	public AmpCategoryValue getTypeOfAssistance() {
		return typeOfAssistance;
	}

	public void setTypeOfAssistance(AmpCategoryValue typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
	}

	public String getOrgFundingId() {
		return orgFundingId;
	}
	
	public void setOrgFundingId(String orgFundingId) {
		this.orgFundingId = orgFundingId;
	}
	
	public String getSignatureDate() {
		return signatureDate;
	}
	
	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}
    public Collection getFundingDetails() {
        return fundingDetails;
    }
    public void setFundingDetails(Collection fundingDetails) {
        this.fundingDetails = fundingDetails;
    }
    public long getFundingId() {
        return fundingId;
    }
    public void setFundingId(long fundingId) {
        this.fundingId = fundingId;
    }
 
    public String getCurrentFunding() {
        return currentFunding;
    }
    
    public void setCurrentFunding(String currentFunding) {
        this.currentFunding = currentFunding;
    }
	/**
	 * @return Returns the actCloseDate.
	 */
	public String getActCloseDate() {
		return actCloseDate;
	}
	/**
	 * @param actCloseDate The actCloseDate to set.
	 */
	public void setActCloseDate(String actCloseDate) {
		this.actCloseDate = actCloseDate;
	}
	/**
	 * @return Returns the actStartDate.
	 */
	public String getActStartDate() {
		return actStartDate;
	}
	/**
	 * @param actStartDate The actStartDate to set.
	 */
	public void setActStartDate(String actStartDate) {
		this.actStartDate = actStartDate;
	}
	/**
	 * @return Returns the propCloseDate.
	 */
	public String getPropCloseDate() {
		return propCloseDate;
	}
	/**
	 * @param propCloseDate The propCloseDate to set.
	 */
	public void setPropCloseDate(String propCloseDate) {
		this.propCloseDate = propCloseDate;
	}
	/**
	 * @return Returns the propStartDate.
	 */
	public String getPropStartDate() {
		return propStartDate;
	}
	/**
	 * @param propStartDate The propStartDate to set.
	 */
	public void setPropStartDate(String propStartDate) {
		this.propStartDate = propStartDate;
	}
	/**
	 * @return Returns the conditions.
	 */
	public String getConditions() {
		return conditions;
	}
	/**
	 * @param conditions The conditions to set.
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	/**
	 * @return Returns the reportingDate.
	 */
	public String getReportingDate() {
		return reportingDate;
	}
	/**
	 * @param reportingDate The reportingDate to set.
	 */
	public void setReportingDate(String reportingDate) {
		this.reportingDate = reportingDate;
	}
	
		
	public AmpCategoryValue getFinancingInstrument() {
		return financingInstrument;
	}

	public void setFinancingInstrument(AmpCategoryValue financingInstrument) {
		this.financingInstrument = financingInstrument;
	}

	public boolean equals(Object e) {
		if (e instanceof Funding) {
			Funding tmp = (Funding) e;
			return fundingId == tmp.fundingId;
		}
		logger.error( "Received an object of class " + e.getClass().getName() + " instead of class Funding");
		throw new ClassCastException();
	}

	public Collection<MTEFProjection> getMtefProjections() {
		return mtefProjections;
	}

	public void setMtefProjections(Collection<MTEFProjection> mtefProjections) {
		this.mtefProjections = mtefProjections;
	}
	
	public String getSubtotalActualCommitments(){
		
		return this.subtotalActualCommitments;
	}
	public void setSubtotalActualCommitments(String s){
		
		this.subtotalActualCommitments = s;
	}
	public String getSubtotalPlannedCommitments(){
		
		return this.subtotalPlannedCommitments;
	}
	public void setSubtotalPlannedCommitments(String s){
		
		this.subtotalPlannedCommitments = s;
	}
	
	public String getSubtotalPlannedDisbursements(){
		
		return this.subtotalPlannedDisbursements;
	}
	public void setSubtotalPlannedDisbursements(String s){
		
		this.subtotalPlannedDisbursements = s;
	}
	public String getSubtotalDisbursements(){
		
		return this.subtotalDisbursements;
	}
	public void setSubtotalDisbursements(String s){
		
		this.subtotalDisbursements = s;
	}
	public String getSubtotalPlannedExpenditures(){
		
		return this.subtotalPlannedExpenditures;
	}
	public void setSubtotalPlannedExpenditures(String s){
		
		this.subtotalPlannedExpenditures = s;
	}
	public String getSubtotalExpenditures(){
		
		return this.subtotalExpenditures;
	}
	public void setSubtotalExpenditures(String s){
		
		this.subtotalExpenditures = s;
	}

	public void setSubtotalActualDisbursementsOrders(
			String s) {
		this.subtotalActualDisbursementsOrders = s;
	}

	public String getSubtotalActualDisbursementsOrders() {
		return this.subtotalActualDisbursementsOrders;
	}

	public void setAmpFundingDetails(Collection fundDetails) {
		this.ampFundingDetails = fundDetails;
	}

	public Collection getAmpFundingDetails() {
		return this.ampFundingDetails;
	}

	public void setUnDisbursementBalance(String formatNumber) {
		this.undisbursementbalance = formatNumber;
		
	}
	public String getUnDisbursementBalance(){
		return this.undisbursementbalance;
	}

	/**
	 * @return the donorObjective
	 */
	public String getDonorObjective() {
		return this.donorObjective;
	}

	/**
	 * @param donorObjective the donorObjective to set
	 */
	public void setDonorObjective(String donorObjective) {
		this.donorObjective = donorObjective;
	}

	public AmpCategoryValue getFundingStatus() {
		return fundingStatus;
	}

	public void setFundingStatus(AmpCategoryValue fundingStatus) {
		this.fundingStatus = fundingStatus;
	}

	public String getSubtotalPipelineCommitments() {
		return subtotalPipelineCommitments;
	}

	public void setSubtotalPipelineCommitments(String subtotalPipelineCommitments) {
		this.subtotalPipelineCommitments = subtotalPipelineCommitments;
	}

	/**
	 * @return the modeOfPayment
	 */
	public AmpCategoryValue getModeOfPayment() {
		return modeOfPayment;
	}

	/**
	 * @param modeOfPayment the modeOfPayment to set
	 */
	public void setModeOfPayment(AmpCategoryValue modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	
}
