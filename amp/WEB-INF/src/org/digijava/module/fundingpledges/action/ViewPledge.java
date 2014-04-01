package org.digijava.module.fundingpledges.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class ViewPledge extends Action {

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
//		Long pledgeId = Long.parseLong(request.getParameter("id"));
//		FundingPledges fp = PledgesEntityHelper.getPledgesById(pledgeId);
//		plForm.importPledgeData(fp);
		// skip rendering
		request.setAttribute("bootstrap_insert", true); // for the big layout to know to adapt the page for modern-web-standards insets
		return mapping.findForward("forward");
    		
	}
}
