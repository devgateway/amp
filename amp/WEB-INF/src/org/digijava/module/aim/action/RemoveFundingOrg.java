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
import org.digijava.module.aim.helper.FundingOrganization;

public class RemoveFundingOrg 
extends Action {

	private static Logger logger = Logger.getLogger(RemoveFundingOrg.class);
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		String temp1 = request.getParameter("fundOrgId");
		long fundOrgId = -1;
		if (temp1 != null) {
			try {
				fundOrgId = Long.parseLong(temp1);
			} catch (NumberFormatException nfe) {
				logger.error("Invalid funding organisation Id :" + temp1);
			}
		}
		
		Collection prevSelFund = eaForm.getFunding().getFundingOrganizations();
		Collection newFund = new ArrayList();

		Iterator itr = prevSelFund.iterator();

		while (itr.hasNext()) {
			boolean flag = false;
			FundingOrganization fo = (FundingOrganization) itr.next();
			if (fo.getAmpOrgId().equals(fundOrgId)) {
				flag = true;
				break;
			}
			if (!flag) {
				newFund.add(fo);
			}

		}

		eaForm.getFunding().setFundingOrganizations(newFund);
		eaForm.setStep("3");
		eaForm.getFunding().setSelFundingOrgs(null);
		return mapping.findForward("forward");
	
	}
}

