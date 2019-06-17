package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * Validates that project title field value is provided in default language and is unique across AMP
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityTitleValidator extends InputValidator {
    
    private boolean missingTitle = false;

    @Override
    public ApiErrorMessage getErrorMessage() {
        if (missingTitle)
            return ActivityErrors.TITLE_IN_DEFAULT_LANUGAGE_REQUIRED;
        return ActivityErrors.UNIQUE_ACTIVITY_TITLE;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath) {
        boolean isValid = true;
        String fieldName = fieldDescription.getFieldName();
        
        // this validator only validates project title
        if (FieldMap.underscorify(ActivityFieldsConstants.PROJECT_TITLE).equals(fieldName)) {
            // replicating current AF functionality to validate the default language title value
            String lang = importer.getTrnSettings().getDefaultLangCode();
            // it'  s always required & type is validated earlier
            String activityTitle = null;
            if (Boolean.TRUE.equals(fieldDescription.isTranslatable())) {
                activityTitle = (String) ((Map<String, Object>) newFieldParent.get(fieldName)).get(lang);
            } else {
                activityTitle = (String) newFieldParent.get(fieldName);
            }
            if (StringUtils.isBlank(activityTitle)) {
                isValid = false;
                missingTitle = true;
            } else {
                ActivityImporter activityImporter = (ActivityImporter) importer;
                AmpActivityGroup group = activityImporter.getOldActivity() == null ? null
                        : activityImporter.getOldActivity().getAmpActivityGroup();
                AmpActivity activityByName = ActivityUtil.getActivityByNameExcludingGroup(activityTitle, group);
                isValid = activityByName == null;
            }
        }

        return isValid;
    }

}
