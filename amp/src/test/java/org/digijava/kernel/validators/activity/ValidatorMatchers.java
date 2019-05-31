package org.digijava.kernel.validators.activity;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.isA;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintViolation;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * @author Octavian Ciubotaru
 */
public final class ValidatorMatchers {

    private ValidatorMatchers() {
    }

    public static Matcher<ConstraintViolation> violationFor(
            Class<? extends ConstraintValidator> validatorClass,
            String path,
            Matcher attributesMatcher,
            ApiErrorMessage message) {
        return allOf(
                isA(ConstraintViolation.class),
                hasProperty("constraintDescriptor",
                        hasProperty("constraintValidatorClass", equalTo(validatorClass))),
                hasProperty("attributes", attributesMatcher),
                hasProperty("path", hasToString(path)),
                hasProperty("message", equalTo(message)));
    }

    public static <T> Matcher<java.lang.Iterable<? extends T>> containsInAnyOrder(
            Matcher<? super T> i1, Matcher<? super T> i2) {
        return Matchers.containsInAnyOrder(ImmutableList.of(i1, i2));
    }
}
