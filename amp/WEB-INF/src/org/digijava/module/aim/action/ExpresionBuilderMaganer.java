package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class ExpresionBuilderMaganer extends DispatchAction {

    private static Logger logger = Logger.getLogger(ExpresionBuilderMaganer.class);

    
    public ActionForward listExpresions(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response){
    	
    	return mapping.findForward("listExpresions");
    }

    public ActionForward addExpresion(ActionMapping mapping,
			 ActionForm form,
			 javax.servlet.http.HttpServletRequest request,
			 javax.servlet.http.HttpServletResponse
			 response){
	
    	return mapping.findForward("expresionForm");
    }
    
    public ActionForward editExpresion(ActionMapping mapping,
			 ActionForm form,
			 javax.servlet.http.HttpServletRequest request,
			 javax.servlet.http.HttpServletResponse
			 response){
	
    	return mapping.findForward("expresionForm");
    }
    
    
}
