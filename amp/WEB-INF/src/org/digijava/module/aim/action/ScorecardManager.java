package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpScorecardSettings;
import org.digijava.module.aim.dbentity.AmpScorecardSettingsCategoryValue;
import org.digijava.module.aim.form.ScorecardManagerForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ScorecardManager extends Action {
    
    private final static String CANCEL = "CANCEL";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ScorecardManagerForm scorecardSettingsForm = (ScorecardManagerForm) form;
        Collection<AmpCategoryValue> allCategoryValues = 
        CategoryManagerUtil.
        getAmpCategoryValueCollectionByKey("activity_status");
        
        for (AmpCategoryValue acv : allCategoryValues) {
            acv.setValue(CategoryManagerUtil.translateAmpCategoryValue(acv));
        }
        
        scorecardSettingsForm.setCategoryValues(allCategoryValues);
        scorecardSettingsForm.setQuarters(Arrays.asList("Q1", "Q2", "Q3", "Q4"));
        
        List<AmpScorecardSettings> scorecardSettingsList = (List<AmpScorecardSettings>) DbUtil.getAll(AmpScorecardSettings.class);
        AmpScorecardSettings settings = scorecardSettingsList.isEmpty() ? new AmpScorecardSettings() : scorecardSettingsList.get(0);
        
        if (scorecardSettingsForm.getAction() != null && scorecardSettingsForm.getAction().equals(CANCEL)) {
            return mapping.findForward("index");
        } else {
            if (!scorecardSettingsList.isEmpty()) {
                    scorecardSettingsForm.setValidationPeriod(settings.getValidationPeriod());
                    scorecardSettingsForm.setValidationTime(settings.getValidationTime());
                    scorecardSettingsForm.setPercentageThreshold(settings.getPercentageThreshold());
                    scorecardSettingsForm.setSelectedCategoryValues(getSelectedClosedStatuses(settings.getClosedStatuses()));
                    scorecardSettingsForm.setSelectedQuarters((String[]) settings.getQuartersAsList().toArray());
            }
            
            return mapping.findForward("forward");
        }
    }

    String[] getSelectedClosedStatuses(Set<AmpScorecardSettingsCategoryValue> categoryValues) {
        ArrayList<String> selectedStatuses = new ArrayList<String>();
        
        for (AmpScorecardSettingsCategoryValue categoryValue : categoryValues) {
            selectedStatuses.add(Long.toString(categoryValue.getAmpCategoryValueStatus().getId()));
        }
        
        return selectedStatuses.toArray(new String[selectedStatuses.size()]);
    }

}
