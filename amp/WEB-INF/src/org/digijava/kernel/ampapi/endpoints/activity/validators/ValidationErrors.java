/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Defines validation errors
 *
 * @author Viorel Chihai
 */
public final class ValidationErrors {
    
    public static final ApiErrorMessage FIELD_REQUIRED = new ApiErrorMessage(1, 0, "Required field");
    
    public static final ApiErrorMessage FIELD_INVALID_TYPE = new ApiErrorMessage(1, 1, "Invalid field type");
    
    public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(1, 2, "Invalid field value");
    
    public static final ApiErrorMessage FIELD_READ_ONLY = new ApiErrorMessage(1, 3, "Read-only field");
    
    public static final ApiErrorMessage FIELD_UNQUE_VALUES = new ApiErrorMessage(1, 4, "Unique values required");
    
    public static final ApiErrorMessage FIELD_INVALID = new ApiErrorMessage(1, 5, "Invalid field");
    
    public static final ApiErrorMessage FIELD_INVALID_LENGTH = new ApiErrorMessage(1, 6, "Invalid field length");
    
    public static final ApiErrorMessage FIELD_PERCENTAGE_SUM_BAD = new ApiErrorMessage(1, 7,
            "Sum of percentage fields has to be 100");
    
    public static final ApiErrorMessage FIELD_PARENT_CHILDREN_NOT_ALLOWED = new ApiErrorMessage(1, 8,
            "Parent and child cannot be in the same collection");
    
    public static final ApiErrorMessage UNIQUE_PRIMARY_CONTACT = new ApiErrorMessage(1, 9,
            "Multiple primary contacts not allowed");
    
    public static final ApiErrorMessage FIELD_INVALID_PERCENTAGE = new ApiErrorMessage(1, 10,
            "Percentage fields have to be >0, <=100");
    
    public static final ApiErrorMessage AGREEMENT_CODE_REQUIRED = new ApiErrorMessage(1, 11,
            "Agreement code is required");
    
    public static final ApiErrorMessage AGREEMENT_CODE_UNIQUE = new ApiErrorMessage(1, 12,
            "Agreement code should be unique");
    
    public static final ApiErrorMessage AGREEMENT_TITLE_REQUIRED = new ApiErrorMessage(1, 13,
            "Agreement title is required");
    
    public static final ApiErrorMessage ORGANIZATION_ROLE_PAIR_NOT_DECLARED = new ApiErrorMessage(1, 14,
            "The organization and role pair is not declared");
    
    public static final ApiErrorMessage ORGANIZATION_NOT_DECLARED = new ApiErrorMessage(1, 15,
            "The organization is not declared");
    
    public static final ApiErrorMessage FIELD_TOO_MANY_VALUES_NOT_ALLOWED = new ApiErrorMessage(1, 16,
            "Too many values");
    
    public static final ApiErrorMessage IMPLEMENTATION_LEVEL_NOT_SPECIFIED = new ApiErrorMessage(1, 17,
            "Implementation level must be specified");
    
    public static final ApiErrorMessage DOESNT_MATCH_IMPLEMENTATION_LEVEL = new ApiErrorMessage(1, 18,
            "Doesn't match with implementation level");
    
    public static final ApiErrorMessage INVALID_ID = new ApiErrorMessage(1, 19, "Invalid id");
    
    private ValidationErrors() { }
}
