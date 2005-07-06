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



public class UpdateStatus extends Action
{
	private static Logger logger = Logger.getLogger(UpdateStatus.class) ;
		public ActionForward execute(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
		{
		
			StatusItemForm formBean = (StatusItemForm) form ; 	
			AmpStatus statusItem= new AmpStatus() ;
			Long id = formBean.getAmpStatusId();
	
			statusItem.setName(formBean.getName()) ;
			statusItem.setAmpStatusId(formBean.getAmpStatusId());
			statusItem.setType(formBean.getType()) ;
/*			if (formBean.getDescription() == null)
				statusItem.setDescription("blank") ;
			else statusItem.setDescription(formBean.getDescription()) ;
*/
			if (formBean.getDescription() == null 
					|| formBean.getDescription().trim().equals("")) 
			{ statusItem.setDescription(new String(" "));
			}
			else {
					statusItem.setDescription(formBean.getDescription());
			}
			statusItem.setStatusCode(formBean.getStatusCode());
			logger.debug("UpdateStatus action: unchanged status-id " + formBean.getAmpStatusId());
			logger.debug("UpdateStatus action: updated status code: " + formBean.getStatusCode());
			logger.debug("UpdateStatus action: updated status name:" + formBean.getName());
		
			DbUtil.update(statusItem) ;
		
			return mapping.findForward("forward") ;
		}

}
