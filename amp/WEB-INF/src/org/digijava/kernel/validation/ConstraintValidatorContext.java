package org.digijava.kernel.validation;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Context object that allows to report custom constraint violations.
 *
 * @author Octavian Ciubotaru
 */
public interface ConstraintValidatorContext {

    /**
     * Disables the default constraint violation.
     *
     * For the custom violations to be reported, constraint validator must mark the value as invalid.
     */
    void disableDefaultConstraintViolation();

    /**
     * Returns constraint violation builder.
     *
     * @param message error message of the violation
     * @return builder
     */
    ConstraintViolationBuilder buildConstraintViolation(ApiErrorMessage message);

    /**
     * Allows to build custom constraint violation.
     */
    interface ConstraintViolationBuilder {

        /**
         * Add a property node to the current path.
         * @param name name of the field
         * @return this builder
         */
        ConstraintViolationBuilder addPropertyNode(String name);

        /**
         * Add an attribute to the violation.
         * @param name name of the attribute
         * @param value value of the attribute
         * @param <T> type of the value
         * @return this builder
         */
        <T> ConstraintViolationBuilder addAttribute(String name, T value);

        /**
         * Report this constraint violation if the constraint validator marks the value as invalid.
         */
        void addConstraintViolation();
    }
}
