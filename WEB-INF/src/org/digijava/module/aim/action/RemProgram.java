package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.ProgramUtil;

public class RemProgram extends Action
{
    private static Logger logger = Logger.getLogger(ActivityManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception
    {
        EditActivityForm eaform=(EditActivityForm)form;
        int settingsId = eaform.getProgramType();
         List prgLst=new ArrayList();
         Long prgIds[]=null;
         switch (settingsId) {
           case ProgramUtil.NATIONAL_PLAN_OBJECTIVE_KEY:
             prgLst = eaform.getPrograms().getNationalPlanObjectivePrograms();
             prgIds=eaform.getSelectedNPOPrograms();
             eaform.getPrograms().setNationalPlanObjectivePrograms(removePrograms(prgLst, prgIds));
             eaform.setSelectedNPOPrograms(null);
             break;
           case ProgramUtil.PRIMARY_PROGRAM_KEY:
             prgLst = eaform.getPrograms().getPrimaryPrograms();
             prgIds=eaform.getSelectedPPrograms();
             eaform.getPrograms().setPrimaryPrograms(removePrograms(prgLst, prgIds));
             eaform.setSelectedPPrograms(null);
             break;
           case ProgramUtil.SECONDARY_PROGRAM_KEY:
             prgLst = eaform.getPrograms().getSecondaryPrograms();
             prgIds=eaform.getSelectedSPrograms();
             eaform.getPrograms().setSecondaryPrograms(removePrograms(prgLst, prgIds));
             eaform.setSelectedSPrograms(null);
             break;
         }
        eaform.setStep("2");
        return mapping.findForward("forward");
    }

    private List removePrograms(List prgLst, Long[] prgIds) {
      Iterator itr = prgLst.listIterator();
      AmpActivityProgram ampActivityProgram = null;
      Set newPrograms = new HashSet();
      while (itr.hasNext()) {
        ampActivityProgram = (AmpActivityProgram) itr.next();
        for (int i = 0; i < prgIds.length; i++) {
          if (ampActivityProgram.getProgram().getAmpThemeId().equals(prgIds[i])) {
            newPrograms.add(ampActivityProgram);
          }
        }
      }
      prgLst.removeAll(newPrograms);
      return prgLst;
    }
}
