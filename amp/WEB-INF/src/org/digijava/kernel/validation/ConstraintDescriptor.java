package org.digijava.kernel.validation;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Describes a single constraint.
 *
 * @author Octavian Ciubotaru
 */
public class ConstraintDescriptor {

    private final Class<? extends ConstraintValidator> constraintValidatorClass;

    private final Map<String, String> arguments;

    private final Set<Class<?>> groups;

    private final ConstraintTarget target;

    public enum ConstraintTarget {
        /**
         * Used to mark validators declared at class level.
         */
        TYPE,

        /**
         * Used to mark validators declared on field.
         */
        FIELD
    }

    public ConstraintDescriptor(Class<? extends ConstraintValidator> constraintValidatorClass,
            Map<String, String> arguments, Set<Class<?>> groups, ConstraintTarget target) {
        this.constraintValidatorClass = constraintValidatorClass;
        this.arguments = ImmutableMap.copyOf(arguments);
        this.groups = ImmutableSet.copyOf(groups);
        this.target = target;
    }

    public Class<? extends ConstraintValidator> getConstraintValidatorClass() {
        return constraintValidatorClass;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    /**
     * Validation groups for which this constraint applies to. If empty then constraint applies to the default group.
     *
     * @return validation groups
     */
    public Set<Class<?>> getGroups() {
        return groups;
    }

    public ConstraintTarget getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstraintDescriptor)) {
            return false;
        }
        ConstraintDescriptor that = (ConstraintDescriptor) o;
        return constraintValidatorClass.equals(that.constraintValidatorClass)
                && arguments.equals(that.arguments)
                && groups.equals(that.groups)
                && target == that.target;
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraintValidatorClass, arguments, groups, target);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConstraintDescriptor.class.getSimpleName() + "[", "]")
                .add("constraintValidatorClass=" + constraintValidatorClass)
                .add("arguments=" + arguments)
                .add("groups=" + groups)
                .add("target=" + target)
                .toString();
    }
}
