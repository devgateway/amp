package org.digijava.kernel.validation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.DiscriminatedFieldAccessor;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;

/**
 * Validator that follows type information from APIField.
 *
 * Note: This validator from outside is very similar to the validator from bean validation with the exception that
 * it 'walks' the object differently. This validator uses type information provided by APIField to walk the object
 * as opposed to plain reflection from bean validation.
 *
 * @author Octavian Ciubotaru
 */
public class Validator {

    /**
     * Validates object recursively by invoking validators defined at field and object levels.
     *
     * Accepts only values that are objects.
     *
     * @param type type info, APIField is used just for apiType and children, no other info is used
     * @param value value must be an object, never collection of objects or primitives
     * @param groups validate only for specified groups, if empty will validate only Default group
     * @return constraint violations if any
     */
    public Set<ConstraintViolation> validate(APIField type, Object value, Class<?>... groups) {

        if (type.getApiType().getFieldType() != FieldType.OBJECT) {
            throw new IllegalArgumentException("Invalid type: " + type.getApiType().getFieldType());
        }

        PathImpl path = rootPath();

        ValidationContext validationContext = new ValidationContext();

        validate(type, value, ImmutableSet.copyOf(groups), validationContext, path);

        return validationContext.getFailingConstraintViolations();
    }

    /**
     * Validates object recursively by invoking validators defined at field and object levels.
     *
     * Called only on objects. Never on collections or primitive types.
     *
     * First this method will invoke all constraint validators declared on the class.
     * After that will validate all fields of the object separately by invoking field constraints and then if
     * value is an object or a list of objects, it will recursively validate the object.
     *
     * @param type type info, APIField is used just for apiType and children, no other info is used
     * @param value value must be an object, never collection of objects or primitives
     * @param groups validate only for specified groups, if empty will validate only Default group
     * @param validationContext a collector for violations
     * @param path current path
     */
    private void validate(APIField type, Object value, Set<Class<?>> groups,
            ValidationContext validationContext, PathImpl path) {

        List<ConstraintDescriptor> constraintDescriptors = type.getBeanConstraints().getDescriptorsFor(groups);
        for (ConstraintDescriptor constraintDescriptor : constraintDescriptors) {
            invokeConstraintValidator(type, value, validationContext, constraintDescriptor, path);
        }

        for (APIField field : type.getChildren()) {
            PathImpl fieldPath = advancePath(path, field);

            Object fieldValue = field.getFieldAccessor().get(value);
            validateField(field, fieldValue, groups, validationContext, fieldPath);

            if (fieldValue != null) {
                cascadeValidationForObjects(field, fieldValue, groups, validationContext, fieldPath);
            }
        }
    }

    /**
     * Invoke object validation recursively for objects or list of objects.
     *
     * @param type type of the value
     * @param value value to validate
     * @param groups validation groups
     * @param validationContext a collector for violations
     * @param path current path
     */
    private void cascadeValidationForObjects(APIField type, Object value, Set<Class<?>> groups,
            ValidationContext validationContext, PathImpl path) {
        if (type.getApiType().isAnObject()) {

            // TODO remove this after refactor to DiscriminatedFieldAccessor
            if (type.isDiscriminatedObject()) {
                value = DiscriminatedFieldAccessor.unwrapSingleElement((Collection) value);
            }

            if (value != null) { // TODO remove this condition after refactor to DiscriminatedFieldAccessor
                validate(type, value, groups, validationContext, path);
            }
        } else if (type.getApiType().isAListOfObjects()) {
            Collection subFieldValueList = (Collection) value;
            for (Object el : subFieldValueList) {
                if (el != null) {
                    validate(type, el, groups, validationContext, path);
                }
            }
        }
    }

    /**
     * Validate just a single field. Will invoke only constraint validators defined on the field.
     * Validation process is not recursive.
     *
     * @param field field metadata
     * @param value value of the field
     * @param groups validation groups
     * @return constraint violations if any
     */
    public Set<ConstraintViolation> validateField(APIField field, Object value, Class<?>... groups) {

        PathImpl fieldPath = advancePath(rootPath(), field);

        ValidationContext validationContext = new ValidationContext();

        validateField(field, value, ImmutableSet.copyOf(groups), validationContext, fieldPath);

        return validationContext.getFailingConstraintViolations();
    }

    /**
     * Validate a field.
     *
     * @param field field metadata
     * @param value value of the field
     * @param groups validation groups
     * @param validationContext a collector for violations
     * @param path current path
     */
    private void validateField(APIField field, Object value, Set<Class<?>> groups,
            ValidationContext validationContext, PathImpl path) {

        List<ConstraintDescriptor> constraintDescriptors =
                field.getFieldConstraints().getDescriptorsFor(groups);

        for (ConstraintDescriptor constraintDescriptor : constraintDescriptors) {
            invokeConstraintValidator(field, value, validationContext, constraintDescriptor, path);
        }
    }

    private void invokeConstraintValidator(APIField type, Object value, ValidationContext validationContext,
            ConstraintDescriptor constraintDescriptor, PathImpl path) {

        ConstraintValidator constraintValidator = instantiateAndInitialize(constraintDescriptor);

        ConstraintValidatorContextImpl context = new ConstraintValidatorContextImpl(constraintDescriptor, path);

        if (!constraintValidator.isValid(type, value, context)) {
            if (context.isDefaultConstraintViolationDisabled()) {
                validationContext.getFailingConstraintViolations().addAll(context.getCustomConstraintViolations());
            } else {
                ConstraintViolation violation =
                        new ConstraintViolation(constraintValidator.getErrorMessage(), constraintDescriptor, path);
                validationContext.getFailingConstraintViolations().add(violation);
            }
        }
    }

    /**
     * Create and initialize a constraint validator.
     *
     * @param descriptor constraint descriptor
     * @return constraint validator
     */
    private ConstraintValidator instantiateAndInitialize(ConstraintDescriptor descriptor) {
        try {
            ConstraintValidator validator = descriptor.getConstraintValidatorClass().newInstance();
            validator.initialize(descriptor.getArguments());
            return validator;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate validator", e);
        }
    }

    /**
     * Create path for the root of the object.
     *
     * @return root path
     */
    private PathImpl rootPath() {
        return new PathImpl();
    }

    /**
     * Returns the path for the specified field.
     *
     * @param path current path
     * @param field field for which to extend the path
     * @return path that points to the field
     */
    private PathImpl advancePath(PathImpl path, APIField field) {
        return path.addPropertyNode(field.getFieldName());
    }
}
