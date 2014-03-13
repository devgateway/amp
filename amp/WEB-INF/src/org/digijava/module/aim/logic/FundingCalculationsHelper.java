package org.digijava.module.aim.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import lombok.Data;

import org.apache.commons.lang.ObjectUtils.Null;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.RegionalFundingsHelper;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.sun.istack.logging.Logger;

@Data
public class FundingCalculationsHelper {
	
	private static Logger logger = Logger.getLogger(FundingCalculationsHelper.class); 

	List<FundingDetail> fundDetailList = new ArrayList<FundingDetail>();

	DecimalWraper totPlanDisb = new DecimalWraper();
	DecimalWraper totPlannedComm = new DecimalWraper();
	DecimalWraper totPlannedExp = new DecimalWraper();
	DecimalWraper totPlannedDisbOrder = new DecimalWraper();
	DecimalWraper totPlannedReleaseOfFunds = new DecimalWraper();
	DecimalWraper totPlannedEDD = new DecimalWraper();

	DecimalWraper totActualComm = new DecimalWraper();
	DecimalWraper totActualDisb = new DecimalWraper();
	DecimalWraper totActualExp = new DecimalWraper();
	DecimalWraper totActualDisbOrder = new DecimalWraper();
	DecimalWraper totActualReleaseOfFunds = new DecimalWraper();
	DecimalWraper totActualEDD = new DecimalWraper();	

	DecimalWraper totPipelineDisb = new DecimalWraper();
	DecimalWraper totPipelineComm = new DecimalWraper();
	DecimalWraper totPipelineExp = new DecimalWraper();
	DecimalWraper totPipelineDisbOrder = new DecimalWraper();
	DecimalWraper totPipelineReleaseOfFunds = new DecimalWraper();
	DecimalWraper totPipelineEDD = new DecimalWraper();

	DecimalWraper totOdaSscComm = new DecimalWraper();
	DecimalWraper totBilateralSscComm = new DecimalWraper();
	DecimalWraper totTriangularSscComm = new DecimalWraper();
	
	/**
	 * DO NOT CALCULATE SSC STUFF HERE!
	 */
	DecimalWraper totalCommitments =  new DecimalWraper();

	
	DecimalWraper unDisbursementsBalance =  new DecimalWraper();
	DecimalWraper totalMtef = new DecimalWraper();

	boolean debug;
	
	/**
	 * extracts all the donor funding + MTEF funding from a source and adds them into a single source; then calculates the totals <br />
	 * also resets the internal {@link #getFundDetailList()}
	 * @param fundingSource
	 * @param userCurrencyCode
	 */
	public void doCalculations(AmpFunding fundingSource, String userCurrencyCode)
	{
		ArrayList<FundingInformationItem> funding = new ArrayList<FundingInformationItem>();
		
		if (fundingSource.getFundingDetails() != null)
			funding.addAll(fundingSource.getFundingDetails());
		
		if (fundingSource.getMtefProjections() != null)
			funding.addAll(fundingSource.getMtefProjections());
		
		boolean updateTotals = fundingSource.isCountedInTotals(); 
		doCalculations(funding, userCurrencyCode, updateTotals);
	}
	
	/**
	 * also resets the internal {@link #getFundDetailList()}
	 * @param details
	 * @param userCurrencyCode
	 * @param updateTotals - if false, then only fundDetailList will be built, without updating the totals
	 */
	public void doCalculations(Collection<? extends FundingInformationItem> details, String userCurrencyCode, boolean updateTotals) {
		Iterator<? extends FundingInformationItem> fundDetItr = details.iterator();
		fundDetailList = new ArrayList<FundingDetail>();
		int indexId = 0;
		String toCurrCode = Constants.DEFAULT_CURRENCY;
		AmpCategoryValue actualAdjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB();
		if (actualAdjustmentType == null)
		{
			throw new RuntimeException("ACTUAL adjustment type not found in the database");
		}
		
		String decimalSeparatorStr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR); 
		char decimalSeparatorChar = decimalSeparatorStr == null ? '.' : (decimalSeparatorStr.isEmpty() ? '.' : decimalSeparatorStr.charAt(0));
		
		while (fundDetItr.hasNext()) {

			FundingInformationItem fundDet = fundDetItr.next();
			AmpCategoryValue adjType = null;
			if(fundDet.getAdjustmentType() != null) 
				adjType = fundDet.getAdjustmentType();
			else
			{
				adjType = actualAdjustmentType;
			}	
			FundingDetail fundingDetail = new FundingDetail();
			fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
            if (fundDet instanceof AmpFundingDetail) {
                fundingDetail.setFundDetId(((AmpFundingDetail)fundDet).getAmpFundDetailId());
            } else if (fundDet instanceof AmpFundingMTEFProjection) {
                fundingDetail.setFundDetId(((AmpFundingMTEFProjection)fundDet).getAmpFundingMTEFProjectionId());
            }
//			String baseCurrCode		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			
			if (fundDet.getFixedExchangeRate() != null && fundDet.getFixedExchangeRate().doubleValue() != 1) {
				// We cannot use FormatHelper.formatNumber as this might roundup our number (and this would be very wrong)
				fundingDetail.setFixedExchangeRate( (fundDet.getFixedExchangeRate().toString()).replace('.', decimalSeparatorChar) );
				fundingDetail.setUseFixedRate(true);
			}
			fundingDetail.setIndexId(indexId++);
			fundingDetail.setAdjustmentTypeName(fundDet.getAdjustmentType());
			fundingDetail.setContract(fundDet.getContract());

			java.sql.Date dt = new java.sql.Date(fundDet.getTransactionDate().getTime());
			
			Double fixedExchangeRate = fundDet.getFixedExchangeRate();
			if (fixedExchangeRate != null && (Math.abs(fixedExchangeRate.doubleValue()) < 1e-15))
				fixedExchangeRate = null;
			
			double frmExRt;
			if ( fixedExchangeRate == null){
				frmExRt = Util.getExchange(fundDet.getAmpCurrencyId().getCurrencyCode(), dt);
			}else{
				frmExRt = fixedExchangeRate;
			}
			
			double toExRt;
			if (userCurrencyCode != null)
				toCurrCode = userCurrencyCode;
			if (fundDet.getAmpCurrencyId().getCurrencyCode().equalsIgnoreCase(toCurrCode)){
				toExRt=frmExRt;
			}else{
				toExRt = Util.getExchange(toCurrCode, dt);
			}
			
			DecimalWraper amt = CurrencyWorker.convertWrapper(fundDet.getTransactionAmount().doubleValue(), frmExRt, toExRt, dt);

			if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				fundingDetail.setClassification(fundDet.getExpCategory());
			}
			fundingDetail.setCurrencyCode(fundDet.getAmpCurrencyId().getCurrencyCode());
			fundingDetail.setCurrencyName(fundDet.getAmpCurrencyId().getCountryName());

