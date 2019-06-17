package org.digijava.kernel.validators.common;

import static org.digijava.kernel.validators.ValidatorUtil.getDefaultTranslationContext;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.kernel.validators.activity.ValidatorMatchers;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class SizeValidatorTest {

    private static APIField objField;

    public class TestObject {

        @Interchangeable(
                fieldTitle = "max1",
                interValidators = @InterchangeableValidator(
                        value = SizeValidator.class,
                        attributes = "max=1"))
        private List<Integer> max1;

        @Interchangeable(
                fieldTitle = "max3",
                interValidators = @InterchangeableValidator(
                        value = SizeValidator.class,
                        attributes = "max=3"))
        private List<Integer> max3;
    }

    @BeforeClass
    public static void setUp() {
        objField = ValidatorUtil.getMetaData(TestObject.class);
    }

    @Test
    public void testNull() {
        TestObject testObject = new TestObject();

        Set<ConstraintViolation> violations = getConstraintViolations(testObject);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmpty() {
        TestObject testObject = new TestObject();
        testObject.max1 = ImmutableList.of();
        testObject.max3 = ImmutableList.of();

        Set<ConstraintViolation> violations = getConstraintViolations(testObject);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test1AtLimit() {
        TestObject testObject = new TestObject();
        testObject.max1 = ImmutableList.of(1);

        Set<ConstraintViolation> violations = getConstraintViolations(testObject);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test3AtLimit() {
        TestObject testObject = new TestObject();
        testObject.max3 = ImmutableList.of(1, 2, 3);

        Set<ConstraintViolation> violations = getConstraintViolations(testObject);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test1OverLimit() {
        TestObject testObject = new TestObject();
        testObject.max1 = ImmutableList.of(1, 2);

        Set<ConstraintViolation> violations = getConstraintViolations(testObject);

        assertThat(violations, contains(violation("max1")));
    }

    @Test
    public void test3OverLimit() {
        TestObject testObject = new TestObject();
        testObject.max3 = ImmutableList.of(1, 2, 3, 4);

        Set<ConstraintViolation> violations = getConstraintViolations(testObject);

        assertThat(violations, contains(violation("max3")));
    }

    private Matcher<ConstraintViolation> violation(String path) {
        return ValidatorMatchers.violationFor(SizeValidator.class, path, anything(),
                ActivityErrors.FIELD_TOO_MANY_VALUES_NOT_ALLOWED);
    }

    private Set<ConstraintViolation> getConstraintViolations(Object object) {
        Validator validator = new Validator();
        return validator.validate(objField, object, getDefaultTranslationContext());
    }
}