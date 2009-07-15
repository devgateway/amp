package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;

public class SelectorAction extends Action{
	
	public static String ATTRIBUTE_START = SelectorAction.class.getCanonicalName() + ".START";
	public static String ATTRIBUTE_END = SelectorAction.class.getCanonicalName() + ".END";
	
	private static Logger logger = Logger.getLogger(SelectorAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionForward forward = null;		
		
		Object endSelection = request.getAttribute(ATTRIBUTE_END);
		if(endSelection != null && endSelection instanceof Boolean && (Boolean)endSelection){
			forward = selectorEnd(mapping,form,request,response);
		}else{
			request.setAttribute(ATTRIBUTE_START, mapping.getPath());
			forward = selectorStart(mapping,form,request,response);
			if(forward == null) 
				forward = mapping.findForward(mapping.getParameter());			
		}
		return forward;
	}
	
	public ActionForward selectorStart(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{	
		return null;
	}
	
	public ActionForward selectorEnd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{	
		return null;
	}
	
	public ActionForm getForm(HttpServletRequest request,
			String actionFormName) {
        SiteDomain currentSite = (SiteDomain) request.getAttribute(
                Constants.
                CURRENT_SITE);
        ComponentContext context = ComponentContext.getContext(request);
        String moduleInstance;
		if (context != null) {
            moduleInstance = (String) context.getAttribute(Constants.
                MODULE_INSTANCE);
        }
        else {
            moduleInstance = (String) request.getAttribute(Constants.
                MODULE_INSTANCE);
        }
		
		String attribute = "site" + currentSite.getSite().getSiteId() +
        (moduleInstance == null ? actionFormName :
         moduleInstance) + actionFormName;

		ActionForm instance = null;
		HttpSession session = request.getSession();
		instance = (ActionForm) session.getAttribute(attribute);

		return instance;

	}	
	

	
}
