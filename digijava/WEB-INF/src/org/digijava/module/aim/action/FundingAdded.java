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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class FundingAdded extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		
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

		if (found) {
			if (eaForm.isEditFunding()) {
				offset = eaForm.getOffset();
				ArrayList fundList = new ArrayList(fundOrg.getFundings());
				fundList.set(offset, null);
				fundOrg.setFundings(fundList);
			}
		}

		double totComm = eaForm.getTotalCommitments();
		double totDisb = eaForm.getTotalDisbursements();
		double totExp = eaForm.getTotalExpenditures();
		
		Funding newFund = new Funding();
		
		if (eaForm.getFundingId() != null && eaForm.getFundingId().longValue() > 0) {
			newFund.setFundingId(eaForm.getFundingId().longValue());
		} else {
			newFund.setFundingId(System.currentTimeMillis());	
		}
		newFund.setAmpTermsAssist(DbUtil.getAssistanceType(eaForm
				.getAssistanceType()));
		newFund.setOrgFundingId(eaForm.getOrgFundingId());
		newFund.setModality(DbUtil.getModality(eaForm.getModality()));
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
				String perspective = fundDet.getPerspectiveCode();
				Iterator itr1 = eaForm.getPerspectives().iterator();
				while (itr1.hasNext()) {
					AmpPerspective pers = (AmpPerspective) itr1.next();
					if (pers.getCode().equals(perspective)) {
						fundDet.setPerspectiveName(pers.getName());
					}
				}
				if (fundDet.getAdjustmentType() == Constants.PLANNED)
					fundDet.setAdjustmentTypeName("Planned");
				else if (fundDet.getAdjustmentType() == Constants.ACTUAL) {
					fundDet.setAdjustmentTypeName("Actual");
					Date dt = DateConversion.getDate(fundDet.getTransactionDate());
					double frmExRt = CurrencyUtil.getExchangeRate(fundDet.getCurrencyCode(),1,dt);
					String toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
					eaForm.setCurrCode(toCurrCode);
					double toExRt = CurrencyUtil.getExchangeRate(toCurrCode,1,dt);
					double amt = CurrencyWorker.convert1(DecimalToText.getDouble(fundDet.getTransactionAmount()),frmExRt,toExRt);
					if (fundDet.getTransactionType() == Constants.COMMITMENT) {
						totComm += amt;
					} else if (fundDet.getTransactionType() == Constants.DISBURSEMENT) {
						totDisb += amt;
					} else if (fundDet.getTransactionType() == Constants.EXPENDITURE) {
						totExp += amt;
					}
				}
				fundDetails.add(fundDet);
			}
		}
		List sortedList = new ArrayList(fundDetails);
		Collections.sort(sortedList,FundingValidator.dateComp);
		
		newFund.setFundingDetails(sortedList);

		ArrayList fundList = new ArrayList();
		if (fundOrg.getFundings() != null) {
			fundList = new ArrayList(fundOrg.getFundings());
		}

		if (offset != -1)
			fundList.set(offset, newFund);
		else
			fundList.add(newFund);

		fundOrg.setFundings(fundList);
		ArrayList fundingOrgs = new ArrayList();
		if (eaForm.getFundingOrganizations() != null) {
			fundingOrgs = new ArrayList(eaForm.getFundingOrganizations());
			fundingOrgs.set(fundOrgOffset, fundOrg);
		}
		eaForm.setTotalCommitments(totComm);
		eaForm.setTotalDisbursements(totDisb);
		eaForm.setTotalExpenditures(totExp);
		eaForm.setStep("3");
		return mapping.findForward("forward");
	}
}
