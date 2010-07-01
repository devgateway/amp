package org.digijava.module.fundingpledges.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.ViewPledgesForm;

public class ShowPledgesList extends Action {

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
		ViewPledgesForm plForm = (ViewPledgesForm) form;
		
		plForm.setAllFundingPledges(PledgesEntityHelper.getPledges());
		
		 return mapping.findForward("forward");
    		
	}
}
