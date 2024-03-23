package org.digijava.module.budget.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.form.EditBudgetSectorForm;
import org.digijava.module.budget.helper.BudgetDbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EditBudgetSector extends Action {

    
     public ActionForward execute(ActionMapping mapping, ActionForm form,
             HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
         
         HttpSession session = request.getSession();
         if (session.getAttribute("ampAdmin") == null) {
             return mapping.findForward("index");
         }else{
             String str = (String) session.getAttribute("ampAdmin");
         if (str.equals("no")) {
             return mapping.findForward("index");
            }
         }
         
         EditBudgetSectorForm eform = (EditBudgetSectorForm)form;
         if (request.getParameter("id")!=null){
             Long id = new Long(request.getParameter("id"));
             eform.setId(id);
             AmpBudgetSector sector = BudgetDbUtil.getBudgetSectorById(id);
             eform.setBudgetsectorcode(sector.getCode());
             eform.setBudgetsectorname(sector.getSectorname());
             return mapping.findForward("forward");
         }
         if (request.getParameter("edit")!=null){
             modeEdit(eform,request);
             return mapping.findForward("forwardToBudgetmanager");
         }
         return mapping.findForward("forward");
     }
     
     public void modeEdit (EditBudgetSectorForm eform,HttpServletRequest request) throws Exception {
            if (eform.getId()!=null){
                if(BudgetDbUtil.existsBudgetSector(eform.getBudgetsectorname(), eform.getBudgetsectorcode(), eform.getId())){
                    request.getSession().setAttribute("duplicateBudgetSect", "exists") ;                    
                }else{
                    AmpBudgetSector sector = BudgetDbUtil.getBudgetSectorById(eform.getId());
                    sector.setSectorname(eform.getBudgetsectorname());
                    sector.setCode(eform.getBudgetsectorcode());
                    BudgetDbUtil.UpdateBudgetSector(sector);
                }
                eform.setBudgetsectorcode(null);
                eform.setBudgetsectorname(null);
            }
        }
     }
