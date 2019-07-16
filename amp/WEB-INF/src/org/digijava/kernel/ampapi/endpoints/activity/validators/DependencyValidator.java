package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeDependencyResolver;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Validates that dependencies are met and all fields required by dependency are specified 
 */
public class DependencyValidator extends InputValidator {

    @Override
    public boolean isValid(ObjectImporter importer,
            Map<String, Object> newFieldParent,
            Map<String, Object> oldFieldParent, APIField fieldDescription,
            String fieldPath) {
        Object value = newFieldParent.get(fieldDescription.getFieldName());
        List<String> deps = fieldDescription.getDependencies();
        if (deps != null)
        {
            boolean result = false;
            for (String dep : deps) {
                switch(InterchangeDependencyResolver.checkDependency(value, importer, dep, newFieldParent)) {
                    case INVALID_NOT_CONFIGURABLE:
                        errors.add(dep);
                        break;
                    case VALID:
                        result = true;
                    default:
                        break;
                    }
            }
            return result;
        }
        return true;
    }

    private ArrayList<String> errors = new ArrayList<String>();
    
    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.DEPENDENCY_NOT_MET.withDetails(errors);
    }

}
