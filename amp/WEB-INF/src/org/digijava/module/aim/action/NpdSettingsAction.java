package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.form.NpdSettingsForm;
import org.digijava.module.aim.util.NpdUtil;
import org.digijava.module.aim.util.TeamUtil;

public class NpdSettingsAction extends DispatchAction {

	public ActionForward execute(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
		return super.execute(arg0, arg1, arg2, arg3);
	}

	public ActionForward viewCurrentSettings(ActionMapping arg0,
			ActionForm arg1, HttpServletRequest request, HttpServletResponse arg3)
			throws Exception {
		NpdSettingsForm form = (NpdSettingsForm) arg1;
		Long teamId;
		if (form.getAmpTeamId() == null) {
			teamId = TeamUtil.getCurrentTeam(request).getAmpTeamId();
		} else {
			teamId = form.getAmpTeamId();
		}

		NpdSettings npdSettings = NpdUtil.getCurrentSettings(teamId);	
		if(npdSettings.getAngle()==null){
			form.setAngle("");
		}else {
			form.setAngle(String.valueOf(npdSettings.getAngle()));	
		}		
		
		form.setHeight(npdSettings.getHeight());
		form.setWidth(npdSettings.getWidth());
		form.setAmpTeamId(npdSettings.getTeam().getAmpTeamId());
		form.setPageSize(npdSettings.getActListPageSize());

		return arg0.findForward("forward");

	}

	public ActionForward changeSettings(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse arg3) throws Exception {
		NpdSettingsForm form = (NpdSettingsForm) arg1;
		ActionMessages errors = new ActionMessages();
		errors = proceedNpdsettingsValidation(form);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return arg0.getInputForward();
		}
		Long teamId;
		if (form.getAmpTeamId() == null) {
			teamId = TeamUtil.getCurrentTeam(request).getAmpTeamId();
		} else {
			teamId = form.getAmpTeamId();
		}
		NpdSettings npdSettings = NpdUtil.getCurrentSettings(teamId);	
		
		if(form.getAngle().equals("")){
			npdSettings.setAngle(null);
		}else {
			npdSettings.setAngle(new Integer(form.getAngle()));
				
		}
		
		npdSettings.setHeight(form.getHeight());
		npdSettings.setWidth(form.getWidth());
		npdSettings.setActListPageSize(form.getPageSize());
		NpdUtil.updateSettings(npdSettings);
		return arg0.findForward("forward");
	}

	private ActionMessages proceedNpdsettingsValidation(NpdSettingsForm form) {
		ActionMessages errors = new ActionMessages();
		if (form.getHeight() == null || form.getHeight().intValue() <= 0) {
			errors.add(null, new ActionMessage(
					"errors:aim:npdsettings:incorrectHeigh"));
		}
		if (form.getWidth() == null || form.getWidth().intValue() <= 0) {
			errors.add(null, new ActionMessage(
					"errors:aim:npdsettings:incorectWidth"));
		}
		if (!form.getAngle().equals("") && ( form.getAngle() == null || Integer.parseInt(form.getAngle()) < 0
				|| Integer.parseInt(form.getAngle()) > 90)) {
			errors.add(null, new ActionMessage(
					"errors:aim:npdsettings:incorrectAngle"));
		}
		return errors;
	}

}
