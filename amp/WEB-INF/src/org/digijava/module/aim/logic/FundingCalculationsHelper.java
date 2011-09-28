package org.digijava.module.aim.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils.Null;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;

public class FundingCalculationsHelper {

	List<FundingDetail> fundDetailList = new ArrayList<FundingDetail>();

	DecimalWraper totPlanDisb = new DecimalWraper();
	DecimalWraper totPlannedComm = new DecimalWraper();
	DecimalWraper totPlannedExp = new DecimalWraper();
	DecimalWraper totPlannedDisbOrder = new DecimalWraper();

	DecimalWraper totActualComm = new DecimalWraper();
	DecimalWraper totPipelineComm = new DecimalWraper();
	DecimalWraper totActualDisb = new DecimalWraper();
	DecimalWraper totActualExp = new DecimalWraper();
	DecimalWraper totActualDisbOrder = new DecimalWraper();

	DecimalWraper totalCommitments =  new DecimalWraper();

	
	DecimalWraper unDisbursementsBalance =  new DecimalWraper();

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

	public void doCalculations(Collection<AmpFundingDetail> details, String userCurrencyCode) {
		Iterator<AmpFundingDetail> fundDetItr = details.iterator();
		fundDetailList = new ArrayList<FundingDetail>();
		int indexId = 0;
		String toCurrCode = Constants.DEFAULT_CURRENCY;

		while (fundDetItr.hasNext()) {

			AmpFundingDetail fundDet = fundDetItr.next();
			int adjType = 1;
			if(fundDet.getAdjustmentType() != null) adjType=fundDet.getAdjustmentType().intValue();
			FundingDetail fundingDetail = new FundingDetail();
			fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());

			if (fundDet.getFixedExchangeRate() != null && fundDet.getFixedExchangeRate().doubleValue() != 1) {
				// We cannot use FormatHelper.formatNumber as this might roundup our number (and this would be very wrong)
				fundingDetail.setFixedExchangeRate( (fundDet.getFixedExchangeRate()+"").replace(".", FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR)) );
				fundingDetail.setUseFixedRate(true);
			}
			fundingDetail.setIndexId(indexId++);
			fundingDetail.setAdjustmentType(adjType);
			fundingDetail.setContract(fundDet.getContract());

			java.sql.Date dt = new java.sql.Date(fundDet.getTransactionDate().getTime());

			double frmExRt = fundDet.getFixedExchangeRate() != null ? fundDet.getFixedExchangeRate() : Util.getExchange(fundDet.getAmpCurrencyId().getCurrencyCode(), dt);

			if (userCurrencyCode != null)
				toCurrCode = userCurrencyCode;

			double toExRt = Util.getExchange(toCurrCode, dt);
			DecimalWraper amt = CurrencyWorker.convertWrapper(fundDet.getTransactionAmount().doubleValue(), frmExRt, toExRt, dt);

