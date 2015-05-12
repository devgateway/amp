package org.digijava.module.fundingpledges.action;

import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class ViewPledgeData extends Action {

	public static void pump_flash_attribute(HttpServletRequest request, String attrName){
		if (request.getSession().getAttribute(attrName) != null){
			request.setAttribute(attrName, request.getSession().getAttribute(attrName));
			request.getSession().removeAttribute(attrName);
		}
	}
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
		//request.setAttribute("bootstrap_insert", true); // for the big layout to know to adapt the page for modern-web-standards insets
		PledgeForm plForm = (PledgeForm) form;
		
		pump_flash_attribute(request, "PNOTIFY_ERROR_MESSAGE");
		pump_flash_attribute(request, "PNOTIFY_ERROR_TITLE");
		
		Long pledgeId = Long.parseLong(request.getParameter("id"));		
		FundingPledges fp = PledgesEntityHelper.getPledgesById(pledgeId);
		plForm.importPledgeData(fp);
		//return null;
		return mapping.findForward("forward");
	}
}
