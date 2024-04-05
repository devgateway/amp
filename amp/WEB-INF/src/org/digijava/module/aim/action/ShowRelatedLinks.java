/*
 * ShowRelatedLinks.java
 * Created : 13-Jul-2005 
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.MyDesktopForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowRelatedLinks extends Action {
    private static Logger logger = Logger.getLogger(ShowRelatedLinks.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        MyDesktopForm mdForm = (MyDesktopForm) form;
        return mapping.findForward("forward");
    }
}
