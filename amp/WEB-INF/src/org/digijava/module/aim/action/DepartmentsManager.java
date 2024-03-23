package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.form.DepartmentsManagerForm;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DepartmentsManager extends MultiAction{

    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
        
        HttpSession session = request.getSession();
         if (session.getAttribute("ampAdmin") == null) {
             return mapping.findForward("index");
         }else{
             String str = (String) session.getAttribute("ampAdmin");
         if (str.equals("no")) {
             return mapping.findForward("index");
            }
         }
         
        DepartmentsManagerForm dform = (DepartmentsManagerForm) form;
        dform.setDepartments(BudgetDbUtil.getDepartments());
        return modeSelect(mapping, form, request, response);
    }

    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getParameter("new")!=null && request.getParameter("new").equalsIgnoreCase("true")){
            return modeNew(mapping, form, request, response);
        }
        if (request.getParameter("delete")!=null && request.getParameter("delete").equalsIgnoreCase("true")){
            return modeDelete(mapping, form, request, response);
        }
        
        if(request.getSession().getAttribute("duplicateDepratment")!=null){
            ActionMessages errors= new ActionMessages();
            errors.add("", new ActionMessage("error.admin.deparmentExists", TranslatorWorker.translateText("AmpDepartment with given name or code already exists")));
            saveErrors(request, errors);
            
            request.getSession().removeAttribute("duplicateDepratment");
        }
        
         return mapping.findForward("forward");
        
    }
    
    public ActionForward modeNew(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        DepartmentsManagerForm dform = (DepartmentsManagerForm) form;
        if(BudgetDbUtil.existsDepartment(dform.getDepartmentname(), dform.getDepartmentcode(), null)){
            ActionMessages errors= new ActionMessages();
            errors.add("", new ActionMessage("error.admin.deparmentExists", TranslatorWorker.translateText("AmpDepartment with given name or code already exists")));
            saveErrors(request, errors);            
            dform.setDepartments(BudgetDbUtil.getDepartments());
            return mapping.findForward("forward");
        }
        
        AmpDepartments dep = new AmpDepartments();
        dep.setName(dform.getDepartmentname());
        dep.setCode(dform.getDepartmentcode());
        BudgetDbUtil.saveDepartment(dep);
        
        return modeFinalize(mapping, form, request, response);
    }
    
    public ActionForward modeFinalize(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        DepartmentsManagerForm dform = (DepartmentsManagerForm) form;
        dform.setDepartmentcode(null);
        dform.setDepartmentname(null);
        dform.setDepartments(BudgetDbUtil.getDepartments());
        return mapping.findForward("forward");
    }
    
    public ActionForward modeDelete(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = new Long(request.getParameter("id"));
        BudgetDbUtil.DeleteDepartment(id);
        
        return modeFinalize(mapping, form, request, response);
    }
    
}