			fundingDetail.setTransactionAmount(CurrencyWorker.convert(fundDet.getTransactionAmount().doubleValue(), 1, 1));
			fundingDetail.setTransactionDate(DateConversion.ConvertDateToString(fundDet.getTransactionDate()));
			fundingDetail.setCapitalPercent(fundDet.getCapitalSpendingPercentage());
			fundingDetail.setReportingDate(fundDet.getReportingDate());
			fundingDetail.setRecipientOrganisation(fundDet.getRecipientOrg());
			fundingDetail.setRecipientOrganisationRole(fundDet.getRecipientRole());

			fundingDetail.setTransactionType(fundDet.getTransactionType().intValue());
			fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
			if (fundDet.getPledgeid()!= null){
				fundingDetail.setPledge(fundDet.getPledgeid().getId());
			}
						
			// TOTALS
			if (updateTotals)
				addToTotals(adjType, fundDet, amt);

			fundDetailList.add(fundingDetail);
		}

		totalCommitments = Logic.getInstance().getTotalDonorFundingCalculator().getTotalCommtiments(totPlannedComm, totActualComm, totPipelineComm);

		unDisbursementsBalance = Logic.getInstance().getTotalDonorFundingCalculator().getunDisbursementsBalance(totalCommitments, totActualDisb);

	}
	
	protected void addToTotals(AmpCategoryValue adjType, FundingInformationItem fundDet, DecimalWraper amt)
	{
		/**
		 * no adjustment type for MTEF transactions, so this "if" is outside the PLANNED / ACTUAL / PIPELINE branching if's
		 */
		if (fundDet.getTransactionType().intValue() == Constants.MTEFPROJECTION)
		{
			totalMtef.setValue(totalMtef.getValue().add(amt.getValue()));
			return;
		}

		if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()) ) {
			//fundingDetail.setAdjustmentTypeName("Planned");
			if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
				totPlanDisb.add(amt);
				//totPlanDisb.setCalculations(totPlanDisb.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
				totPlannedComm.add(amt);
				//totPlannedComm.setCalculations(totPlannedComm.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totPlannedExp.add(amt);
				//totPlannedExp.setCalculations(totPlannedExp.getCalculations() + " + " + amt.getCalculations());
			}

			else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totPlannedDisbOrder.add(amt);
				//totPlannedDisbOrder.setCalculations(totPlannedDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} 
			else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totPlannedReleaseOfFunds.add(amt);
				//totPlannedReleaseOfFunds.setCalculations(totPlannedReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totPlannedEDD.add(amt);
				//totPlannedEDD.setCalculations(totPlannedEDD.getCalculations() + " + " + amt.getCalculations());
			}
		} else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {

			//fundingDetail.setAdjustmentTypeName("Actual");
			if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
				totActualComm.add(amt);
				//totActualComm.setCalculations(totActualComm.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
				totActualDisb.add(amt);
				//totActualDisb.setCalculations(totActualDisb.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totActualExp.add(amt);
				//totActualExp.setCalculations(totActualExp.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totActualDisbOrder.add(amt);
				//totActualDisbOrder.setCalculations(totActualDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totActualReleaseOfFunds.add(amt);
				//totActualReleaseOfFunds.setCalculations(totActualReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totActualEDD.add(amt);
				//totActualEDD.setCalculations(totActualEDD.getCalculations() + " + " + amt.getCalculations());
			}
        } else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey())){
           // fundingDetail.setAdjustmentTypeName("Pipeline");
            if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
            	totPipelineComm.add(amt);
               // totPipelineComm.setCalculations(totPipelineComm.getCalculations() + " + " + amt.getCalculations());
            } else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
				totPipelineDisb.add(amt);
				//totPipelineDisb.setCalculations(totPipelineDisb.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totPipelineExp.add(amt);
				//totPipelineExp.setCalculations(totPipelineExp.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totPipelineDisbOrder.add(amt);
				//totPipelineDisbOrder.setCalculations(totPipelineDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totPipelineReleaseOfFunds.add(amt);
				//totPipelineReleaseOfFunds.setCalculations(totPipelineReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totPipelineEDD.add(amt);
				//totPipelineEDD.setCalculations(totPipelineEDD.getCalculations() + " + " + amt.getCalculations());
			}
        } else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ODA_SSC.getValueKey()))
        {
        	if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT)
        		totOdaSscComm.add(amt);
        } else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC.getValueKey()))
        {
        	if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT)
        		totBilateralSscComm.add(amt);
        } else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC.getValueKey()))
        {
        	if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT)
        		totTriangularSscComm.add(amt);
        }
	}
}
