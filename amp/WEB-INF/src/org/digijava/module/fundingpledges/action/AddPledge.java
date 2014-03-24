package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class AddPledge extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
    		request.setAttribute("bootstrap_insert", true);
    		PledgeForm plForm = (PledgeForm) form;   		
    		
    		String yearToSpecify = TranslatorWorker.translateText("unspecified");
            
            if (plForm.getYear() == null) {     
               plForm.setYear(yearToSpecify);
            };
            
 	        if (request.getParameter("reset") != null && request.getParameter("reset").equalsIgnoreCase("true")) {
	        	plForm.reset();
	        	request.getSession().removeAttribute("reset");
			} else if (request.getParameter("pledgeId") != null && Long.valueOf(request.getParameter("pledgeId")) > 0){
				FundingPledges fp = PledgesEntityHelper.getPledgesById(Long.valueOf(request.getParameter("pledgeId")));
				plForm.reset();
				plForm.importPledgeData(fp);
	        	request.getSession().removeAttribute("pledgeId");
			}
	        request.getSession().setAttribute("pledgeForm", plForm);
	        
	        ActionMessages errors = (ActionMessages)request.getSession().getAttribute("duplicatedTitleError");
	 	 	if(errors!=null){
	 	 		saveErrors(request, errors);
	 	 		request.getSession().removeAttribute("duplicatedTitleError");
	 	 	}
	        
            return mapping.findForward("forward");
            
    }
   
}

