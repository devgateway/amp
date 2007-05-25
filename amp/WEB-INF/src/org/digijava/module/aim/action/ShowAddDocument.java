package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowAddDocument 
		  extends org.digijava.module.cms.action.ShowCreateContentItem {
		  
		  public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
				java.lang.Exception {

						  return super.execute(mapping,form,request,response);
    }
}
