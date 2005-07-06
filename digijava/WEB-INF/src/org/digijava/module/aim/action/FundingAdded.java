package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.helper.Constants;

public class FundingAdded extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
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

		Funding newFund = new Funding();
		newFund.setAmpTermsAssist(DbUtil.getAssistanceType(eaForm
				.getAssistanceType()));
		newFund.setOrgFundingId(eaForm.getOrgFundingId());
		newFund.setModality(DbUtil.getModality(eaForm.getModality()));
		//newFund.setSignatureDate(eaForm.getSignatureDate());
		//newFund.setReportingDate(eaForm.getReportingDate());
		//newFund.setPropCloseDate(eaForm.getPlannedCompletionDate());
		//newFund.setPropStartDate(eaForm.getPlannedStartDate());
		//newFund.setActCloseDate(eaForm.getActualCompletionDate());
		//newFund.setActStartDate(eaForm.getActualStartDate());
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
					AmpCurrency currency = DbUtil.getCurrencyByCode(fundDet
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
				if (perspective != null) {
					if (perspective.equals(Constants.DONOR))
						fundDet.setPerspectiveName("Donor");
					else if (perspective.equals(Constants.MOFED))
						fundDet.setPerspectiveName("MOFED");
					else if (perspective.equals(Constants.IMPLEMENTING_AGENCY))
						fundDet.setPerspectiveName("Implementing Agency");
				}

				if (fundDet.getAdjustmentType() == Constants.PLANNED)
					fundDet.setAdjustmentTypeName("Planned");
				else if (fundDet.getAdjustmentType() == Constants.ACTUAL)
					fundDet.setAdjustmentTypeName("Actual");

				fundDetails.add(fundDet);
			}
		}
		newFund.setFundingDetails(fundDetails);

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
		return mapping.findForward("forward");
	}
}
