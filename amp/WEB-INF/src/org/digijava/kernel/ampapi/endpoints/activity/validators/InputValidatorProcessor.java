package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.contact.validators.PrimaryOrganisationContactValidator;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines input validation chain and executes it
 *
 * @author Nadejda Mandrescu
 */
public class InputValidatorProcessor {

    public static List<InputValidator> getActivityFormatValidators() {
        return Arrays.asList(
                new ValidFieldValidator(),
                new AllowedInputValidator(),
                new InputTypeValidator(),
                new ValueValidator());
    }

    public static List<InputValidator> getActivityBusinessRulesValidators() {
        return Arrays.asList(
                new RequiredValidator(),
                new ActivityTitleValidator(),
                new AmpActivityIdValidator(),
                new MultipleEntriesValidator(),
                new UniqueValidator(),
                new TreeCollectionValidator(),
                new DependencyValidator(),
                new AgreementCodeValidator(),
                new AgreementTitleValidator(),
                new RegexPatternValidator(),
                new UUIDValidator());
    }

    public static List<InputValidator> getContactFormatValidators() {
        return Arrays.asList(
                new ValidFieldValidator(),
                new InputTypeValidator(),
                new ValueValidator());
    }

    public static List<InputValidator> getContactBusinessRulesValidators() {
        return Arrays.asList(
                new RequiredValidator(),
                new MultipleEntriesValidator(),
                new UniqueValidator(),
                new PrimaryOrganisationContactValidator(),
                new RegexPatternValidator());
    }

    public static List<InputValidator> getResourceFormatValidators() {
        return Arrays.asList(
                new ValidFieldValidator(),
                new InputTypeValidator(),
                new ValueValidator());
    }

    public static List<InputValidator> getResourceBusinessRulesValidators() {
        return Arrays.asList(
                new RequiredValidator());
    }

    private final List<InputValidator> validators;

    public InputValidatorProcessor(List<InputValidator> validators) {
        this.validators = validators;
    }

    /**
     * Executes validation chain for the new field values
     *
     * @param importer         Activity Importer instance that holds other import information
     * @param newParent        new field JSON structure
     * @param fieldDef         field description
     * @param errors           map to store errors
     * @return true if the current field passes the full validation chain
     */
    public boolean isValid(ObjectImporter importer, Map<String, Object> newParent,
            APIField fieldDef, String fieldPath, Map<Integer, ApiErrorMessage> errors) {
        boolean valid = true;
        String fieldName = fieldPath.substring(fieldPath.lastIndexOf("~") + 1);
        for (InputValidator current : validators) {
            boolean currentValid = current.isValid(importer, newParent, fieldDef, fieldPath);
            valid = currentValid && valid;

            if (!currentValid) {
                ErrorDecorator.addError(newParent, fieldName, fieldPath, current.getErrorMessage(), errors);
            }

            if (!(currentValid && current.isContinueOnSuccess() || !currentValid && current.isContinueOnError())) {
                break;
            }
        }
        return valid;
    }
}
