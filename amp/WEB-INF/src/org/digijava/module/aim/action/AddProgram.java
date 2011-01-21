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
import org.digijava.module.aim.util.ProgramUtil;

public class AddProgram
    extends Action {
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

    EditActivityForm eaform = (EditActivityForm) form;
    eaform.setReset(false);
    List prl = getParents();
    List prLevels = new ArrayList();
    String selectedThemeId = request.getParameter("themeid");
    String opStatus = request.getParameter("op");
    String strLevel = request.getParameter("selPrgLevel");
    String s= request.getParameter("programType");
    Integer pType = null;
    if(s!=null) pType = new Integer(s);
    int settingsId = eaform.getPrograms().getProgramType();
    if(pType!=null) {
    	settingsId = pType.intValue();
    	eaform.getPrograms().setProgramType(settingsId);
    }
    AmpActivityProgramSettings parent=null;
    switch(settingsId){
      case ProgramUtil.NATIONAL_PLAN_OBJECTIVE_KEY: parent=eaform.getPrograms().getNationalSetting(); break;
          case ProgramUtil.PRIMARY_PROGRAM_KEY: parent=eaform.getPrograms().getPrimarySetting(); break;
              case ProgramUtil.SECONDARY_PROGRAM_KEY: parent=eaform.getPrograms().getSecondarySetting(); break;
    }
    if (selectedThemeId == null && opStatus == null && strLevel == null) {

      if (parent == null || parent.getDefaultHierarchy() == null) {
        prLevels.add(prl);
      }
      else {
        Collection defaultHierarchy = ProgramUtil.getSubThemes(parent.getDefaultHierarchyId());
        prLevels.add(defaultHierarchy);
      }
      eaform.getPrograms().setProgramLevels(prLevels);
      eaform.getPrograms().setSelPrograms(null);
      return mapping.findForward("forward");
    }

    if (selectedThemeId == null) {
      if (eaform.getPrograms().getProgramLevels() != null) {
        prLevels = eaform.getPrograms().getProgramLevels();
      }
      if (prLevels.size() == 0) {
        prLevels.add(prl);
        eaform.getPrograms().setProgramLevels(prLevels);
      }
      return mapping.findForward("forward");
    }
    else if (selectedThemeId.equals("-1")) {
      if (strLevel != null) {
        Long level = Long.valueOf(strLevel);
        prLevels = eaform.getPrograms().getProgramLevels();
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
      if (opStatus.equals("add")) {
        List npoPrograms = new ArrayList();
        List ppPrograms = new ArrayList();
        List spPrograms = new ArrayList();
        if (eaform.getPrograms().getNationalPlanObjectivePrograms() != null) {
          npoPrograms = eaform.getPrograms().getNationalPlanObjectivePrograms();
        }
        if (eaform.getPrograms().getPrimaryPrograms() != null) {
          ppPrograms = eaform.getPrograms().getPrimaryPrograms();
        }
        if (eaform.getPrograms().getSecondaryPrograms() != null) {
          spPrograms= eaform.getPrograms().getSecondaryPrograms();
        }

        AmpTheme prg = ProgramUtil.getThemeObject(Long.valueOf(
            selectedThemeId));
        AmpActivityProgram activityProgram = new AmpActivityProgram();
        activityProgram.setProgram(prg);
        activityProgram.setProgramSetting(parent);

        if (settingsId == ProgramUtil.NATIONAL_PLAN_OBJECTIVE_KEY) {
          if(npoPrograms.size()==0) {
            activityProgram.setProgramPercentage(100L);
          }
          // changed by mouhamad for burkina the 22/02/08
          // for AMP-2666
          AmpActivityProgram program = null;
          boolean exist = false; 
          for (int i = 0; i < npoPrograms.size(); i++) {
        	  program = (AmpActivityProgram) npoPrograms.get(i);
        	  if ((program.getAmpActivityProgramId() == null) || (program.getAmpActivityProgramId().equals(activityProgram.getAmpActivityProgramId()))) {
                      if(program.getProgram().equals(activityProgram.getProgram())){
        		  exist = true;
                      }
        	  }
          }
          if (!exist) {
        	  npoPrograms.add(activityProgram);
          }
          // end 
          eaform.getPrograms().setNationalPlanObjectivePrograms(npoPrograms);         
        }
        else {
          if (settingsId ==ProgramUtil.PRIMARY_PROGRAM_KEY) {
            if( ppPrograms.size()==0){
               activityProgram.setProgramPercentage(100L);
            }
            // changed by mouhamad for burkina the 26/02/08
            // for AMP-2666
            AmpActivityProgram program = null;
            boolean exist = false; 
            for (int i = 0; i < ppPrograms.size(); i++) {
          	  program = (AmpActivityProgram) ppPrograms.get(i);
          	  if ((program.getAmpActivityProgramId() == null) || (program.getAmpActivityProgramId().equals(activityProgram.getAmpActivityProgramId()))) {
                    if(program.getProgram().equals(activityProgram.getProgram())){  
                            exist = true;
                     }
          	  }
            }
            if (!exist) {
            	 ppPrograms.add(activityProgram);
            }
            // end            
            eaform.getPrograms().setPrimaryPrograms(ppPrograms);

          }
          else {
            if( spPrograms.size()==0){
            	activityProgram.setProgramPercentage(100L);
            }
            // changed by mouhamad for burkina the 26/02/08
            // for AMP-2666
            AmpActivityProgram program = null;
            boolean exist = false; 
            for (int i = 0; i < spPrograms.size(); i++) {
          	  program = (AmpActivityProgram) spPrograms.get(i);
          	  if ((program.getAmpActivityProgramId() == null) || (program.getAmpActivityProgramId().equals(activityProgram.getAmpActivityProgramId()))) {
                       if(program.getProgram().equals(activityProgram.getProgram())){
          		  exist = true;
                       }
          	  }
            }
            if (!exist) {
            	spPrograms.add(activityProgram);
            }
            // end            
            eaform.getPrograms().setSecondaryPrograms(spPrograms);
          }
        }

        /* if(eaform.getActPrograms()!=null){
             prgLst=eaform.getActPrograms();
         }
         */
        /*AmpTheme prg = new AmpTheme();
                         AmpTheme theme=null;
         prg = ProgramUtil.getThemeObject(Long.valueOf(selectedThemeId));

                         Iterator itr=prgLst.listIterator();
                         while(itr.hasNext()){
            theme=(AmpTheme)itr.next();
            if(validateData(prg,theme)){
                 return mapping.findForward("added");
            }
                         }
                         prgLst.add(prg);
                         eaform.setActPrograms(prgLst);*/
      }


      return mapping.findForward("added");
    }

    int ind = 0;
    boolean opflag = false;
    AmpTheme prg = null;
    prLevels = eaform.getPrograms().getProgramLevels();
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
    eaform.getPrograms().setProgramLevels(prLevels);
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
