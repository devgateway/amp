/**
 * 
 */
package org.digijava.module.budgetexport.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.budgetexport.util.BudgetExportConstants;

/**
 * @author Alex 
 *
 */
public class BudgetExportAction extends Action {
    private static Logger logger    = Logger.getLogger(BudgetExportAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        return mapping.findForward("forward");
    }
}
