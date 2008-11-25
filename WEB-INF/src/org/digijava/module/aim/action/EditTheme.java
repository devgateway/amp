/*
 * EditTheme.java 
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.EditProgram;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;


public class EditTheme extends Action {

	private static Logger logger = Logger.getLogger(EditTheme.class);

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

		ThemeForm themeForm = (ThemeForm) form;
		String event = request.getParameter("event");
		Long id = new Long(Long.parseLong(request.getParameter("themeId")));
		
		if (event != null)
		{
			if (event.equals("edit"))
			{
				AmpTheme ampTheme = ProgramUtil.getThemeObject(id);
				themeForm.setThemeId(id);
				themeForm.setProgramName(ampTheme.getName());
				themeForm.setProgramCode(ampTheme.getThemeCode());
				themeForm.setBudgetProgramCode(ampTheme.getBudgetProgramCode());
				themeForm.setProgramDescription(ampTheme.getDescription());
				if (ampTheme.getTypeCategoryValue() != null)
					themeForm.setProgramTypeCategValId(ampTheme.getTypeCategoryValue().getId());
				else {
					logger.error( "AmpTheme " +  ampTheme.getName() + " has Program Type null which should not be allowed.");
					themeForm.setProgramTypeCategValId( new Long(0) );
				}
				//themeForm.setProgramTypeNames(ProgramUtil.getProgramTypes());
				
				themeForm.setProgramLeadAgency( ampTheme.getLeadAgency() );
				themeForm.setProgramBackground( ampTheme.getBackground() );
				themeForm.setProgramTargetGroups( ampTheme.getTargetGroups() );
				themeForm.setProgramObjectives( ampTheme.getObjectives() );
				themeForm.setProgramOutputs( ampTheme.getOutputs() );
				themeForm.setProgramBeneficiaries( ampTheme.getBeneficiaries() );
				themeForm.setProgramEnvironmentConsiderations( ampTheme.getEnvironmentConsiderations() );
				
				if (ampTheme.getExternalFinancing() == null) {
					themeForm.setProgramExternalFinancing(Double.valueOf(0));
				} else {
					themeForm.setProgramExternalFinancing(ampTheme.getExternalFinancing());
				}
				if (ampTheme.getInternalFinancing() == null) {
					themeForm.setProgramInernalFinancing(Double.valueOf(0));
				} else {
					themeForm.setProgramInernalFinancing(ampTheme.getInternalFinancing());
				}
				if (ampTheme.getTotalFinancing() == null) {
					themeForm.setProgramTotalFinancing(Double.valueOf(0));
				} else {
					themeForm.setProgramTotalFinancing(ampTheme.getTotalFinancing());
				}
				
				return mapping.findForward("editProgram");				
			}
			if(event.equals("editSub"))
			{
				Long rootid = new Long(Long.parseLong(request.getParameter("rootId")));
				AmpTheme ampTheme = ProgramUtil.getThemeObject(id);
				themeForm.setThemeId(id);
				themeForm.setProgramName(ampTheme.getName());
				themeForm.setProgramCode(ampTheme.getThemeCode());
				themeForm.setBudgetProgramCode(ampTheme.getBudgetProgramCode());
				themeForm.setProgramDescription(ampTheme.getDescription());
				if (ampTheme.getTypeCategoryValue() != null)
					themeForm.setProgramTypeCategValId(ampTheme.getTypeCategoryValue().getId());
				else {
					logger.error( "AmpTheme " +  ampTheme.getName() + " has Program Type null which should not be allowed.");
					themeForm.setProgramTypeCategValId( new Long(0) );
				}
				themeForm.setRootId(rootid);
				
				//themeForm.setProgramTypeNames(ProgramUtil.getProgramTypes());
				themeForm.setProgramLeadAgency( ampTheme.getLeadAgency() );
				themeForm.setProgramBackground( ampTheme.getBackground() );
				themeForm.setProgramTargetGroups( ampTheme.getTargetGroups() );
				themeForm.setProgramObjectives( ampTheme.getObjectives() );
				themeForm.setProgramOutputs( ampTheme.getOutputs() );
				themeForm.setProgramBeneficiaries( ampTheme.getBeneficiaries() );
				themeForm.setProgramEnvironmentConsiderations( ampTheme.getEnvironmentConsiderations() );
				
				if (ampTheme.getExternalFinancing() == null) {
					themeForm.setProgramExternalFinancing(Double.valueOf(0));
				} else {
					themeForm.setProgramExternalFinancing(ampTheme.getExternalFinancing());
				}
				if (ampTheme.getInternalFinancing() == null) {
					themeForm.setProgramInernalFinancing(Double.valueOf(0));
				} else {
					themeForm.setProgramInernalFinancing(ampTheme.getInternalFinancing());
				}
				if (ampTheme.getTotalFinancing() == null) {
					themeForm.setProgramTotalFinancing(Double.valueOf(0));
				} else {
					themeForm.setProgramTotalFinancing(ampTheme.getTotalFinancing());
				}
				
				return mapping.findForward("editProgram");
			}
			if (event.equals("update"))
			{
				String rootid = request.getParameter("rootId");
				if(rootid.trim().length() != 0)
				{
					Long baseid = new Long(Long.parseLong(rootid));
					EditProgram editPrg = new EditProgram();
					editPrg.setAmpThemeId(id);
					editPrg.setName(themeForm.getProgramName());
					editPrg.setThemeCode(themeForm.getProgramCode());
					editPrg.setBudgetProgramCode(themeForm.getBudgetProgramCode());
					editPrg.setDescription(themeForm.getProgramDescription());
					
					editPrg.setTypeCategVal( CategoryManagerUtil.getAmpCategoryValueFromDb(themeForm.getProgramTypeCategValId()) );
					
					editPrg.setLeadAgency( themeForm.getProgramLeadAgency() );
					editPrg.setTargetGroups( themeForm.getProgramTargetGroups() );
					editPrg.setBackground( themeForm.getProgramBackground() );
					editPrg.setObjectives( themeForm.getProgramObjectives() );
					editPrg.setOutputs( themeForm.getProgramOutputs() );
					editPrg.setBeneficiaries( themeForm.getProgramBeneficiaries() );
					editPrg.setEnvironmentConsiderations( themeForm.getProgramEnvironmentConsiderations() );
					
					editPrg.setExternalFinancing(themeForm.getProgramExternalFinancing());
					editPrg.setInternalFinancing(themeForm.getProgramInernalFinancing());
					editPrg.setTotalFinancing(themeForm.getProgramTotalFinancing());

					ProgramUtil.updateTheme(editPrg);
					themeForm.setSubPrograms(ProgramUtil.getAllSubThemes(baseid));
					return mapping.findForward("subPrgEdit");
				}
				else
				{
					EditProgram editPrg = new EditProgram();
					editPrg.setAmpThemeId(id);
					editPrg.setName(themeForm.getProgramName());
					editPrg.setThemeCode(themeForm.getProgramCode());
					editPrg.setBudgetProgramCode(themeForm.getBudgetProgramCode());
					editPrg.setDescription(themeForm.getProgramDescription());
					editPrg.setTypeCategVal( CategoryManagerUtil.getAmpCategoryValueFromDb(themeForm.getProgramTypeCategValId()) );					
					editPrg.setLeadAgency( themeForm.getProgramLeadAgency() );
					editPrg.setTargetGroups( themeForm.getProgramTargetGroups() );
					editPrg.setBackground( themeForm.getProgramBackground() );
					editPrg.setObjectives( themeForm.getProgramObjectives() );
					editPrg.setOutputs( themeForm.getProgramOutputs() );
					editPrg.setBeneficiaries( themeForm.getProgramBeneficiaries() );
					editPrg.setEnvironmentConsiderations( themeForm.getProgramEnvironmentConsiderations() );
					
					editPrg.setExternalFinancing(themeForm.getProgramExternalFinancing());
					editPrg.setInternalFinancing(themeForm.getProgramInernalFinancing());
					editPrg.setTotalFinancing(themeForm.getProgramTotalFinancing());

					ProgramUtil.updateTheme(editPrg);
					return mapping.findForward("forward");
				}
			}
		}
		return mapping.findForward("forward");
	}
}