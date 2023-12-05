package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditDepartmentForm;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EditDepartment extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }
        EditDepartmentForm eform = (EditDepartmentForm) form;
        if (request.getParameter("id") != null) {
            Long id = new Long(request.getParameter("id"));
            eform.setId(id);
            AmpDepartments department = BudgetDbUtil.getBudgetDepartmentById(id);
            eform.setDepname(department.getName());
            eform.setDepcode(department.getCode());
            return mapping.findForward("forward");
        }
        
        if (request.getParameter("edit") != null) {
            modeEdit(eform,request);
            return mapping.findForward("forwardToDepartmentsmanager");
        }
        return mapping.findForward("forward");
    }

    public void modeEdit(EditDepartmentForm eform,HttpServletRequest request) {
        if (eform.getId() != null) {
            if(BudgetDbUtil.existsDepartment(eform.getDepname(), eform.getDepcode(), eform.getId())){
                request.getSession().setAttribute("duplicateDepratment", "exists") ;
            }else{
                AmpDepartments dep = BudgetDbUtil.getBudgetDepartmentById(eform.getId());
                dep.setName(eform.getDepname());
                dep.setCode(eform.getDepcode());
                BudgetDbUtil.UpdateBudgetDepartment(dep);
            }
        }
    }
}
