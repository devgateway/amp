package org.digijava.module.aim.action ;

import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;


public class ViewAmp extends Action
{
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		return mapping.findForward("forward");
	}
}
