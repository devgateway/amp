package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

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
	private String sourceRole;
	private String signatureDate;
	//private AmpModality modality;
	private Collection<FundingDetail> fundingDetails;	// Collection of Funding Details
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
	private String subtotalPipelineDisbursements;
	private String subtotalDisbursements;
	private String subtotalPlannedExpenditures;
	private String subtotalPipelineExpenditures;
	private String subtotalExpenditures;
	private String subtotalActualDisbursementsOrders;
	private String subtotalPlannedDisbursementsOrders;
	private String subtotalPipelineDisbursementsOrders;
	private String subtotalActualRoF;
	private String subtotalPlannedRoF;
	private String subtotalPipelineRoF;
	private String subtotalRoF;
	private String subtotalActualEDD;
	private String subtotalPlannedEDD;
	private String subtotalPipelineEDD;
	private String subtotalEDD;
	private String undisbursementbalance;
	private String title;
	private String code;
	
	private Long groupVersionedFunding;

    private Float capitalSpendingPercentage;

    public Float getCapitalSpendingPercentage() {
        return capitalSpendingPercentage;
    }

    public void setCapitalSpendingPercentage(Float capitalSpendingPercentage) {
        this.capitalSpendingPercentage = capitalSpendingPercentage;
    }

	public AmpCategoryValue getTypeOfAssistance() {
		return typeOfAssistance;
	}

	public void setTypeOfAssistance(AmpCategoryValue typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
	}

	/**
	 * Funding Organization Id
	 * @return
	 */
	public String getOrgFundingId() {
		return orgFundingId;
	}

	/**
	 * Funding Organization Id
	 * @return
	 */
	public void setOrgFundingId(String orgFundingId) {
		this.orgFundingId = orgFundingId;
	}
	
	public String getSignatureDate() {
		return signatureDate;
	}
	
	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}
    public Collection<FundingDetail> getFundingDetails() {
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

	public String getSubtotalPlannedDisbursementsOrders() {
		return subtotalPlannedDisbursementsOrders;
	}

	public void setSubtotalPlannedDisbursementsOrders(
			String subtotalPlannedDisbursementsOrders) {
		this.subtotalPlannedDisbursementsOrders = subtotalPlannedDisbursementsOrders;
	}

	public String getSubtotalPipelineDisbursementsOrders() {
		return subtotalPipelineDisbursementsOrders;
	}

	public void setSubtotalPipelineDisbursementsOrders(
			String subtotalPipelineDisbursementsOrders) {
		this.subtotalPipelineDisbursementsOrders = subtotalPipelineDisbursementsOrders;
	}

	public String getSubtotalPipelineDisbursements() {
		return subtotalPipelineDisbursements;
	}

	public void setSubtotalPipelineDisbursements(
			String subtotalPipelineDisbursements) {
		this.subtotalPipelineDisbursements = subtotalPipelineDisbursements;
	}

	public String getSubtotalPipelineExpenditures() {
		return subtotalPipelineExpenditures;
	}

	public void setSubtotalPipelineExpenditures(String subtotalPipelineExpenditures) {
		this.subtotalPipelineExpenditures = subtotalPipelineExpenditures;
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
	
	public Long getGroupVersionedFunding() {
		return groupVersionedFunding;
	}

	public void setGroupVersionedFunding(Long groupVersionedFunding) {
		this.groupVersionedFunding = groupVersionedFunding;
	}
	
	protected Collection<FundingDetail> filterFundings(int transactionType, String adjustmentType)
	{
		if(fundingDetails != null){
			List<FundingDetail> retDetails = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails){
				if(detail.getTransactionType() == transactionType && detail.getAdjustmentTypeName().getValue().equals( adjustmentType))
					retDetails.add(detail);
			}
			return retDetails;
		}
		return null;		
	}
	
	protected Collection<FundingDetail> filterFundings(int transactionType)
	{
		if(fundingDetails != null){
			List<FundingDetail> retDetails = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails){
				if(detail.getTransactionType() == transactionType)
					retDetails.add(detail);
			}
			return retDetails;
		}
		return null;		
	}
	
	public Collection<FundingDetail> getPlannedCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}

	public Collection<FundingDetail> getActualCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}

	public Collection<FundingDetail> getPlannedDisbursementDetails() {
		return filterFundings(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}	
	
	public Collection<FundingDetail> getActualDisbursementDetails() {
		return filterFundings(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineDisbursementDetails() {
		return filterFundings(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}
		
	
	public Collection<FundingDetail> getPlannedExpendituresDetails() {
		return filterFundings(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}
	
	public Collection<FundingDetail> getActualExpendituresDetails() {
		return filterFundings(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineExpendituresDetails() {
		return filterFundings(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}

	public Collection<FundingDetail> getPlannedRoFDetails() {
		return filterFundings(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}
	
	public Collection<FundingDetail> getActualRoFDetails() {
		return filterFundings(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineRoFDetails() {
		return filterFundings(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}
	
	public Collection<FundingDetail> getPlannedEDDDetails() {
		return filterFundings(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}
	
	public Collection<FundingDetail> getActualEDDDetails() {
		return filterFundings(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineEDDDetails() {
		return filterFundings(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}
	
	public Collection<FundingDetail> getCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT);
	}
	
	public Collection<FundingDetail> getDisbursementsDetails() {
		return filterFundings(Constants.DISBURSEMENT);
	}
	
	public Collection<FundingDetail> getDisbursementOrdersDetails() {
		return filterFundings(Constants.DISBURSEMENT_ORDER);
	}

	public Collection<FundingDetail> getExpendituresDetails() {
		return filterFundings(Constants.EXPENDITURE);
	}

	public Collection<FundingDetail> getRoFDetails() {
		return filterFundings(Constants.RELEASE_OF_FUNDS);
	}
	
	public Collection<FundingDetail> getEDDDetails() {
		return filterFundings(Constants.ESTIMATED_DONOR_DISBURSEMENT);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the orgRole
	 */
	public String getSourceRole() {
		return sourceRole;
	}

	/**
	 * @param orgRole the orgRole to set
	 */
	public void setSourceRole(String orgRole) {
		this.sourceRole = orgRole;
	}
	
	// RoF start
	public String getSubtotalPlannedRoF(){
		
		return this.subtotalPlannedRoF;
	}
	
	public void setSubtotalPlannedRoF(String s){
		
		this.subtotalPlannedRoF = s;
	}
	
	public String getSubtotalRoF(){
		
		return this.subtotalRoF;
	}
	
	public void setSubtotalRoF(String s){
		
		this.subtotalRoF = s;
	}
	
	public String getSubtotalActualRoF(){
		
		return this.subtotalActualRoF;
	}
	
	public void setSubtotalActualRoF(String s){
		
		this.subtotalActualRoF = s;
	}
	
	public String getSubtotalPipelineRoF(){
		
		return this.subtotalPipelineRoF;
	}
	
	public void setSubtotalPipelineRoF(String s){
		
		this.subtotalPipelineRoF = s;
	}
	
	// RoF end
	
	// EDD start
	public String getSubtotalPlannedEDD(){
		
		return this.subtotalPlannedEDD;
	}
	
	public void setSubtotalPlannedEDD(String s){
		
		this.subtotalPlannedEDD = s;
	}
	
	public String getSubtotalEDD(){
		
		return this.subtotalEDD;
	}
	
	public void setSubtotalEDD(String s){
		
		this.subtotalEDD = s;
	}
	
	public String getSubtotalActualEDD(){
		
		return this.subtotalActualEDD;
	}
	
	public void setSubtotalActualEDD(String s){
		
		this.subtotalActualEDD = s;
	}
	
	public String getSubtotalPipelineEDD(){
		
		return this.subtotalPipelineEDD;
	}
	
	public void setSubtotalPipelineEDD(String s){
		
		this.subtotalPipelineEDD = s;
	}	
}


