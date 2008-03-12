package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class FundingAdded extends Action {

	private static Logger logger = Logger.getLogger(FundingAdded.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		
		boolean perspectiveEnabled = FeaturesUtil.isPerspectiveEnabled();

		Iterator fundOrgsItr = eaForm.getFundingOrganizations().iterator();
		FundingOrganization fundOrg = null;
		boolean found = false;
		int fundOrgOffset = 0;
		while (fundOrgsItr.hasNext()) {
			fundOrg = (FundingOrganization) fundOrgsItr.next();
			if (fundOrg.getAmpOrgId().equals(eaForm.getOrgId())) {
				found = true;
				break;
			}
			fundOrgOffset++;
		}

		int offset = -1;
		Collection oldFundDetails = null;
		if (found) {
			if (eaForm.isEditFunding()) {
				offset = eaForm.getOffset();
				ArrayList fundList = new ArrayList(fundOrg.getFundings());
				Funding fs = (Funding) fundList.get(offset);
				oldFundDetails = fs.getFundingDetails();
				fundList.set(offset, null);
				fundOrg.setFundings(fundList);
			}
		}

		double totComm = FormatHelper.parseDouble(eaForm.getTotalCommitments());
		double totDisb =FormatHelper.parseDouble(eaForm.getTotalDisbursements());
		double totExp = FormatHelper.parseDouble(eaForm.getTotalExpenditures());

		Funding newFund = new Funding();

		if (eaForm.getFundingId() != null && eaForm.getFundingId().longValue() > 0) {
			newFund.setFundingId(eaForm.getFundingId().longValue());
		} else {
			newFund.setFundingId(System.currentTimeMillis());
		}
		//newFund.setAmpTermsAssist(DbUtil.getAssistanceType(eaForm.getAssistanceType()));
		newFund.setTypeOfAssistance( CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getAssistanceType()) );
		newFund.setOrgFundingId(eaForm.getOrgFundingId());
		newFund.setFinancingInstrument(CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getModality()));
		newFund.setConditions(eaForm.getFundingConditions());

		Collection fundDetails = new ArrayList();
		if (eaForm.getFundingDetails() != null) {
			Iterator itr = eaForm.getFundingDetails().iterator();
			while (itr.hasNext()) {
				FundingDetail fundDet = (FundingDetail) itr.next();
				String formattedAmt = CurrencyWorker.formatAmount(
						fundDet.getTransactionAmount());
				fundDet.setTransactionAmount(formattedAmt);
				
				if (fundDet.getCurrencyCode() != null
						&& fundDet.getCurrencyCode().trim().length() != 0) {
					AmpCurrency currency = CurrencyUtil.getCurrencyByCode(fundDet
							.getCurrencyCode());
					fundDet.setCurrencyName(currency.getCountryName());
				}
				if (fundDet.getReportingOrganizationId() != null
						&& fundDet.getReportingOrganizationId().intValue() != 0) {
					AmpOrganisation org = DbUtil.getOrganisation(fundDet
							.getReportingOrganizationId());
					fundDet.setReportingOrganizationName(org.getName());
				}
				if(perspectiveEnabled){
					String perspective = fundDet.getPerspectiveCode();
					Iterator itr1 = eaForm.getPerspectives().iterator();
					while (itr1.hasNext()) {
						AmpPerspective pers = (AmpPerspective) itr1.next();
						if (pers.getCode().equals(perspective)) {
							fundDet.setPerspectiveName(pers.getName());
						}
					}
				}else{
					fundDet.setPerspectiveName("MOFED");
				}
				if (fundDet.getAdjustmentType() == Constants.PLANNED)
					fundDet.setAdjustmentTypeName("Planned");
				else if (fundDet.getAdjustmentType() == Constants.ACTUAL) {
					fundDet.setAdjustmentTypeName("Actual");
					java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
					double frmExRt = Util.getExchange(fundDet.getCurrencyCode(),dt);
					String toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
					eaForm.setCurrCode(toCurrCode);
					double toExRt = Util.getExchange(toCurrCode,dt);
					double amt = CurrencyWorker.convert1(FormatHelper.parseDouble(fundDet.getTransactionAmount()),frmExRt,toExRt);
					if (fundDet.getTransactionType() == Constants.COMMITMENT)
						totComm += amt;
					else if (fundDet.getTransactionType() == Constants.DISBURSEMENT)
						totDisb += amt;
					else if (fundDet.getTransactionType() == Constants.EXPENDITURE)
						totExp += amt;
				}
				fundDetails.add(fundDet);
			}
			
			// Substract the old fundDetails of the funding we just added
			if ((found)&&(oldFundDetails != null)&&(eaForm.isEditFunding())) {
				itr = oldFundDetails.iterator();
				while (itr.hasNext()) {
					FundingDetail fundDet = (FundingDetail) itr.next();
					String formattedAmt = CurrencyWorker.formatAmount(
							fundDet.getTransactionAmount());
					fundDet.setTransactionAmount(formattedAmt);
					
					if (fundDet.getAdjustmentType() == Constants.PLANNED)
						fundDet.setAdjustmentTypeName("Planned");
					else if (fundDet.getAdjustmentType() == Constants.ACTUAL) {
						fundDet.setAdjustmentTypeName("Actual");
						java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
						double frmExRt = Util.getExchange(fundDet.getCurrencyCode(),dt);
						String toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
						eaForm.setCurrCode(toCurrCode);
						double toExRt = Util.getExchange(toCurrCode,dt);
						double amt = CurrencyWorker.convert1(DecimalToText.getDouble(fundDet.getTransactionAmount()),frmExRt,toExRt);
						if (fundDet.getTransactionType() == Constants.COMMITMENT)
							totComm -= amt;
						else if (fundDet.getTransactionType() == Constants.DISBURSEMENT)
							totDisb -= amt;
						else if (fundDet.getTransactionType() == Constants.EXPENDITURE)
							totExp -= amt;
					}
				}
			}
			
		}
		
		Collection mtefProjections=new ArrayList();
		if (eaForm.getFundingMTEFProjections() != null) {
			Iterator itr = eaForm.getFundingMTEFProjections().iterator();
			while (itr.hasNext()) {
				MTEFProjection mtef = (MTEFProjection) itr.next();
			
				if ( mtef.getAmount() == null ) //This MTEFProjection has been created in AddFunding action 
						continue;				// but if projections are disabled then the amount will be empty so this shouldn't be taken into consideration
				String formattedAmt = CurrencyWorker.formatAmount(
						mtef.getAmount());
				mtef.setAmount(formattedAmt);
				if (mtef.getCurrencyCode() != null
						&& mtef.getCurrencyCode().trim().length() != 0) {
					AmpCurrency currency = CurrencyUtil.getCurrencyByCode(mtef.getCurrencyCode());
					mtef.setCurrencyName(currency.getCountryName());
				}
				if (mtef.getReportingOrganizationId() != null
						&& mtef.getReportingOrganizationId().intValue() != 0) {
					AmpOrganisation org = DbUtil.getOrganisation(mtef
							.getReportingOrganizationId());
					mtef.setReportingOrganizationName(org.getName());
				
				}
				mtefProjections.add(mtef);
			}
		}
		
		List sortedList = new ArrayList(fundDetails);
		Collections.sort(sortedList,FundingValidator.dateComp);

		newFund.setFundingDetails(sortedList);
		newFund.setMtefProjections(mtefProjections);
		ArrayList fundList = new ArrayList();
		if (fundOrg.getFundings() != null) {
			fundList = new ArrayList(fundOrg.getFundings());
		}

		if (offset != -1)
			fundList.set(offset, newFund);
		else
			fundList.add(newFund);

		eaForm.setDupFunding(false);
		eaForm.setFirstSubmit(false);

		if (eaForm.getFundingDetails() != null)
		{
			int i=0;
			Iterator fundItr1 = eaForm.getFundingDetails().iterator();
			while(fundItr1.hasNext())
			{
				i++;
				FundingDetail fundDetItr1 = (FundingDetail) fundItr1.next();
				Iterator fundItr2 = eaForm.getFundingDetails().iterator();
				int j=0;
				while(fundItr2.hasNext())
				{
					j++;
					FundingDetail fundDetItr2 = (FundingDetail) fundItr2.next();
					if(j>i)
					{
						if((fundDetItr2.getAdjustmentTypeName().equalsIgnoreCase(fundDetItr1.getAdjustmentTypeName()))&&
						(fundDetItr2.getCurrencyName().equalsIgnoreCase(fundDetItr1.getCurrencyName()))&&
						( !perspectiveEnabled || perspectiveEnabled && fundDetItr2.getPerspectiveName().equalsIgnoreCase(fundDetItr1.getPerspectiveName()) )&&
						(fundDetItr2.getTransactionAmount().equalsIgnoreCase(fundDetItr1.getTransactionAmount()))&&
						(fundDetItr2.getTransactionDate().equalsIgnoreCase(fundDetItr1.getTransactionDate()))&&
						(fundDetItr2.getTransactionType()==fundDetItr1.getTransactionType()))
						{
							eaForm.setDupFunding(true);
							eaForm.setFirstSubmit(true);
						}
					}
				}
			}
		}

		fundOrg.setFundings(fundList);
		ArrayList fundingOrgs = new ArrayList();
		if (eaForm.getFundingOrganizations() != null) {
			fundingOrgs = new ArrayList(eaForm.getFundingOrganizations());
			fundingOrgs.set(fundOrgOffset, fundOrg);
		}
		eaForm.setTotalCommitments(FormatHelper.formatNumber(totComm));
		eaForm.setTotalDisbursements(FormatHelper.formatNumber(totDisb));
		eaForm.setTotalExpenditures(FormatHelper.formatNumber(totExp));
		eaForm.setStep("3");

		return mapping.findForward("forward");
	}
}
