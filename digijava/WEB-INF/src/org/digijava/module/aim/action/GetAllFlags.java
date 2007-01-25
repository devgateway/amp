package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.FlagUploaderForm;
import org.digijava.module.aim.util.FeaturesUtil;

public class GetAllFlags extends Action {
	
	private static Logger logger = Logger.getLogger(GetAllFlags.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		FlagUploaderForm fuForm = (FlagUploaderForm) form;
		fuForm.setCntryFlags(FeaturesUtil.getAllCountryFlags());
		fuForm.setCountries(FeaturesUtil.getAllCountries());
		
		String event = request.getParameter("event");
		if(event!=null)
			if(event.equals("delete"))
			{
				Long ampCntryFlagId=new Long( Long.parseLong(request.getParameter("ampCntryFlagId")));
				FeaturesUtil.deleteFlag(ampCntryFlagId);
				
				return mapping.findForward("deleted");
				
			}
		
		return mapping.findForward("forward");
	}
	
}