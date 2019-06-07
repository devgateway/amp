package org.digijava.kernel.validation;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public class ConstraintValidatorContextImpl implements ConstraintValidatorContext {

    private Set<ConstraintViolation> customConstraintViolations = new LinkedHashSet<>();

    private boolean defaultConstraintViolation = true;

    private ConstraintDescriptor constraintDescriptor;

    private PathImpl path;

    public ConstraintValidatorContextImpl(ConstraintDescriptor constraintDescriptor, PathImpl path) {
        this.constraintDescriptor = constraintDescriptor;
        this.path = path;
    }

    @Override
    public void disableDefaultConstraintViolation() {
        defaultConstraintViolation = false;
    }

    boolean isDefaultConstraintViolationDisabled() {
        return !defaultConstraintViolation;
    }

    Set<ConstraintViolation> getCustomConstraintViolations() {
        return customConstraintViolations;
    }

    @Override
    public ConstraintViolationBuilder buildConstraintViolation(ApiErrorMessage message) {
        return new ConstraintViolationBuilderImpl(message, path);
    }

    class ConstraintViolationBuilderImpl implements ConstraintViolationBuilder {

        private ApiErrorMessage message;

        private PathImpl path;

        private ImmutableMap.Builder<String, Object> attributesBuilder = new ImmutableMap.Builder<>();

        ConstraintViolationBuilderImpl(ApiErrorMessage message, PathImpl path) {
            this.message = message;
            this.path = path;
        }

        @Override
        public ConstraintViolationBuilder addPropertyNode(String name) {
            path = path.addPropertyNode(name);
            return this;
        }

        @Override
        public ConstraintViolationBuilder addAttribute(String name, Object value) {
            attributesBuilder.put(name, value);
            return this;
        }

        @Override
        public void addConstraintViolation() {
            ImmutableMap<String, Object> attributes = attributesBuilder.build();
            customConstraintViolations.add(new ConstraintViolation(message, constraintDescriptor, path, attributes));
        }
    }
}
