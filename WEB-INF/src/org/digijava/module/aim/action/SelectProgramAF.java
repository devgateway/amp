package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProgramsForm;
import org.digijava.module.aim.util.ProgramUtil;

public class SelectProgramAF extends SelectorAction{
	private static Logger logger = Logger.getLogger(SelectProgramAF.class);
	@Override
	public ActionForward selectorEnd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EditActivityForm eaForm = (EditActivityForm) getForm(request,"aimEditActivityForm");
		ProgramsForm pgForm = (ProgramsForm) form;
		
		String selectedThemeId = request.getParameter("themeid");
		int settingsId = pgForm.getProgramType();
	    AmpActivityProgramSettings parent=null;
	    switch(settingsId){
	      case ProgramUtil.NATIONAL_PLAN_OBJECTIVE_KEY: parent=eaForm.getPrograms().getNationalSetting(); break;
	      case ProgramUtil.PRIMARY_PROGRAM_KEY: parent=eaForm.getPrograms().getPrimarySetting(); break;
	      case ProgramUtil.SECONDARY_PROGRAM_KEY: parent=eaForm.getPrograms().getSecondarySetting(); break;
	    }
	          List npoPrograms = new ArrayList();
	          List ppPrograms = new ArrayList();
	          List spPrograms = new ArrayList();
	          if (eaForm.getPrograms().getNationalPlanObjectivePrograms() != null) {
	            npoPrograms = eaForm.getPrograms().getNationalPlanObjectivePrograms();
	          }
	          if (eaForm.getPrograms().getPrimaryPrograms() != null) {
	            ppPrograms = eaForm.getPrograms().getPrimaryPrograms();
	          }
	          if (eaForm.getPrograms().getSecondaryPrograms() != null) {
	            spPrograms= eaForm.getPrograms().getSecondaryPrograms();
	          }

	          AmpTheme prg = ProgramUtil.getThemeObject(Long.valueOf(
	              selectedThemeId));
	          AmpActivityProgram activityProgram = new AmpActivityProgram();
	          activityProgram.setProgram(prg);
	          activityProgram.setProgramSetting(parent);

	          if (settingsId == ProgramUtil.NATIONAL_PLAN_OBJECTIVE_KEY) {
	            if(npoPrograms.size()==0) {
	              activityProgram.setProgramPercentage(100);
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
	            eaForm.getPrograms().setNationalPlanObjectivePrograms(npoPrograms);         
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
	              eaForm.getPrograms().setPrimaryPrograms(ppPrograms);

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
	              eaForm.getPrograms().setSecondaryPrograms(spPrograms);
	            }
	          }
	          return null;
	        
	}
}
