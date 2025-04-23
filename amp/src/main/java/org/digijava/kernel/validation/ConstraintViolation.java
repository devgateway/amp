package org.digijava.kernel.validation;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Describes a constraint violation. This object exposes the constraint violation context (path and attributes)
 * as well as the message describing the violation.
 *
 * @author Octavian Ciubotaru
 */
public class ConstraintViolation {

    private final ApiErrorMessage message;

    private final ConstraintDescriptor constraintDescriptor;

    private final Path path;

    private final Map<String, ?> attributes;

    public ConstraintViolation(ApiErrorMessage message, ConstraintDescriptor constraintDescriptor, Path path) {
        this(message, constraintDescriptor, path, ImmutableMap.of());
    }

    public ConstraintViolation(ApiErrorMessage message, ConstraintDescriptor constraintDescriptor, Path path,
            Map<String, ?> attributes) {
        this.message = message;
        this.constraintDescriptor = constraintDescriptor;
        this.path = path;
        this.attributes = ImmutableMap.copyOf(attributes);
    }

    public ApiErrorMessage getMessage() {
        return message;
    }

    public ConstraintDescriptor getConstraintDescriptor() {
        return constraintDescriptor;
    }

    public Path getPath() {
        return path;
    }

    public Map<String, ?> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstraintViolation)) {
            return false;
        }
        ConstraintViolation that = (ConstraintViolation) o;
        return message.equals(that.message)
                && constraintDescriptor.equals(that.constraintDescriptor)
                && path.equals(that.path)
                && attributes.equals(that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, constraintDescriptor, path, attributes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConstraintViolation.class.getSimpleName() + "[", "]")
                .add("message=" + message)
                .add("constraintDescriptor=" + constraintDescriptor)
                .add("path=" + path)
                .add("attributes=" + attributes)
                .toString();
    }
}
