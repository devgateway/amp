package org.digijava.module.aim.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils.Null;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
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
	
	DecimalWraper totalCommitments =  new DecimalWraper();

	
	DecimalWraper unDisbursementsBalance =  new DecimalWraper();
	DecimalWraper totalMtef = new DecimalWraper();

	boolean debug;
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public List<FundingDetail> getFundDetailList() {
		return fundDetailList;
	}

	public DecimalWraper getTotPlanDisb() {
		return totPlanDisb;
	}

	public DecimalWraper getTotPlannedComm() {
		return totPlannedComm;
	}

	public DecimalWraper getTotPlannedExp() {
		return totPlannedExp;
	}

	public DecimalWraper getTotPlannedRoF()
	{
		return totPlannedReleaseOfFunds;
	}
	
	public DecimalWraper getTotPlannedEDD()
	{
		return totPlannedEDD;
	}
	
	public DecimalWraper getTotPlannedDisbOrder() {
		return totPlannedDisbOrder;
	}

	public DecimalWraper getTotActualComm() {
		return totActualComm;
	}

	public DecimalWraper getTotActualDisb() {
		return totActualDisb;
	}

	public DecimalWraper getTotActualExp() {
		return totActualExp;
	}

	public DecimalWraper getTotActualDisbOrder() {
		return totActualDisbOrder;
	}

	public DecimalWraper getTotPipelineDisb() {
		return totPipelineDisb;
	}

	public void setTotPipelineDisb(DecimalWraper totPipelineDisb) {
		this.totPipelineDisb = totPipelineDisb;
	}

	public DecimalWraper getTotPipelineExp() {
		return totPipelineExp;
	}

	public void setTotPipelineExp(DecimalWraper totPipelineExp) {
		this.totPipelineExp = totPipelineExp;
	}

	public DecimalWraper getTotPipelineDisbOrder() {
		return totPipelineDisbOrder;
	}

	public void setTotPipelineDisbOrder(DecimalWraper totPipelineDisbOrder) {
		this.totPipelineDisbOrder = totPipelineDisbOrder;
	}

	public DecimalWraper getTotalActualRoF()
	{
		return this.totActualReleaseOfFunds;
	}
	
	public DecimalWraper getTotalActualEDD()
	{
		return this.totActualEDD;
	}
	
	public DecimalWraper getTotalPlannedRoF()
	{
		return this.totPlannedReleaseOfFunds;
	}
	
	public DecimalWraper getTotalPlannedEDD()
	{
		return this.totPlannedEDD;
	}
	
	public DecimalWraper getTotalPipelineRoF()
	{
		return this.totPlannedReleaseOfFunds;
	}
	
	public DecimalWraper getTotalPipelineEDD()
	{
		return this.totPlannedEDD;
	}
	
	public DecimalWraper getTotalMtef()
	{
		return this.totalMtef;
	}
	
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
		
		boolean updateTotals = fundingSource.isDonorFunding(); 
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

		while (fundDetItr.hasNext()) {

			FundingInformationItem fundDet = fundDetItr.next();
			AmpCategoryValue adjType=null;
			if(fundDet.getAdjustmentType() != null) 
				adjType=fundDet.getAdjustmentType();
			else
			{
				try {
					adjType = CategoryManagerUtil.getAmpCategoryValueFromDB( CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.info("", e);
				}
			}	
			FundingDetail fundingDetail = new FundingDetail();
			fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
			String baseCurrCode		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			
			if (fundDet.getFixedExchangeRate() != null && fundDet.getFixedExchangeRate().doubleValue() != 1) {
				// We cannot use FormatHelper.formatNumber as this might roundup our number (and this would be very wrong)
				fundingDetail.setFixedExchangeRate( (fundDet.getFixedExchangeRate()+"").replace(".", FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR)) );
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
				totPlanDisb.setValue(totPlanDisb.getValue().add(amt.getValue()));
				totPlanDisb.setCalculations(totPlanDisb.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {

				totPlannedComm.setValue(totPlannedComm.getValue().add(amt.getValue()));
				totPlannedComm.setCalculations(totPlannedComm.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totPlannedExp.setValue(totPlannedExp.getValue().add(amt.getValue()));
				totPlannedExp.setCalculations(totPlannedExp.getCalculations() + " + " + amt.getCalculations());
			}

			else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totPlannedDisbOrder.setValue(totPlannedDisbOrder.getValue().add(amt.getValue()));
				totPlannedDisbOrder.setCalculations(totPlannedDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} 
			else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totPlannedReleaseOfFunds.setValue(totPlannedReleaseOfFunds.getValue().add(amt.getValue()));
				totPlannedReleaseOfFunds.setCalculations(totPlannedReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totPlannedEDD.setValue(totPlannedEDD.getValue().add(amt.getValue()));
				totPlannedEDD.setCalculations(totPlannedEDD.getCalculations() + " + " + amt.getCalculations());
			}
		} else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {

			//fundingDetail.setAdjustmentTypeName("Actual");
			if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
				totActualComm.setValue(totActualComm.getValue().add(amt.getValue()));
				totActualComm.setCalculations(totActualComm.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
				totActualDisb.setValue(totActualDisb.getValue().add(amt.getValue()));
				totActualDisb.setCalculations(totActualDisb.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totActualExp.setValue(totActualExp.getValue().add(amt.getValue()));
				totActualExp.setCalculations(totActualExp.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totActualDisbOrder.setValue(totActualDisbOrder.getValue().add(amt.getValue()));
				totActualDisbOrder.setCalculations(totActualDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totActualReleaseOfFunds.setValue(totActualReleaseOfFunds.getValue().add(amt.getValue()));
				totActualReleaseOfFunds.setCalculations(totActualReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totActualEDD.setValue(totActualEDD.getValue().add(amt.getValue()));
				totActualEDD.setCalculations(totActualEDD.getCalculations() + " + " + amt.getCalculations());
			}
        } else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey())){
           // fundingDetail.setAdjustmentTypeName("Pipeline");
            if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
            	totPipelineComm.setValue(totPipelineComm.getValue().add(amt.getValue()));
                totPipelineComm.setCalculations(totPipelineComm.getCalculations() + " + " + amt.getCalculations());
            } else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
				totPipelineDisb.setValue(totPipelineDisb.getValue().add(amt.getValue()));
				totPipelineDisb.setCalculations(totPipelineDisb.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totPipelineExp.setValue(totPipelineExp.getValue().add(amt.getValue()));
				totPipelineExp.setCalculations(totPipelineExp.getCalculations() + " + " + amt.getCalculations());

			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totPipelineDisbOrder.setValue(totPipelineDisbOrder.getValue().add(amt.getValue()));
				totPipelineDisbOrder.setCalculations(totPipelineDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totPipelineReleaseOfFunds.setValue(totPipelineReleaseOfFunds.getValue().add(amt.getValue()));
				totPipelineReleaseOfFunds.setCalculations(totPipelineReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totPipelineEDD.setValue(totPipelineEDD.getValue().add(amt.getValue()));
				totPipelineEDD.setCalculations(totPipelineEDD.getCalculations() + " + " + amt.getCalculations());
			}
        }	
	}
	
//	public void doCalculations(Collection<AmpFundingDetail> details, String userCurrencyCode, int transactionType, AmpCategoryValue adjustmentType) {
//		Iterator<AmpFundingDetail> fundDetItr = details.iterator();
//		fundDetailList = new ArrayList<FundingDetail>();
//		int indexId = 0;
//		String toCurrCode = Constants.DEFAULT_CURRENCY;
//
//		while (fundDetItr.hasNext()) {
//
//			
//			AmpFundingDetail fundDet = fundDetItr.next();
//			AmpCategoryValue adjType = null;
//			if(fundDet.getAdjustmentType() != null) 
//				adjType = fundDet.getAdjustmentType();
//			else
//			{
//				try {
//					adjType = CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					logger.info("", e);
//				}
//			}	
//
//			if(adjType.equals(adjustmentType) && fundDet.getTransactionType().intValue() == transactionType){
//				FundingDetail fundingDetail = new FundingDetail();
//				fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
//
//				if (fundDet.getFixedExchangeRate() != null && fundDet.getFixedExchangeRate().doubleValue() != 1) {
//					// We cannot use FormatHelper.formatNumber as this might roundup our number (and this would be very wrong)
//					fundingDetail.setFixedExchangeRate( (fundDet.getFixedExchangeRate()+"").replace(".", FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR)) );
//					fundingDetail.setUseFixedRate(true);
//				}
//				fundingDetail.setIndexId(indexId++);
//				fundingDetail.setAdjustmentTypeName(fundDet.getAdjustmentType());
//				fundingDetail.setContract(fundDet.getContract());
//
//				java.sql.Date dt = new java.sql.Date(fundDet.getTransactionDate().getTime());
//
//				double frmExRt = fundDet.getFixedExchangeRate() != null ? fundDet.getFixedExchangeRate() : Util.getExchange(fundDet.getAmpCurrencyId().getCurrencyCode(), dt);
//
//				if (userCurrencyCode != null)
//					toCurrCode = userCurrencyCode;
//
//				//double toExRt = Util.getExchange(toCurrCode, dt);
//				double toExRt;
//				if (userCurrencyCode != null)
//					toCurrCode = userCurrencyCode;
//				if (fundDet.getAmpCurrencyId().getCurrencyCode().equalsIgnoreCase(toCurrCode)){
//					toExRt=frmExRt;
//				}else{
//					toExRt = Util.getExchange(toCurrCode, dt);
//				}
//				
//				DecimalWraper amt = CurrencyWorker.convertWrapper(fundDet.getTransactionAmount().doubleValue(), frmExRt, toExRt, dt);
//
//				if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
//					fundingDetail.setClassification(fundDet.getExpCategory());
//				}
//				fundingDetail.setCurrencyCode(fundDet.getAmpCurrencyId().getCurrencyCode());
//				fundingDetail.setCurrencyName(fundDet.getAmpCurrencyId().getCountryName());
//
//				fundingDetail.setTransactionAmount(CurrencyWorker.convert(fundDet.getTransactionAmount().doubleValue(), 1, 1));
//				fundingDetail.setTransactionDate(DateConversion.ConvertDateToString(fundDet.getTransactionDate()));
//				fundingDetail.setReportingDate(fundDet.getReportingDate());
//
//				fundingDetail.setTransactionType(fundDet.getTransactionType().intValue());
//				fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
//				if (fundDet.getPledgeid()!= null){
//					fundingDetail.setPledge(fundDet.getPledgeid().getId());
//				}
//				
//				// TOTALS
//				if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
//					//fundingDetail.setAdjustmentTypeName("Planned");
//					if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
//						totPlanDisb.setValue(totPlanDisb.getValue().add(amt.getValue()));
//						totPlanDisb.setCalculations(totPlanDisb.getCalculations() + " + " + amt.getCalculations());
//					} else if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
//
//						totPlannedComm.setValue(totPlannedComm.getValue().add(amt.getValue()));
//						totPlannedComm.setCalculations(totPlannedComm.getCalculations() + " + " + amt.getCalculations());
//
//					} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
//						totPlannedExp.setValue(totPlannedExp.getValue().add(amt.getValue()));
//						totPlannedExp.setCalculations(totPlannedExp.getCalculations() + " + " + amt.getCalculations());
//					}
//
//					else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
//						totPlannedDisbOrder.setValue(totPlannedDisbOrder.getValue().add(amt.getValue()));
//						totPlannedDisbOrder.setCalculations(totPlannedDisbOrder.getCalculations() + " + " + amt.getCalculations());
//					}
//				} else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
//
//					//fundingDetail.setAdjustmentTypeName("Actual");
//					if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
//						totActualComm.setValue(totActualComm.getValue().add(amt.getValue()));
//						totActualComm.setCalculations(totActualComm.getCalculations() + " + " + amt.getCalculations());
//
//					} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
//						totActualDisb.setValue(totActualDisb.getValue().add(amt.getValue()));
//						totActualDisb.setCalculations(totActualDisb.getCalculations() + " + " + amt.getCalculations());
//
//					} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
//						totActualExp.setValue(totActualExp.getValue().add(amt.getValue()));
//						totActualExp.setCalculations(totActualExp.getCalculations() + " + " + amt.getCalculations());
//
//					} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
//						totActualDisbOrder.setValue(totActualDisbOrder.getValue().add(amt.getValue()));
//						totActualDisbOrder.setCalculations(totActualDisbOrder.getCalculations() + " + " + amt.getCalculations());
//					}
//	            } else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey())) {
//	               // fundingDetail.setAdjustmentTypeName("Pipeline");
//	                if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
//	                	totPipelineComm.setValue(totPipelineComm.getValue().add(amt.getValue()));
//	                    totPipelineComm.setCalculations(totPipelineComm.getCalculations() + " + " + amt.getCalculations());
//	                } else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
//						totPipelineDisb.setValue(totPipelineDisb.getValue().add(amt.getValue()));
//						totPipelineDisb.setCalculations(totPipelineDisb.getCalculations() + " + " + amt.getCalculations());
//
//					} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
//						totPipelineExp.setValue(totPipelineExp.getValue().add(amt.getValue()));
//						totPipelineExp.setCalculations(totPipelineExp.getCalculations() + " + " + amt.getCalculations());
//
//					} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
//						totPipelineDisbOrder.setValue(totPipelineDisbOrder.getValue().add(amt.getValue()));
//						totPipelineDisbOrder.setCalculations(totPipelineDisbOrder.getCalculations() + " + " + amt.getCalculations());
//					}
//	            }
//
//				fundDetailList.add(fundingDetail);
//			}
//		}
//
//		totalCommitments = Logic.getInstance().getTotalDonorFundingCalculator().getTotalCommtiments(totPlannedComm, totActualComm, totPipelineComm);
//
//		unDisbursementsBalance = Logic.getInstance().getTotalDonorFundingCalculator().getunDisbursementsBalance(totalCommitments, totActualDisb);
//
//	}

	public DecimalWraper getTotalCommitments() {
		return totalCommitments;
	}

	
	public DecimalWraper getUnDisbursementsBalance() {
		return unDisbursementsBalance;
	}
	
    public DecimalWraper getTotPipelineComm() {
    	return totPipelineComm;
    }
    public void setTotPipelineComm(DecimalWraper totPipelineComm) {
        this.totPipelineComm = totPipelineComm;
    }
}