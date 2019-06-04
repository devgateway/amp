package org.digijava.kernel.validators.common;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.kernel.validators.activity.ValidatorMatchers;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class RegexValidatorTest {

    private static APIField objField;

    public static class Obj {

        @Interchangeable(
                fieldTitle = "field",
                interValidators = @InterchangeableValidator(value = RegexValidator.class, attributes = "regex=\\d\\w"))
        private String field;
    }

    @BeforeClass
    public static void setUp() {
        objField = ValidatorUtil.getMetaData(Obj.class);
    }

    @Test
    public void testNullValue() {
        Obj obj = new Obj();

        Set<ConstraintViolation> violations = getConstraintViolations(obj);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidValue() {
        Obj obj = new Obj();
        obj.field = "aa";

        Set<ConstraintViolation> violations = getConstraintViolations(obj);

        assertThat(violations, contains(ValidatorMatchers.violationFor(
                RegexValidator.class, "field", anything(), ActivityErrors.FIELD_INVALID_VALUE)));
    }

    @Test
    public void testValidValue() {
        Obj obj = new Obj();
        obj.field = "9b";

        Set<ConstraintViolation> violations = getConstraintViolations(obj);

        assertThat(violations, emptyIterable());
    }

    private Set<ConstraintViolation> getConstraintViolations(Object object) {
        Validator validator = new Validator();
        return validator.validate(objField, object);
    }
}