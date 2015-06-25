/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * Validates that project title field value is unique across AMP
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityTitleValidator extends InputValidator {

	public ActivityTitleValidator() {
		this.continueOnSuccess = false;
	}

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.UNIQUE_ACTIVITY_TITLE;
	}

	@Override
	public boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldParent, JsonBean oldFieldParent,
			JsonBean fieldDescription, boolean update) {
		boolean isValid = true;
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		//this validator only validates project title
		if (InterchangeUtils.underscorify(ActivityFieldsConstants.PROJECT_TITLE).equals(fieldName)) {
			HttpServletRequest request = TLSUtils.getRequest();
			String title = newFieldValue.getString(fieldName);
			String langCode = null;
			if (ContentTranslationUtil.multilingualIsEnabled()) {
				langCode = TLSUtils.getEffectiveLangCode ();
			}
			List<AmpActivity> list = LuceneUtil.findActivitiesMoreLikeThis(request.getServletContext().getRealPath("/")
					+ LuceneUtil.ACTVITY_INDEX_DIRECTORY, title, langCode, 2);
			isValid = !isTitleExistent(newFieldValue, list);
		}

		return isValid;
	}

	/**
	 * Validates if the Activity's amp Id  contained on the newFieldValue JsonBean already exists on 
	 * the list of AmpActivity.
	 * The List <AmpActivity> contains activities with similar titles to the one present on newFieldValue. So we 
	 * check if the list with Activities with similar title contains more activities besides the
	 * one sent by parameter (newFieldValue)
	 * 
	 * @param newFieldValue the JsonBean containing the Activity amp Id to verify
	 * @param list the List with AmpActivity with similar titles
	 * @return true if the title is repeated, false otherwise.
	 */
	private boolean isTitleExistent(JsonBean newFieldValue, List<AmpActivity> list) {
		boolean isExistent = false;
		if (!list.isEmpty()) {
			String ampId = null;
			if (newFieldValue != null) {
				ampId = (String) newFieldValue.get(ActivityFieldsConstants.AMP_ID);
			}

			for (AmpActivity activity : list)
				// we want to avoid comparing the Activity with itself
				if (ampId == null || (activity.getAmpId() != null && !ampId.equalsIgnoreCase(activity.getAmpId()))) {
					isExistent = true;
				}

		}
		return isExistent;
	}

}
