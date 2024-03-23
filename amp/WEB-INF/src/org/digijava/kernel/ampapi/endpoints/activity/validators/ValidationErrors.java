package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_VALIDATION_ID;

/**
 * Defines validation errors
 *
 * @author Viorel Chihai
 */
public final class ValidationErrors {
    
    public static final ApiErrorMessage FIELD_REQUIRED = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 0,
            "Required field");
    
    public static final ApiErrorMessage FIELD_INVALID_TYPE = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 1,
            "Invalid field type");
    
    public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 2,
            "Invalid field value");
    
    public static final ApiErrorMessage FIELD_READ_ONLY = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 3,
            "Read-only field");
    
    public static final ApiErrorMessage FIELD_UNQUE_VALUES = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 4,
            "Unique values required");
    
    public static final ApiErrorMessage FIELD_INVALID = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 5,
            "Invalid field");
    
    public static final ApiErrorMessage FIELD_INVALID_LENGTH = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 6,
            "Invalid field length");
    
    public static final ApiErrorMessage FIELD_PERCENTAGE_SUM_BAD = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 7,
            "Sum of percentage fields has to be 100");
    
    public static final ApiErrorMessage FIELD_PARENT_CHILDREN_NOT_ALLOWED =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 8, "Parent and child cannot be in the same collection");
    
    public static final ApiErrorMessage UNIQUE_PRIMARY_CONTACT = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 9,
            "Multiple primary contacts not allowed");
    
    public static final ApiErrorMessage FIELD_INVALID_PERCENTAGE =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 10, "Percentage fields have to be >0, <=100");
    
    public static final ApiErrorMessage AGREEMENT_CODE_REQUIRED = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 11,
            "Agreement code is required");
    
    public static final ApiErrorMessage AGREEMENT_CODE_UNIQUE = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 12,
            "Agreement code should be unique");
    
    public static final ApiErrorMessage AGREEMENT_TITLE_REQUIRED = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 13,
            "Agreement title is required");
    
    public static final ApiErrorMessage ORGANIZATION_ROLE_PAIR_NOT_DECLARED =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 14, "The organization and role pair is not declared");
    
    public static final ApiErrorMessage ORGANIZATION_NOT_DECLARED = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 15,
            "The organization is not declared");
    
    public static final ApiErrorMessage FIELD_TOO_MANY_VALUES_NOT_ALLOWED =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 16, "Too many values");
    
    public static final ApiErrorMessage IMPLEMENTATION_LEVEL_NOT_SPECIFIED =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 17, "Implementation level must be specified");
    
    public static final ApiErrorMessage DOESNT_MATCH_IMPLEMENTATION_LEVEL =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 18, "Doesn't match with implementation level");

    public static final ApiErrorMessage INVALID_ID = new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 19, "Invalid id");

    public static final ApiErrorMessage LOCATION_NOT_DECLARED =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 20, "Location is not declared.");

    public static final ApiErrorMessage PROGRAM_MAPPING_DOESNT_MATCH =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 21, "Doesn't match with mapped program");

    public static final ApiErrorMessage FIELD_SOURCE_PROGRAM_NOT_SPECIFIED =
            new ApiErrorMessage(ERROR_CLASS_VALIDATION_ID, 22, "The source program mapping is missing");

    private ValidationErrors() { }
}
