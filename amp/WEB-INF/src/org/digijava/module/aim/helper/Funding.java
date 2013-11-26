package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author jose
 * 
 * funding digest for an AmpFunding instance
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
	private List<FundingDetail> fundingDetails;	// Collection of Funding Details
//	private Collection<MTEFProjection> mtefProjections;
   	private String currentFunding;
   	private String propStartDate;
   	private String propCloseDate;
   	private String actStartDate;
   	private String actCloseDate;
   	private String reportingDate;
   	private String conditions;
   	private String donorObjective;

	private List<FundingInformationItem> ampRawFunding;

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
	private String subtotalMTEFs;
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
    public List<FundingDetail> getFundingDetails() {
        return fundingDetails;
    }
    public void setFundingDetails(List<FundingDetail> fundingDetails) {
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

//	public Collection<MTEFProjection> getMtefProjections() {
//		return mtefProjections;
//	}
//
//	public void setMtefProjections(Collection<MTEFProjection> mtefProjections) {
//		this.mtefProjections = mtefProjections;
//	}
	
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
	
	public String getSubtotalMTEFs()
	{
		return this.subtotalMTEFs;
	}
	
	public void setSubtotalMTEFs(String s)
	{
		this.subtotalMTEFs = s;
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

	public void populateAmpRawFunding(AmpFunding fundingSource) 
	{
		ArrayList<FundingInformationItem> funding = new ArrayList<FundingInformationItem>();
		
		if (fundingSource.getFundingDetails() != null)
			funding.addAll(fundingSource.getFundingDetails());
		
		if (fundingSource.getMtefProjections() != null)
			funding.addAll(fundingSource.getMtefProjections());
		
		this.ampRawFunding = funding;
	}

	public void cleanAmpRawFunding()
	{
		this.ampRawFunding = null;
	}
	
	public List<FundingInformationItem> getAmpRawFunding() {
		return this.ampRawFunding;
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
	
	public Collection<FundingDetail> getMtefDetails()
	{
		return filterFundings(Constants.MTEFPROJECTION);
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
	
	  /**
	   * returns a funding item built and with all its' currency Codes overwritten to a single one
	   * WARNING, BUG! CurrencyName is not overwritten
	   * WARNING 2, BUG 2 - MTEF projections do not have their currency updated anyway - only the totals are
	   * @param ampFunding
	   * @param activityTotalCalculations
	   * @param toCurrCode
	   * @param isPreview
	   * @param tm
	   * @return
	   */
	  public Funding(AmpFunding ampFunding, FundingCalculationsHelper activityTotalCalculations, String toCurrCode, boolean changeToWorkspaceCurrency, TeamMember tm)
	  {
		  //Funding funding = new Funding();

		  //fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
		  this.setTypeOfAssistance(ampFunding.getTypeOfAssistance());
		  this.setFinancingInstrument(ampFunding.getFinancingInstrument());
		  this.setFundingStatus(ampFunding.getFundingStatus());
		  this.setModeOfPayment(ampFunding.getModeOfPayment());

		  this.setActStartDate(DateConversion.ConvertDateToString(ampFunding.getActualStartDate()));
		  this.setActCloseDate(DateConversion.ConvertDateToString(ampFunding.getActualCompletionDate()));

		  this.setFundingId(ampFunding.getAmpFundingId().longValue());
		  this.setGroupVersionedFunding(ampFunding.getGroupVersionedFunding());
		  this.setOrgFundingId(ampFunding.getFinancingId());

		  if (ampFunding.getSourceRole() != null)
			  this.setSourceRole(ampFunding.getSourceRole().getName());

		  this.setConditions(ampFunding.getConditions());
		  this.setDonorObjective(ampFunding.getDonorObjective());
		  this.setCapitalSpendingPercentage(ampFunding.getCapitalSpendingPercentage());
		  if(ampFunding.getAgreement() != null){
			  this.setTitle(ampFunding.getAgreement().getTitle());
			  this.setCode(ampFunding.getAgreement().getCode());
		  }
		  else{
			  this.setCode("");
			  this.setTitle("");
		  }

//		  /* Get MTEF Projections */
//		  ArrayList<MTEFProjection> mtefProjections = new ArrayList<MTEFProjection>();
//		  if (ampFunding.getMtefProjections() != null)
//		  {
//			  Iterator<AmpFundingMTEFProjection> iterMtef	= ampFunding.getMtefProjections().iterator();
//			  while ( iterMtef.hasNext() )
//			  {
//				  AmpFundingMTEFProjection ampProjection		= iterMtef.next();
//				  MTEFProjection	projection					= new MTEFProjection();
//
//				  projection.setAmount( FormatHelper.formatNumber(ampProjection.getAmount()) + "" );
//				  if ( ampProjection.getProjected() != null )
//					  projection.setProjected( ampProjection.getProjected().getId() );
//				  else
//					  logger.error("Projection with date " + ampProjection.getProjectionDate() + " has no type (neither projection nor pipeline) !!!!");
//
//				  projection.setCurrencyCode( ampProjection.getAmpCurrency().getCurrencyCode() );
//				  projection.setCurrencyName( ampProjection.getAmpCurrency().getCurrencyName() );
//				  if (ampProjection.getProjectionDate() != null) {
//					  projection.setProjectionDate(DateConversion.ConvertDateToString(ampProjection.getProjectionDate()));
//					  // projection.setIndex();
//					  projection.setAmpFunding( ampProjection.getAmpFunding() );
//					  mtefProjections.add(projection);
//				  }
//			  }
//		  }
//
//		  Collections.sort(mtefProjections);
//		  this.setMtefProjections(mtefProjections);
//		  /* END - Get MTEF Projections */

		  //Collection<AmpFundingDetail> fundDetails = ampFunding.getFundingDetails();

		  String currencyCode;
		  if (tm != null) {
			  currencyCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId() ).getCurrencyCode();
		  }
		  else {
			  currencyCode = Constants.DEFAULT_CURRENCY;
		  }

		  if (true) // we might also have MTEFs, so no reason to do the "if". Plus, anyway, this will be a NOP if there are no fundings inside
		  {
			  //  Iterator fundDetItr = fundDetails.iterator();
			  // long indexId = System.currentTimeMillis();

			  activityTotalCalculations.doCalculations(ampFunding, toCurrCode);

			  List<FundingDetail> fundDetail = activityTotalCalculations.getFundDetailList();
			  if (changeToWorkspaceCurrency)
			  {
				  Iterator<FundingDetail> fundingIterator = fundDetail.iterator();
				  while(fundingIterator.hasNext())
				  {
					  FundingDetail currentFundingDetail = fundingIterator.next();

					  currentFundingDetail.getContract();
					  Double currencyAppliedAmount;

					  if(currentFundingDetail.getFixedExchangeRate() == null)
					  {

						  currencyAppliedAmount			= getAmountInCurrency(currentFundingDetail, currencyCode );
					  }
					  else
					  {
						  Double fixedExchangeRate = FormatHelper.parseDouble( currentFundingDetail.getFixedExchangeRate() );
						  currencyAppliedAmount = CurrencyWorker.convert1(FormatHelper.parseDouble(currentFundingDetail.getTransactionAmount()),fixedExchangeRate,1);
					  }
					  String currentAmount = FormatHelper.formatNumber(currencyAppliedAmount);
					  currentFundingDetail.setTransactionAmount(currentAmount);
					  currentFundingDetail.setCurrencyCode( currencyCode );

				  }
			  }

			  if (fundDetail != null)
				  Collections.sort(fundDetail, FundingValidator.dateComp);

			  this.setFundingDetails(fundDetail);
			  this.populateAmpRawFunding(ampFunding);
		              // funding.add(fund);
		  }
	  }
	  
	  private double getAmountInCurrency(FundingDetail fundDet, String toCurrCode ) 
	  {
		  java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
		  double frmExRt = Util.getExchange(fundDet.getCurrencyCode(),dt);
		  // String toCurrCode = CurrencyUtil.getAmpcurrency( appSet.getCurrencyId() ).getCurrencyCode();
		  double toExRt = Util.getExchange(toCurrCode, dt);
		  
		  double amt = CurrencyWorker.convert1(FormatHelper.parseDouble(fundDet.getTransactionAmount()),frmExRt,toExRt);
		  return amt;
	  }

}


