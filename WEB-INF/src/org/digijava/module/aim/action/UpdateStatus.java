package org.digijava.module.aim.action ;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.form.StatusItemForm;
import org.digijava.module.aim.util.DbUtil;



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
			// added for checking whether the status code is already present
			boolean Flag = true;
			String code = formBean.getStatusCode();
			Long originalCode = formBean.getOriginalAmpStatusId();
			String trial = originalCode.toString();
			Collection col = DbUtil.getStatusCodes();
			Iterator itr = col.iterator();
			while(itr.hasNext())
			{
				AmpStatus amp=  (AmpStatus) itr.next();
				String a = amp.getStatusCode();
				if(a.equals(code) && !(a.equals(trial)))
				{
					Flag = false;
				}
				else
				{
					logger.info("COOL u can proceed!!!!");
				}
				
			}
			
			
			if(Flag){
			DbUtil.update(statusItem) ;
			return mapping.findForward("forward") ;
			}
			else
			{
				ActionMessages errors = new ActionMessages();
				errors.add("title", new ActionMessage(
						"error.aim.addStatus.statusAdded"));
				saveErrors(request, errors);
				return mapping.findForward("returnforward");
			}
		
			//return mapping.findForward("forward") ;
		}

}
