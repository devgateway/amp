package org.digijava.module.budgetexport.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 3:05 PM
 */
public class ShowProjectList extends Action {
    private static Logger logger	= Logger.getLogger(ShowProjectList.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return mapping.findForward("forward");
    }
}
