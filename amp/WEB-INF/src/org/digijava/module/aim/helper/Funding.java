package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lombok.Data;

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
@Data
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
	private String subtotalOfficialDevelopmentAidCommitments;
	private String subtotalBilateralSscCommitments;
	private String subtotalTriangularSscCommitments;
	
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

	public boolean equals(Object e) {
		if (e instanceof Funding) {
			Funding tmp = (Funding) e;
			return fundingId == tmp.fundingId;
		}
		throw new ClassCastException("cannot compare a " + this.getClass().getName() + " instance with a " + e.getClass().getName() + " instance");
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
    
	public Collection<FundingDetail> filterFundings(int transactionType, String adjustmentType)
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

	public Collection<FundingDetail> getOfficialDevelopmentAidCommitmentsDetails(){
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ODA_SSC.getValueKey());
	}
	
	public Collection<FundingDetail> getBilateralSscCommitmentsDetails(){
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC.getValueKey());
	}
	
	public Collection<FundingDetail> getTriangularSscCommitmentsDetails(){
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC.getValueKey());
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


