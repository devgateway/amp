/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.contact.validators.PrimaryOrganisationContactValidator;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Defines input validation chain and executes it
 * 
 * @author Nadejda Mandrescu
 */
public class InputValidatorProcessor {

    public static List<InputValidator> getActivityValidators() {
        return Arrays.asList(
                new ValidFieldValidator(),
                new AllowedInputValidator(),
                new InputTypeValidator(),
                new RequiredValidator(),
                new ActivityTitleValidator(),
                new AmpActivityIdValidator(),
                new MultipleEntriesValidator(),
                new UniqueValidator(),
                new PercentageValidator(),
                new ValueValidator(),
                new TreeCollectionValidator(),
                new DependencyValidator(),
                new PrimaryContactValidator(),
                new AgreementCodeValidator(),
                new AgreementTitleValidator(),
                new FundingOrgRolesValidator(),
                new RegexPatternValidator());
    }

    public static List<InputValidator> getContactValidators() {
        return Arrays.asList(
                new ValidFieldValidator(),
                new InputTypeValidator(),
                new RequiredValidator(),
                new MultipleEntriesValidator(),
                new UniqueValidator(),
                new ValueValidator(),
                new PrimaryOrganisationContactValidator(),
                new RegexPatternValidator());
    }
    
    public static List<InputValidator> getResourceValidators() {
        return Arrays.asList(
                new ValidFieldValidator(),
                new InputTypeValidator(),
                new RequiredValidator(),
                new ValueValidator());
    }

    private final List<InputValidator> validators;

    public InputValidatorProcessor(List<InputValidator> validators) {
        this.validators = validators;
    }

    /**
     * Executes validation chain for the new field values
     *
     * @param importer         Activity Importer instance that holds other import information
     * @param newFieldValue    new field JSON structure
     * @param oldFieldValue    old field JSON structure
     * @param fieldDescription field description
     * @param errors           map to store errors
     * @return true if the current field passes the full validation chain
     */
    public boolean isValid(ObjectImporter importer, Map<String, Object> newParent, Map<String, Object> oldParent,
                           APIField fieldDef, String fieldPath, Map<Integer, ApiErrorMessage> errors) {
        boolean valid = true;
        String fieldName = fieldPath.substring(fieldPath.lastIndexOf("~") + 1);
        for (InputValidator current : validators) {
            boolean currentValid = current.isValid(importer, newParent, oldParent, fieldDef, fieldPath);
            valid = currentValid && valid;

            if (!currentValid) {
                addError(newParent, fieldName, fieldPath, current.getErrorMessage(), errors);
            }

            if (!(currentValid && current.isContinueOnSuccess() || !currentValid && current.isContinueOnError())) {
                break;
            }
        }
        return valid;
    }

    /**
     * Adds a specific error message to the set of errors for the given JSON section (newParent)
     * and fieldName that is part of it
     * @param newParent parent JSON structure to update
     * @param fieldName fieldName
     * @param fieldPath full field path
     * @param error the error to add
     * @param errors the errors stored so far
     */
    public void addError(Map<String, Object> newParent, String fieldName, String fieldPath, ApiErrorMessage error,
            Map<Integer, ApiErrorMessage> errors) {
        // REFACTOR: to split to reuse some functionality in order to be able to add a generic error not necessarily
        // related to a field
        String errorCode = ApiError.getErrorCode(error);
        JsonBean newField = new JsonBean();
        Object newFieldValue = newParent.get(fieldName);
        // if some errors where already reported, use new field storage
        if (newFieldValue instanceof JsonBean && ((JsonBean) newFieldValue).get(ActivityEPConstants.INVALID) != null) {
            newField = (JsonBean) newFieldValue;
        } else {
            // store original input
            newField.set(ActivityEPConstants.INPUT, newFieldValue);
            // and initialize fields errors list
            newField.set(ActivityEPConstants.INVALID, new HashSet<String>());
            // record new wrapped field value
            newParent.put(fieldName, newField);
        }

        // register field level invalid errors
        ((Set<String>) newField.get(ActivityEPConstants.INVALID)).add(errorCode);

        // record general errors for the request
        ApiErrorMessage generalError = errors.get(error.id);
        if (generalError == null) {
            generalError = error;
        }
        generalError = generalError.withDetails(fieldPath);
        errors.put(error.id, generalError);
    }

}
