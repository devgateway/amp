package org.dgfoundation.amp.onepager.models;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpOverallFundingModel implements IModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4602506401806485678L;
	private IModel<Set<AmpFunding>> fundingModel;
	private int transactionType;
	String adjustmentType;

	public AmpOverallFundingModel(IModel<Set<AmpFunding>> fundingModel,
			int transactionType, String adjustmentType) {
		this.fundingModel = fundingModel;
		this.transactionType = transactionType;
		this.adjustmentType = adjustmentType;
	}

	@Override
	public void detach() {
		fundingModel.detach();
	}

	@Override
	public String getObject() {
//		String title="";
//		if(transactionType==Constants.DISBURSEMENT){
//			title="Total Actual Disbursement";
//		}else{
//			if(transactionType==Constants.EXPENDITURE){ 
//				title="Total Actual Expenditures";
//			}else{
//				title="";
//			}
//		}
		String toCurrCode = Constants.DEFAULT_CURRENCY;
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession()
				.getAttribute("currentMember");

		if (tm != null) {
			toCurrCode = CurrencyUtil.getAmpcurrency(
					tm.getAppSettings().getCurrencyId()).getCurrencyCode();
		}
			return doCalculations(toCurrCode).toString() + " "+toCurrCode;
	}

	@Override
	public void setObject(Object object) {
		throw new AssertionError("Shouldn't be used!");
	}

	private DecimalWraper doCalculations(String toCurrCode) {
		DecimalWraper amount = new DecimalWraper();

		for (AmpFunding funding : fundingModel.getObject()) {
			for (AmpFundingDetail fundingDetail : funding.getFundingDetails()) {
				if (fundingDetail.getAdjustmentType()!=null && fundingDetail.getAdjustmentType().getValue()!=null 
						&& fundingDetail.getAdjustmentType().getValue()
						.equals(adjustmentType)
						&& fundingDetail.getTransactionType().intValue() == transactionType) {

					if (fundingDetail.getAmpCurrencyId() != null
							&& fundingDetail.getTransactionAmount() != null
							&& fundingDetail.getTransactionDate() != null) {
						amount.add(getDecimalWraper(fundingDetail, toCurrCode));

					}
				}
			}

		}
		return amount;
	}

	private DecimalWraper getDecimalWraper(AmpFundingDetail fundDet,
			String toCurrCode) {
		java.sql.Date dt = new java.sql.Date(fundDet.getTransactionDate()
				.getTime());

		Double fixedExchangeRate = fundDet.getFixedExchangeRate();
		if (fixedExchangeRate != null
				&& (Math.abs(fixedExchangeRate.doubleValue()) < 1.0E-15))
			fixedExchangeRate = null;
		double frmExRt;
		if (fixedExchangeRate == null) {
			frmExRt = Util.getExchange(fundDet.getAmpCurrencyId()
					.getCurrencyCode(), dt);
		} else {
			frmExRt = fixedExchangeRate;
		}
		double toExRt;

		if (fundDet.getAmpCurrencyId().getCurrencyCode()
				.equalsIgnoreCase(toCurrCode)) {
			toExRt = frmExRt;
		} else {
			toExRt = Util.getExchange(toCurrCode, dt);
		}
		return CurrencyWorker.convertWrapper(fundDet.getTransactionAmount()
				.doubleValue(), frmExRt, toExRt, dt);

	}

}
