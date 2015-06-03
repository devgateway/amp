package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardService;
import org.digijava.module.aim.dbentity.AmpScorecardSettings;
import org.digijava.module.aim.dbentity.AmpScorecardSettingsCategoryValue;
import org.digijava.module.aim.form.ScorecardManagerForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class ScorecardManager extends Action {

	private final static String UPDATE = "UPDATE";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
		ScorecardManagerForm scorecardSettingsForm = (ScorecardManagerForm) form;
		scorecardSettingsForm.setCategoryValues(ScorecardService.getAllCategoryValues());
		Collection<AmpScorecardSettings> scorecardSettingsList = DbUtil.getAll(AmpScorecardSettings.class);
		if (scorecardSettingsForm.getAction()!=null && scorecardSettingsForm.getAction().equals(UPDATE)) {
			AmpScorecardSettings settings;
			if (scorecardSettingsList.isEmpty()) {
				settings = new AmpScorecardSettings();
			}
			else {
				settings = scorecardSettingsList.iterator().next();
			}
			
			settings.setValidationPeriod(scorecardSettingsForm.getValidationPeriod());
			settings.setPercentageThreshold(scorecardSettingsForm.getPercentageThreshold());
			
			// We use in hbm files delete-orphan-all clause. We have to delete all children and then we should add the new list
			settings.getClosedStatuses().clear();
			settings.getClosedStatuses().addAll((getClosedStatusesCollection(scorecardSettingsForm.getCategoryValues(), scorecardSettingsForm.getSelectedCategoryValues(), settings)));
			
			settings.setValidationTime(scorecardSettingsForm.getValidationTime() == null
					|| scorecardSettingsForm.getValidationTime().equals(0) ? null : scorecardSettingsForm
					.getValidationTime());
			DbUtil.saveOrUpdateObject(settings);
			return mapping.findForward("index");

		} else {
			if (!scorecardSettingsList.isEmpty()) {
				AmpScorecardSettings settings = scorecardSettingsList.iterator().next();
				scorecardSettingsForm.setValidationPeriod(settings.getValidationPeriod());
				scorecardSettingsForm.setValidationTime(settings.getValidationTime());
				scorecardSettingsForm.setPercentageThreshold(settings.getPercentageThreshold());
				scorecardSettingsForm.setSelectedCategoryValues(getSelectedClosedStatuses(settings.getClosedStatuses()));
			}
			return mapping.findForward("forward");

		}
	}
	
	Set<AmpScorecardSettingsCategoryValue> getClosedStatusesCollection(Collection<AmpCategoryValue> categoryValues, String[] selectedValues, AmpScorecardSettings settings) {
		Set <AmpScorecardSettingsCategoryValue> closedStatuses = new HashSet<AmpScorecardSettingsCategoryValue>();
		
		if (selectedValues != null) {
			for (AmpCategoryValue categoryValue : categoryValues) {
				if (Arrays.asList(selectedValues).contains(Long.toString(categoryValue.getId()))) {
					AmpScorecardSettingsCategoryValue scSettingsCategoryValue = new AmpScorecardSettingsCategoryValue();
					scSettingsCategoryValue.setAmpCategoryValueStatus(categoryValue);
					scSettingsCategoryValue.setAmpScorecardSettings(settings);
					closedStatuses.add(scSettingsCategoryValue);
				}
			}
		}
		
		return closedStatuses;
	}
	
	String[] getSelectedClosedStatuses(Set<AmpScorecardSettingsCategoryValue> categoryValues) {
		ArrayList<String> selectedStatuses = new ArrayList<String>();
		
		for (AmpScorecardSettingsCategoryValue categoryValue : categoryValues) {
			selectedStatuses.add(Long.toString(categoryValue.getAmpCategoryValueStatus().getId()));
		}
		
		return selectedStatuses.toArray(new String[selectedStatuses.size()]);
	}

}
