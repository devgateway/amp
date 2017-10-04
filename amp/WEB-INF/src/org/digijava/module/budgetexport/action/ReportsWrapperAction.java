package org.digijava.module.budgetexport.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.budgetexport.util.BudgetExportConstants;

public class ReportsWrapperAction extends MultiAction {

    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        request.getSession().setAttribute(BudgetExportConstants.BUDGET_EXPORT_TYPE, "sometype");
        request.getSession().setAttribute(BudgetExportConstants.BUDGET_EXPORT_PROJECT_ID, request.getParameter("projectId"));
        request.getSession().removeAttribute("BUDGET_EXPORTED_MAPPER");
        return mapping.findForward("forward");
    }

    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
