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
			Collection<JsonBean> fieldValue = (Collection) newFieldParent.get(fieldName);
			
			//Set that will hold the values that need to be different between each other
			Set<String> idValuesSet = new HashSet<String>();
			if (fieldValue != null && fieldValue.size() > 1) {
				List<String> fieldPathToId = new ArrayList<String>();

				boolean found = determineIdFieldName(
						(List<JsonBean>) fieldDescription.get(ActivityEPConstants.CHILDREN), fieldPathToId);
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
	 * @param fieldPathToId	List <String> with list of fields parents to access the id field.
	 * @return number of unique id values
	 */
	private Integer populateUniqueValues(Collection<JsonBean> fieldValue, Set<String> idValuesSet,
			List<String> fieldPathToId) {
		Integer totalElements = 0;
		for (JsonBean child : fieldValue) {
			JsonBean result = child;
			for (int i = 0; i < fieldPathToId.size() - 1; i++) {
				result = (JsonBean) result.get(fieldPathToId.get(i));
			}
			totalElements++;
			idValuesSet.add(result.getString(fieldPathToId.get(fieldPathToId.size() - 1)));
		}
		return totalElements;
	}
	
	/**
	 * Explores a List<JsonBean> recursively containing a field of AmpActivityField, until it finds one
	 * with 'id'== true, indicating that field is an ID of the DB, and while doing that creates a List <String> with the
	 * ordered names of the field parents in order to get to the ID field. 
	 * Returns true if an ID field is found.
	 * For example: 
	 *       [{  
	 *        "field_name":"value"
	 *	      },
	 *	      {  
	 *		         "field_name":"aux",
	 *		         "children":[
	 *		            {
	 *		               "field_name":"valueXAux"
	 *		            },
	 *		            {
	 *		               "field_name":"idOfChildAux"
	 *		            }
	 *		         ]
	 *		      },
	 *		      {
	 *		         "field_name":"id",
	 *		         "field_type":"list",
	 *		         "children":[
	 *		            {
	 *		               "field_name":"valueX"
	 *		            },
	 *		            {
	 *		               "field_name":"idOfChild",
	 *		               "id":"true"
	 *		            }
	 *		         ]
	 *		      }]
	 * 
	 * If that List<JsonBean> is received as children, it will process it recursively, creating a fieldPath result of ['id','idOfChild']
	 * and it will return true
	 * @param children List <JsonBean> with list of JsonBean representing some attributes of a field on AmpActivityField
	 * @param fieldPath List <String> containing the list of ordered field names to access to the field
	 * @return true if a field that is an Id of the DB is found, false otherwise
	 */
	private boolean determineIdFieldName(List<JsonBean> children, List<String> fieldPath) {
		 boolean found = false;
		if (children != null && children.size() > 0) {
			for (JsonBean child : children) {
				String id = child.getString(ActivityEPConstants.ID);

				// If the 'id' attribute is present and true, then it is the
				// field that acts as DB identifier
				// We found the desired field
				if (id != null && Boolean.valueOf(id)) {
					fieldPath.add(child.getString(ActivityEPConstants.FIELD_NAME));
					found = true;
					break;
				}
				// If the present field has children, descend on the
				// substructure
				if (child.getString(ActivityEPConstants.CHILDREN) != null) {

					// add the field name to the path to access the id field.
					fieldPath.add(child.getString(ActivityEPConstants.FIELD_NAME));
					found = determineIdFieldName((List<JsonBean>) child.get(ActivityEPConstants.CHILDREN), fieldPath);
				}
				if (found) {
					break;
				}
			}
			if (!found) {
				fieldPath.remove(fieldPath.size() - 1);
			}

		}
		return found;
	}

}