			if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				fundingDetail.setClassification(fundDet.getExpCategory());
			}
			fundingDetail.setCurrencyCode(fundDet.getAmpCurrencyId().getCurrencyCode());
			fundingDetail.setCurrencyName(fundDet.getAmpCurrencyId().getCountryName());

			fundingDetail.setTransactionAmount(CurrencyWorker.convert(fundDet.getTransactionAmount().doubleValue(), 1, 1));
			fundingDetail.setTransactionDate(DateConversion.ConvertDateToString(fundDet.getTransactionDate()));
			fundingDetail.setReportingDate(fundDet.getReportingDate());

			fundingDetail.setTransactionType(fundDet.getTransactionType().intValue());
			fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
			if (fundDet.getPledgeid()!= null){
				fundingDetail.setPledge(fundDet.getPledgeid().getId());
			}
			
			// TOTALS
			if (adjType == Constants.PLANNED) {
				fundingDetail.setAdjustmentTypeName("Planned");
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
			} else if (adjType == Constants.ACTUAL) {

				fundingDetail.setAdjustmentTypeName("Actual");
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
				}
            } else if (adjType == Constants.ADJUSTMENT_TYPE_PIPELINE) {
                fundingDetail.setAdjustmentTypeName("Pipeline");
                if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
                	totPipelineComm.setValue(totPipelineComm.getValue().add(amt.getValue()));
                    totPipelineComm.setCalculations(totPipelineComm.getCalculations() + " + " + amt.getCalculations());
                }
            }

			fundDetailList.add(fundingDetail);
		}

		totalCommitments = Logic.getInstance().getTotalDonorFundingCalculator().getTotalCommtiments(totPlannedComm, totActualComm, totPipelineComm);

		unDisbursementsBalance = Logic.getInstance().getTotalDonorFundingCalculator().getunDisbursementsBalance(totalCommitments, totActualDisb);

	}
	public void doCalculations(Collection<AmpFundingDetail> details, String userCurrencyCode, int transactionType, int adjustmentType) {
		Iterator<AmpFundingDetail> fundDetItr = details.iterator();
		fundDetailList = new ArrayList<FundingDetail>();
		int indexId = 0;
		String toCurrCode = Constants.DEFAULT_CURRENCY;

		while (fundDetItr.hasNext()) {

			AmpFundingDetail fundDet = fundDetItr.next();
			int adjType = 1;
			if(fundDet.getAdjustmentType() != null) adjType=fundDet.getAdjustmentType().intValue();

			if(adjType == adjustmentType && fundDet.getTransactionType().intValue() == transactionType){
				FundingDetail fundingDetail = new FundingDetail();
				fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());

				if (fundDet.getFixedExchangeRate() != null && fundDet.getFixedExchangeRate().doubleValue() != 1) {
					// We cannot use FormatHelper.formatNumber as this might roundup our number (and this would be very wrong)
					fundingDetail.setFixedExchangeRate( (fundDet.getFixedExchangeRate()+"").replace(".", FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR)) );
					fundingDetail.setUseFixedRate(true);
				}
				fundingDetail.setIndexId(indexId++);
				fundingDetail.setAdjustmentType(adjType);
				fundingDetail.setContract(fundDet.getContract());

				java.sql.Date dt = new java.sql.Date(fundDet.getTransactionDate().getTime());

				double frmExRt = fundDet.getFixedExchangeRate() != null ? fundDet.getFixedExchangeRate() : Util.getExchange(fundDet.getAmpCurrencyId().getCurrencyCode(), dt);

				if (userCurrencyCode != null)
					toCurrCode = userCurrencyCode;

				double toExRt = Util.getExchange(toCurrCode, dt);
				DecimalWraper amt = CurrencyWorker.convertWrapper(fundDet.getTransactionAmount().doubleValue(), frmExRt, toExRt, dt);

				if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
					fundingDetail.setClassification(fundDet.getExpCategory());
				}
				fundingDetail.setCurrencyCode(fundDet.getAmpCurrencyId().getCurrencyCode());
				fundingDetail.setCurrencyName(fundDet.getAmpCurrencyId().getCountryName());

				fundingDetail.setTransactionAmount(CurrencyWorker.convert(fundDet.getTransactionAmount().doubleValue(), 1, 1));
				fundingDetail.setTransactionDate(DateConversion.ConvertDateToString(fundDet.getTransactionDate()));
				fundingDetail.setReportingDate(fundDet.getReportingDate());

				fundingDetail.setTransactionType(fundDet.getTransactionType().intValue());
				fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
				if (fundDet.getPledgeid()!= null){
					fundingDetail.setPledge(fundDet.getPledgeid().getId());
				}
				
				// TOTALS
				if (adjType == Constants.PLANNED) {
					fundingDetail.setAdjustmentTypeName("Planned");
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
				} else if (adjType == Constants.ACTUAL) {

					fundingDetail.setAdjustmentTypeName("Actual");
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
					}
	            } else if (adjType == Constants.ADJUSTMENT_TYPE_PIPELINE) {
	                fundingDetail.setAdjustmentTypeName("Pipeline");
	                if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
	                	totPipelineComm.setValue(totPipelineComm.getValue().add(amt.getValue()));
	                    totPipelineComm.setCalculations(totPipelineComm.getCalculations() + " + " + amt.getCalculations());
	                }
	            }

				fundDetailList.add(fundingDetail);
			}
		}

		totalCommitments = Logic.getInstance().getTotalDonorFundingCalculator().getTotalCommtiments(totPlannedComm, totActualComm, totPipelineComm);

		unDisbursementsBalance = Logic.getInstance().getTotalDonorFundingCalculator().getunDisbursementsBalance(totalCommitments, totActualDisb);

	}

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