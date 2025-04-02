package org.digijava.module.budget.action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.budget.form.SetBudgetProgramForm;
import org.digijava.module.budget.helper.BudgetDbUtil;

public class SetBudgetProgram extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        SetBudgetProgramForm sf = (SetBudgetProgramForm) form;

        if (request.getParameter("save") != null) {
            save(sf);
            return mapping.findForward("forward");
        }

        ArrayList<AmpTheme> programs = BudgetDbUtil.getBudgetProgramsLevel_0();

        for (Iterator iterator = programs.iterator(); iterator.hasNext();) {
            AmpTheme prog = (AmpTheme) iterator.next();
            if (prog.getIsbudgetprogram() != null
                    && prog.getIsbudgetprogram() == 1) {
                sf.setSeletedprogram(prog.getAmpThemeId());
            }
            sf.setPrograms(programs);
        }
        return mapping.findForward("forward");
    }

    public void save(SetBudgetProgramForm sform) {

        ArrayList<AmpTheme> programs = BudgetDbUtil.getBudgetProgramsLevel_0();
        for (Iterator iterator = programs.iterator(); iterator.hasNext();) {
            AmpTheme prog = (AmpTheme) iterator.next();
            if (prog.getAmpThemeId().equals(sform.getSeletedprogram()) && prog.getIsbudgetprogram()==null ){
                prog.setIsbudgetprogram(1);
                ProgramUtil.updateTheme(prog);
                
            }else if (prog.getAmpThemeId().equals(sform.getSeletedprogram()) && prog.getIsbudgetprogram()==0) {
                prog.setIsbudgetprogram(1);
                ProgramUtil.updateTheme(prog);
                
            }else if (prog.getIsbudgetprogram()==1){
                prog.setIsbudgetprogram(0);
                ProgramUtil.updateTheme(prog);
            }
        }
    }
}
