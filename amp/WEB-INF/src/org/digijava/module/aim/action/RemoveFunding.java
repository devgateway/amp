
package org.digijava.module.aim.action;

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
import org.digijava.module.aim.helper.FundingOrganization;


public class RemoveFunding extends Action {
	private static Logger logger = Logger.getLogger(RemoveFunding.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
	
		throw new RuntimeException("SaveActivity::execute: not implemented!");
//		EditActivityForm eaForm = (EditActivityForm) form;
//		
//		String temp = request.getParameter("fundId");
//		String temp1 = request.getParameter("fundOrgId");
//		long fundId = -1;
//		long fundOrgId = -1;
//		
//		if (temp != null) {
//			try {
//				fundId = Long.parseLong(temp);	
//			} catch (NumberFormatException nfe) {
//				logger.error("Invalid funding Id :" + temp);
//			}
//		}
//		
//		if (temp1 != null) {
//			try {
//				fundOrgId = Long.parseLong(temp1);
//			} catch (NumberFormatException nfe) {
//				logger.error("Invalid funding organisation Id :" + temp1);
//			}
//		}
//		
//		if (fundId > 0 && fundOrgId > 0) {
//			Iterator itr = eaForm.getFunding().getFundingOrganizations().iterator();
//			while (itr.hasNext()) {
//				FundingOrganization fOrg = (FundingOrganization) itr.next();
//				if (fOrg.getAmpOrgId().longValue() == fundOrgId) {
//					Funding fund = new Funding();
//					fund.setFundingId(fundId);
//					fOrg.getFundings().remove(fund);
//					break;
//				}
//			}
//		}
//		
//		return mapping.findForward("forward");
	}
}