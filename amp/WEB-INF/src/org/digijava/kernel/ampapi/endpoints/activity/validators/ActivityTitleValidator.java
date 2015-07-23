/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * Validates that project title field value is unique across AMP
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityTitleValidator extends InputValidator {
	
	private boolean missingTitle = false;

	public ActivityTitleValidator() {
	}

	@Override
	public ApiErrorMessage getErrorMessage() {
		if (missingTitle)
			return ActivityErrors.TITLE_IN_DEFAULT_LANUGAGE_REQUIRED;
		return ActivityErrors.UNIQUE_ACTIVITY_TITLE;
	}

	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
			Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
		boolean isValid = true;
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		
		// this validator only validates project title
		if (InterchangeUtils.underscorify(ActivityFieldsConstants.PROJECT_TITLE).equals(fieldName)) {
			// replicating current AF functionality to validate the default language title value
			String lang = importer.getTrnSettings().getDefaultLangCode();
			// it's always required & type is validated earlier
			String activityTitle = null;
			if (Boolean.TRUE.equals(fieldDescription.get(ActivityEPConstants.TRANSLATABLE))) {
				activityTitle = (String) ((Map<String, Object>) newFieldParent.get(fieldName)).get(lang);
			} else {
				activityTitle = (String) newFieldParent.get(fieldName);
			}
			if (StringUtils.isBlank(activityTitle)) {
				isValid = false;
				missingTitle = true;
			} else {
				AmpActivityGroup group = importer.getOldActivity() == null ? null : 
					importer.getOldActivity().getAmpActivityGroup();
				AmpActivity activityByName = ActivityUtil.getActivityByNameExcludingGroup(activityTitle, group);
				isValid = activityByName == null;
			}
		}

		return isValid;
	}

}
