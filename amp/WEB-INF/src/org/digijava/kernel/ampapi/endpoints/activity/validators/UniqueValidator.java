/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
			Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
		boolean isValid = true;
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		Boolean unique = Boolean.valueOf(fieldDescription.getString(ActivityEPConstants.UNIQUE));
		if (unique) {
			// get Collection with values to be unique
			Collection<Map<String, Object>> fieldValue = (Collection<Map<String, Object>>) newFieldParent.get(fieldName);
			
			//Set that will hold the values that need to be different between each other
			Set<Object> idValuesSet = new HashSet<Object>();
			if (fieldValue != null && fieldValue.size() > 1) {
				String fieldPathToId = determineUniqueFieldName((List<JsonBean>) fieldDescription.get(ActivityEPConstants.CHILDREN));
				Integer totalElements = populateUniqueValues(fieldValue, idValuesSet, fieldPathToId);
				
				//if the set contains less elements, then we have some repeated values
				if (idValuesSet.size() < totalElements) {
					isValid = false;
				}
			}

		}

		return isValid;
	}

	/**
	 * Populates a Set of unique id values from the Collection <JsonBean>
	 * containing the fields and returns the number of unique id values.
	 * 
	 * @param fieldValue 	the Collection <JsonBean> with a list of fields from which the
	 * @param idValuesSet	the Set to be populated with unique id values
	 * @param fieldPathToId	String path of the parent.
	 * @return number of unique id values
	 */
	private Integer populateUniqueValues(Collection<Map<String, Object>> fieldValues, Set<Object> idValuesSet, String fieldPathToId) {
		Integer totalElements = 0;
		for (Map<String, Object> child : fieldValues) {
			if (child.get(fieldPathToId) != null) {
				totalElements++;
				idValuesSet.add(child.get(fieldPathToId));
			}
		}
		
		return totalElements;
	}
	
	/**
	 * Explores a List<JsonBean> recursively containing field values, until it finds one
	 * with 'unique'== true, indicating that field is an ID of the DB
	 * 
	 * @param fieldValues List <JsonBean> with list of JsonBean representing attributes of an object
	 * @return name of the field that is an Id of the DB
	 */
	private String determineUniqueFieldName(List<JsonBean> fieldValues) {
		for (JsonBean val : fieldValues) {
			String id = val.getString(ActivityEPConstants.ID);

			// If the 'id' attribute is present and true, then it is the
			// field that acts as DB identifier
			// We found the desired field
			if (id != null && Boolean.valueOf(id)) {
				return val.getString("field_name");
			}
		}
		
		return "";
	}

}
