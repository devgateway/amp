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

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
		request.setAttribute("bootstrap_insert", true); // for the big layout to know to adapt the page for modern-web-standards insets
		PledgeForm plForm = (PledgeForm) form;
//		java.util.Map<String, String[]> params = request.getParameterMap();
//		for(String param:params.keySet())
//			System.out.format("param[%s] = %s\n", param, Arrays.asList(params.get(param)));
		Long pledgeId = Long.parseLong(request.getParameter("id"));		
		FundingPledges fp = PledgesEntityHelper.getPledgesById(pledgeId);
		plForm.importPledgeData(fp);
		//return null;
		return mapping.findForward("forward");
	}
}
