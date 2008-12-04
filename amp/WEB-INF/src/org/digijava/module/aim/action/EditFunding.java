package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class EditFunding extends Action {

	private static Logger logger = Logger.getLogger(EditFunding.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm formBean = (EditActivityForm) form;

		Long orgId = formBean.getOrgId();
		int offset = formBean.getOffset();
		int numComm = 0;
		int numDisb = 0;
                int numDisbOrder=0;
		int numExp = 0;
		formBean.getFunding().setAssistanceType(null);
		formBean.getFunding().setOrgFundingId("");
		formBean.getFunding().setSignatureDate("");
		formBean.getFunding().setFundingDetails(null);
		formBean.getFunding().setFundingMTEFProjections(null);
		ArrayList<FundingOrganization> fundingOrganizations = new ArrayList<FundingOrganization>(formBean.getFunding().getFundingOrganizations());
		if (fundingOrganizations != null) {
			for (int i = 0; i < fundingOrganizations.size(); i++) {
				FundingOrganization fundingOrganization = (FundingOrganization) fundingOrganizations.get(i);
				if (fundingOrganization.getAmpOrgId().equals(orgId)) {
					formBean.getFunding().setOrgName(fundingOrganization.getOrgName());
					int index = 0;
					ArrayList<Funding> fundings = new ArrayList<Funding>(fundingOrganization.getFundings());
					for (int j = 0; j < fundings.size(); j++) {
						if (index == offset) {
							Funding funding = (Funding) fundings.get(j);
							if (funding != null) {
								//formBean.setAssistanceType(funding.getAmpTermsAssist().getAmpTermsAssistId());
								if (funding.getTypeOfAssistance()!=null)
									formBean.getFunding().setAssistanceType( funding.getTypeOfAssistance().getId() );
								else formBean.getFunding().setAssistanceType(new Long(0));
								formBean.getFunding().setOrgFundingId(funding.getOrgFundingId());
								if (funding.getFinancingInstrument() != null)
								formBean.getFunding().setModality(funding.getFinancingInstrument().getId());
								formBean.getFunding().setFundingConditions(funding.getConditions());
								formBean.getFunding().setFundingMTEFProjections( new ArrayList<MTEFProjection> (funding.getMtefProjections()) );
								formBean.getFunding().setFundingDetails(new ArrayList<FundingDetail>());

								if (funding.getFundingDetails() != null) {
									formBean.getFunding().getFundingDetails().addAll(funding.getFundingDetails());
									Iterator<FundingDetail> itr = funding.getFundingDetails().iterator();
									while (itr.hasNext()) {
										FundingDetail fd =itr.next();
										if (fd.getTransactionType() == 0) {
											numComm ++;
										} else if (fd.getTransactionType() == 1) {
											numDisb ++;
										} else if (fd.getTransactionType() == 2) {
											numExp ++;
										}
                                        else if (fd.getTransactionType() == 4) {
                                          numDisbOrder ++;
                                        }

									}
								}
							}
							break;
						}
						index++;
					}
					break;
				}
			}
		}
		formBean.getFunding().setNumComm(numComm);
		formBean.getFunding().setNumDisb(numDisb);
		formBean.getFunding().setNumExp(numExp);
        formBean.getFunding().setNumDisbOrder(numDisbOrder);
		formBean.setCurrencies(CurrencyUtil.getAmpCurrency());
		formBean.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false, request));
		formBean.getFunding().setOrganizations(DbUtil.getAllOrganisation());
		formBean.getFunding().setEditFunding(true);
		formBean.getFunding().setDupFunding(true);
		formBean.getFunding().setFirstSubmit(false);
		return mapping.findForward("forward");
	}
}
