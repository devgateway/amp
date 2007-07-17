package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.form.AuditLoggerManagerForm;
import org.digijava.module.aim.util.AuditLoggerUtil;
 
public class AuditLoggerManager extends MultiAction {
	
	private static Logger logger = Logger.getLogger(AuditLoggerManager.class);
	
	private ServletContext ampContext = null;
	
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Collection logs=AuditLoggerUtil.getLogObjects();
		AuditLoggerManagerForm vForm=(AuditLoggerManagerForm) form;
		vForm.setLogs(logs);
		return  modeSelect(mapping, form, request, response);
	}

	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//return modeNew(mapping, form, request, response);
		if(request.getParameter("action")!=null)
			{
//				if(request.getParameter("action").compareTo("add")==0) return modeAddTemplate(mapping, form, request, response);
			}
		return mapping.findForward("forward");
	}
	
}