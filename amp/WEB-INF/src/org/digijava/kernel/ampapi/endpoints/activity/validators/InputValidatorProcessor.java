package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Defines input validation chain and executes it
 *
 * @author Nadejda Mandrescu
 */
public class InputValidatorProcessor {

    public static List<InputValidator> getFormatValidators() {
        return Arrays.asList(
                new ValidFieldValidator(),
                new InputTypeValidator(),
                new ValidLocaleValidator(),
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
     * @param newParent        new field JSON structure
     * @param fieldDef         field description
     * @return true if the current field passes the full validation chain
     */
    public boolean isValid(ObjectImporter importer, Object newParent, Map<String, Object> newJsonParent,
                           APIField fieldDef, String fieldPath) {
        boolean valid = true;
        String fieldName = fieldPath.substring(fieldPath.lastIndexOf("~") + 1);
        for (InputValidator current : validators) {
            boolean currentValid = current.isValid(importer, newParent, newJsonParent, fieldDef, fieldPath);
            valid = currentValid && valid;
            
            if (!currentValid) {
                ErrorDecorator.addError(newJsonParent, fieldName, fieldPath, current.getErrorMessage(),
                        importer.getErrors());
            }

            if (!(currentValid && current.isContinueOnSuccess() || !currentValid && current.isContinueOnError())) {
                break;
            }
        }
        return valid;
    }
}
