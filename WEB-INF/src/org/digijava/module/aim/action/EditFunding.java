package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

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
		formBean.setAssistanceType(null);
		formBean.setOrgFundingId("");
		formBean.setSignatureDate("");
		formBean.setFundingDetails(null);
		formBean.setFundingMTEFProjections(null);
		ArrayList fundingOrganizations = new ArrayList(formBean.getFundingOrganizations());
		if (fundingOrganizations != null) {
			for (int i = 0; i < fundingOrganizations.size(); i++) {
				FundingOrganization fundingOrganization = (FundingOrganization) fundingOrganizations.get(i);
				if (fundingOrganization.getAmpOrgId().equals(orgId)) {
					formBean.setOrgName(fundingOrganization.getOrgName());
					int index = 0;
					ArrayList fundings = new ArrayList(fundingOrganization.getFundings());
					for (int j = 0; j < fundings.size(); j++) {
						if (index == offset) {
							Funding funding = (Funding) fundings.get(j);
							if (funding != null) {
								//formBean.setAssistanceType(funding.getAmpTermsAssist().getAmpTermsAssistId());
								if (funding.getTypeOfAssistance()!=null)
									formBean.setAssistanceType( funding.getTypeOfAssistance().getId() );
								else formBean.setAssistanceType(new Long(0));
								formBean.setOrgFundingId(funding.getOrgFundingId());
								if (funding.getFinancingInstrument() != null)
									formBean.setModality(funding.getFinancingInstrument().getId());
								formBean.setFundingConditions(funding.getConditions());

								formBean.setFundingMTEFProjections( new ArrayList<MTEFProjection> (funding.getMtefProjections()) );

								formBean.setFundingDetails(new ArrayList());
								if (funding.getFundingDetails() != null) {
									formBean.getFundingDetails().addAll(funding.getFundingDetails());
									Iterator itr = funding.getFundingDetails().iterator();
									while (itr.hasNext()) {
										FundingDetail fd = (FundingDetail) itr.next();
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
		//Collection c = DbUtil.getAllAssistanceTypes();
		formBean.setNumComm(numComm);
		formBean.setNumDisb(numDisb);
		formBean.setNumExp(numExp);
                formBean.setNumDisbOrder(numDisbOrder);
		//formBean.setAssistanceTypes(c);
		formBean.setCurrencies(CurrencyUtil.getAmpCurrency());
		formBean.setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));
		formBean.setOrganizations(DbUtil.getAllOrganisation());
		formBean.setEditFunding(true);
		formBean.setDupFunding(true);
		formBean.setFirstSubmit(false);
		return mapping.findForward("forward");
	}
}
