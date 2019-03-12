package org.digijava.module.aim.validator.percentage;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.mutable.MutableFloat;

/**
 * Validates that the percentages from a collection sum up to 100. Percentage for each element is retrieved
 * via {@link AbstractTotalPercentageValidator#getPercentage(Object)}.
 *
 * <p>Elements can be grouped. In this case the sum must be 100 within each group. Group for each element is retrieved
 * via {@link AbstractTotalPercentageValidator#getItemGroup(Object)}.</p>
 *
 * <p>Validation can be skipped on a per group basis.</p>
 *
 * <p>One constraint violation will be reported for each group in which percentages do not sum up to 100.</p>
 *
 * <p>Corner cases:
 * <ul><li>Null is a valid group.</li>
 * <li>Null percentage is treated as a zero.</li></ul></p>
 *
 * @author Octavian Ciubotaru
 */
public abstract class AbstractTotalPercentageValidator<A extends Annotation, T, K>
        implements ConstraintValidator<A, T> {

    private static final float ERROR = 0.0001f;
    private static final float ONE_HUNDRED = 100f;

    @Override
    public void initialize(A constraintAnnotation) {
    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        Iterable collection = getCollection(value);
        if (collection == null) {
            // nothing to validate
            return true;
        }

        context.disableDefaultConstraintViolation();

        Map<K, MutableFloat> totals = new HashMap<>();
        for (Object item : collection) {
            K group = getItemGroup(item);
            if (!skipValidationForGroup(group)) {
                Float percentage = getPercentage(item);
                MutableFloat total = totals.computeIfAbsent(group, r -> new MutableFloat());
                if (percentage != null) {
                    total.add(percentage);
                }
            }
        }

        boolean valid = true;
        for (Map.Entry<K, MutableFloat> entry : totals.entrySet()) {
            if (Math.abs(ONE_HUNDRED - entry.getValue().floatValue()) > ERROR) {
                addConstraintViolationForGroup(context, entry.getKey());
                valid = false;
            }
        }

        return valid;
    }

    /**
     * Return the collection to be validated.
     */
    public abstract Iterable getCollection(T value);

    /**
     * The object used to group percentages. Must implement hashCode and equals correctly.
     */
    public abstract K getItemGroup(Object collectionItem);

    /**
     * Return true if validation should be skipped for the specified group.
     */
    public boolean skipValidationForGroup(K group) {
        return false;
    }

    /**
     * Return percentage for a collection item.
     */
    public abstract Float getPercentage(Object collectionItem);

    /**
     * Build and add the constraint violation.
     * @param context context in which the constraint is evaluated
     * @param group group
     */
    public abstract void addConstraintViolationForGroup(ConstraintValidatorContext context, K group);
}
