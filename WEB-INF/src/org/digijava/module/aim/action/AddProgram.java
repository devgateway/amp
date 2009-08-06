package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProgramsForm;
import org.digijava.module.aim.util.ProgramUtil;

public class AddProgram
    extends Action {
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

	ProgramsForm pgForm = (ProgramsForm) form;
	//pgForm.setReset(false);
    List prl = getParents();
    List prLevels = new ArrayList();
    String selectedThemeId = request.getParameter("themeid");
    String opStatus = request.getParameter("op");
    String strLevel = request.getParameter("selPrgLevel");
    String s= request.getParameter("programType");
    Integer pType = null;
    if(s!=null) pType = new Integer(s);
    int settingsId = pgForm.getProgramType();
    if(pType!=null) {
    	settingsId = pType.intValue();
    	pgForm.setProgramType(settingsId);
    }
    AmpActivityProgramSettings parent=null;
    switch(settingsId){
      case ProgramUtil.NATIONAL_PLAN_OBJECTIVE_KEY: parent=pgForm.getNationalSetting(); break;
          case ProgramUtil.PRIMARY_PROGRAM_KEY: parent=pgForm.getPrimarySetting(); break;
              case ProgramUtil.SECONDARY_PROGRAM_KEY: parent=pgForm.getSecondarySetting(); break;
    }
    if (selectedThemeId == null && opStatus == null && strLevel == null) {

      if (parent == null || parent.getDefaultHierarchy() == null) {
        prLevels.add(prl);
      }
      else {
        Collection defaultHierarchy = ProgramUtil.getSubThemes(parent.getDefaultHierarchyId());
        prLevels.add(defaultHierarchy);
      }
      pgForm.setProgramLevels(prLevels);
      pgForm.setSelPrograms(null);
      return mapping.findForward("forward");
    }

    if (selectedThemeId == null) {
      if (pgForm.getProgramLevels() != null) {
        prLevels = pgForm.getProgramLevels();
      }
      if (prLevels.size() == 0) {
        prLevels.add(prl);
        pgForm.setProgramLevels(prLevels);
      }
      return mapping.findForward("forward");
    }
    else if (selectedThemeId.equals("-1")) {
      if (strLevel != null) {
        Long level = Long.valueOf(strLevel);
        prLevels = pgForm.getProgramLevels();
        int sz = prLevels.size();
        for (int i = level.intValue(); i < sz; i++) {
          prLevels.remove(level.intValue());
        }
      }
      if (opStatus != null) {
        return mapping.findForward("added");
      }
      else {
        return mapping.findForward("forward");
      }
    }
    else if (selectedThemeId.equals("")) {
      if (opStatus != null) {
        return mapping.findForward("added");
      }
      else {
        return mapping.findForward("forward");
      }
    }

    if (opStatus != null) {
      return null;
    }

    int ind = 0;
    boolean opflag = false;
    AmpTheme prg = null;
    prLevels = pgForm.getProgramLevels();
    if (prLevels == null) {
      prLevels.add(getParents());
    }
    else if (prLevels.size() == 0) {
      prLevels.add(getParents());

    }

    ListIterator prItr = prLevels.listIterator();
    ListIterator subPrgItr;
    while (prItr.hasNext()) {
      ArrayList subPrg = (ArrayList) prItr.next();
      if (subPrg != null) {
        subPrgItr = subPrg.listIterator();
        while (subPrgItr.hasNext()) {
          prg = (AmpTheme) subPrgItr.next();
          if (selectedThemeId.equals(prg.getAmpThemeId().
                                     toString())) {
            List<AmpTheme> subPrograms = getThenmes(Long.valueOf(selectedThemeId));
            if (subPrograms != null) {
              ind = prItr.nextIndex();
              if (subPrograms.size() != 0) {
                if (prItr.hasNext()) {
                  prItr.next();
                  prItr.set(subPrograms);
                  ind = prItr.nextIndex();
                }
                else {
                  prLevels.add(getThenmes(Long.valueOf(selectedThemeId)));
                  ind = 0;
                }
                opflag = true;
              }
            }
            break;
          }
        }
      }
      if (opflag) {
        break;
      }
    }

    if (ind != 0) {
      int sz = prLevels.size();
      for (int i = ind; i < sz; i++) {
        prLevels.remove(ind);
      }
    }
    pgForm.setProgramLevels(prLevels);
    return mapping.findForward("forward");
  }

  private List<AmpTheme> getThenmes(Long parentid) throws DgException{
    ArrayList<AmpTheme> prl = new ArrayList<AmpTheme>(ProgramUtil.getSubThemes(parentid));
    return prl;
  }

  private List<AmpTheme> getParents() throws DgException{
    ArrayList<AmpTheme> prl = new ArrayList<AmpTheme>(ProgramUtil.getParentThemes());
    return prl;
  }

  private boolean validateData(AmpTheme theme1, AmpTheme theme2) {
    if (theme1.getAmpThemeId().equals(theme2.getAmpThemeId())) {
      return true;
    }
    return false;
  }
}
