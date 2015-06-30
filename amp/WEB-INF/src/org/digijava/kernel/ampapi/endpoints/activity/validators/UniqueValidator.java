/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Validates that unique values are provided when within a list required to have
 * unique values
 * 
 * @author Nadejda Mandrescu
 */
public class UniqueValidator extends InputValidator {

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_UNQUE_VALUES;
	}

	@Override
	public boolean isValid(ActivityImporter importer, JsonBean newFieldParent, JsonBean oldFieldParent,
			JsonBean fieldDescription, String fieldPath) {
		boolean isValid = true;
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		Boolean unique = Boolean.valueOf(fieldDescription.getString(ActivityEPConstants.UNIQUE));
		if (unique) {
			Collection<JsonBean> fieldValue = (Collection) newFieldParent.get(fieldName);
			Set<String> idValuesSet = new HashSet<String>();
			if (fieldValue.size() > 1) {
				String idFieldName = determineIdFieldName((List<JsonBean>) fieldDescription
						.get(ActivityEPConstants.CHILDREN));
				for (JsonBean child : fieldValue) {
					String idValue = child.getString(idFieldName);
					idValuesSet.add(idValue);
				}
				if (idValuesSet.size() < fieldValue.size()) {
					isValid = false;
				}
			}

		}

		return isValid;
	}

	private String determineIdFieldName(List<JsonBean> children) {
		String fieldName = "";
		if (children != null && children.size() > 0) {
			for (JsonBean child : children) {
				String id = child.getString(ActivityEPConstants.ID);
				if (id != null && Boolean.valueOf(id)) {
					fieldName = child.getString(ActivityEPConstants.FIELD_NAME);
					if ("list".equals(child.getString(ActivityEPConstants.FIELD_TYPE))) {
						fieldName = determineIdFieldName((List<JsonBean>) child.get(ActivityEPConstants.CHILDREN));
					}
				}

			}

		}
		return fieldName;

	}

}
