package org.digijava.kernel.validators.common;

import static org.digijava.kernel.validators.ValidatorUtil.getDefaultTranslationContext;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.kernel.validators.activity.ValidatorMatchers;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class TotalPercentageValidatorTest {

    public static class Obj {

        @Interchangeable(
                fieldTitle = "items",
                interValidators = @InterchangeableValidator(
                        value = TotalPercentageValidator.class,
                        fmPath = "/totalPercentageConstraint"))
        private Set<Item> items = new HashSet<>();
    }

    public static class Item {

        @Interchangeable(fieldTitle = "id")
        @InterchangeableId
        private Long id;

        @Interchangeable(fieldTitle = "percentage", fmPath = "/percentageField", percentageConstraint = true)
        private Float percentage;

        public Item(Long id, Float percentage) {
            this.id = id;
            this.percentage = percentage;
        }
    }

    private static APIField objField;

    @BeforeClass
    public static void setUp() {
        objField = ValidatorUtil.getMetaData(Obj.class);
    }

    @Test
    public void testEmpty() {
        Obj obj = new Obj();

        Set<ConstraintViolation> violations = getConstraintViolations(objField, obj);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testPercentageFieldDisabled() {
        Obj obj = new Obj();
        obj.items = ImmutableSet.of(new Item(1L, 59f));

        APIField objField = ValidatorUtil.getMetaData(Obj.class, ImmutableSet.of("/percentageField"));

        Set<ConstraintViolation> violations = getConstraintViolations(objField, obj);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testOneElementWithNullPercentage() {
        Obj obj = new Obj();
        obj.items = ImmutableSet.of(new Item(1L, null));

        Set<ConstraintViolation> violations = getConstraintViolations(objField, obj);

        assertThat(violations, contains(violation()));
    }

    @Test
    public void testInvalidPercentageSum() {
        Obj obj = new Obj();
        obj.items = ImmutableSet.of(
                new Item(1L, null),
                new Item(2L, 23f),
                new Item(3L, 25f));

        Set<ConstraintViolation> violations = getConstraintViolations(objField, obj);

        assertThat(violations, contains(violation()));
    }

    @Test
    public void testValidSum() {
        Obj obj = new Obj();
        obj.items = ImmutableSet.of(
                new Item(1L, 33f),
                new Item(2L, 33f),
                new Item(3L, 34f));

        Set<ConstraintViolation> violations = getConstraintViolations(objField, obj);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testFloatingPointPrecision() {
        Obj obj = new Obj();
        obj.items = ImmutableSet.of(
                new Item(null, 98f),
                new Item(null, 0.2f),
                new Item(null, 0.2f),
                new Item(null, 0.2f),
                new Item(null, 0.2f),
                new Item(null, 0.2f),
                new Item(null, 0.2f),
                new Item(null, 0.2f),
                new Item(null, 0.2f),
                new Item(null, 0.2f),
                new Item(null, 0.2f));

        Set<ConstraintViolation> violations = getConstraintViolations(objField, obj);

        assertThat(violations, emptyIterable());
    }

    private Matcher<ConstraintViolation> violation() {
        return ValidatorMatchers.violationFor(TotalPercentageValidator.class, "items", anything(),
                ValidationErrors.FIELD_PERCENTAGE_SUM_BAD);
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField dummyObjField, Object object) {
        Validator validator = new Validator();
        return validator.validate(dummyObjField, object, getDefaultTranslationContext());
    }
}
