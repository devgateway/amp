package org.digijava.kernel.validators.resource;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.resource.AmpResource;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceErrors;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceType;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

/**
 * Require web link or file name depending on the type of the resource.
 *
 * @author Octavian Ciubotaru
 */
public class ResourceRequiredValidator implements ConstraintValidator {

    public static final String RESOURCE_TYPE_FILE_VALID_KEY = "resource_type_file_valid_key";
    public static final String RESOURCE_TYPE_LINK_VALID_KEY = "resource_type_link_valid_key";

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        AmpResource resource = (AmpResource) value;
        ResourceType resourceType = resource.getResourceType();

        if (resourceType == null) {
            return true;
        }

        String requiredFieldName;
        if (resourceType == ResourceType.LINK) {
            requiredFieldName = "web_link";
        } else if (resourceType == ResourceType.FILE) {
            requiredFieldName = "file_name";
        } else {
            throw new IllegalArgumentException("Unknown resource type: " + resourceType);
        }

        String fieldValue = type.getField(requiredFieldName).getFieldAccessor().get(value);

        if (StringUtils.isBlank(fieldValue)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolation(ResourceErrors.FIELD_REQUIRED)
                    .addPropertyNode(requiredFieldName)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return null;
    }
}
