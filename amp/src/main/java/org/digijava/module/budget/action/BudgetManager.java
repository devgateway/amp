package org.digijava.module.budget.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.form.BudgetManagerForm;
import org.digijava.module.budget.helper.BudgetDbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BudgetManager extends MultiAction{
    private static Logger logger = Logger.getLogger(BudgetManager.class);
    
    public BudgetManager(){
        super();
    }
    
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
         BudgetManagerForm bfrom = (BudgetManagerForm) form;
        //get sector from the database
         bfrom.setBudgetsectors(BudgetDbUtil.getBudgetSectors());
         
        return modeSelect(mapping, form, request, response);
    }
    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        if (request.getParameter("new")!=null && request.getParameter("new").equalsIgnoreCase("true")){
            return modeNew(mapping, form, request, response);
        }
        if (request.getParameter("delete")!=null && request.getParameter("delete").equalsIgnoreCase("true")){
            return modeDelete(mapping, form, request, response);
        }
        if (request.getParameter("resetFields")!=null && request.getParameter("resetFields").equalsIgnoreCase("true")){
            ((BudgetManagerForm)form).setBudgetsectorcode(null);
            ((BudgetManagerForm)form).setBudgetsectorname(null);
        }
        
        
        if(request.getSession().getAttribute("duplicateBudgetSect")!=null){
            ActionMessages errors= new ActionMessages();
            errors.add("", new ActionMessage("error.admin.budgetSectExists", TranslatorWorker.translateText("Budget Sector with given name or code already exists")));
            saveErrors(request, errors);
            
            request.getSession().removeAttribute("duplicateBudgetSect");
        }
        
        return mapping.findForward("forward");
    }
    
    public ActionForward modeNew(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetManagerForm bfrom = (BudgetManagerForm) form;
        if(BudgetDbUtil.existsBudgetSector(bfrom.getBudgetsectorname(), bfrom.getBudgetsectorcode(), null)){
            ActionMessages errors= new ActionMessages();
            errors.add("", new ActionMessage("error.admin.budgetSectExists", TranslatorWorker.translateText("Budget Sector with given name or code already exists")));
            saveErrors(request, errors);            
            bfrom.setBudgetsectors(BudgetDbUtil.getBudgetSectors());
            return mapping.findForward("forward");
        }
        
        AmpBudgetSector sector = new AmpBudgetSector();
        sector.setSectorname(bfrom.getBudgetsectorname());
        sector.setCode(bfrom.getBudgetsectorcode());
        BudgetDbUtil.SaveBudgetSector(sector);
        
        return modeFinalize(mapping, form, request, response);
    }
    
    public ActionForward modeFinalize(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetManagerForm bfrom = (BudgetManagerForm) form;
        bfrom.setBudgetsectorcode(null);
        bfrom.setBudgetsectorname(null);
        bfrom.setBudgetsectors(BudgetDbUtil.getBudgetSectors());
        return mapping.findForward("finalized");
    }
    
    public ActionForward modeDelete(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
        Long id = new Long(request.getParameter("sectorid"));
        BudgetDbUtil.DeleteSector(id);
        
        return modeFinalize(mapping, form, request, response);
    }
    
}
