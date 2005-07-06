package org.digijava.module.aim.action ;

import org.apache.log4j.Logger ;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;

import org.digijava.module.aim.form.StatusItemForm ;
import org.digijava.module.aim.dbentity.AmpStatus;

import org.digijava.module.aim.util.DbUtil;
import javax.servlet.http.* ;



public class AddStatus extends Action
{
	private static Logger logger = Logger.getLogger(UpdateStatus.class) ;
		public ActionForward execute(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
		{
	
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}				  
			StatusItemForm formBean = (StatusItemForm) form ; 	
			AmpStatus statusItem = new AmpStatus() ;
			
			if (formBean.getStatusCode() != null)
			{
				statusItem.setName(formBean.getName()) ;
				statusItem.setStatusCode(formBean.getStatusCode()) ;
/*				if (formBean.getDescription() == null)
					statusItem.setDescription("blank") ;
				else
					statusItem.setDescription(formBean.getDescription()) ;
					*/
				if (formBean.getDescription() == null 
					|| formBean.getDescription().trim().equals("")) 
			  	{ statusItem.setDescription(new String(" "));
			  	}
				else {
					statusItem.setDescription(formBean.getDescription());
				}
				statusItem.setType(formBean.getType()) ;
				statusItem.setLanguage(null) ;
				statusItem.setVersion(null) ;
				

				logger.debug("AddStatus action: adding status code: " + formBean.getStatusCode());
		
				DbUtil.add(statusItem) ;
				return mapping.findForward("addforward") ;
			}
		else 			
			return mapping.findForward("forward") ;
		}

}
