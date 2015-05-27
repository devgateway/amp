package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpScorecardSettings;
import org.digijava.module.aim.form.ScorecardManagerForm;
import org.digijava.module.aim.util.DbUtil;

public class ScorecardManager extends Action {

	private final static String UPDATE = "UPDATE";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
		ScorecardManagerForm scorecardSettingsForm = (ScorecardManagerForm) form;
		Collection<AmpScorecardSettings> scorecardSettingsList = DbUtil.getAll(AmpScorecardSettings.class);
		if (scorecardSettingsForm.getAction().equals(UPDATE)) {
			AmpScorecardSettings settings;
			if (scorecardSettingsList.isEmpty()) {
				settings = new AmpScorecardSettings();
			}
			else {
				settings = scorecardSettingsList.iterator().next();
			}
			settings.setValidationPeriod(scorecardSettingsForm.getValidationPeriod());
			settings.setValidationTime(scorecardSettingsForm.getValidationTime());
			DbUtil.saveOrUpdateObject(settings);
			return mapping.findForward("index");

		} else {
			if (!scorecardSettingsList.isEmpty()) {
				AmpScorecardSettings settings = scorecardSettingsList.iterator().next();
				scorecardSettingsForm.setValidationPeriod(settings.getValidationPeriod());
				scorecardSettingsForm.setValidationTime(settings.getValidationTime());
			}
			return mapping.findForward("forward");

		}
	}

}
