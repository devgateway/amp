package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.getDefaultTranslationContext;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.util.Identifiable;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class UniqueValidatorTest {

    private static APIField dummyIntField;
    private static APIField dummyObjField;

    public static class DummyInt {

        @Interchangeable(
                fieldTitle = "list_of_ints",
                validators = @Validators(unique = "/IntegersUniqueValidator"),
                uniqueConstraint = true)
        private List<Integer> listOfInts;
    }

    public static class DummyObj {

        @Interchangeable(
                fieldTitle = "list_of_objs",
                validators = @Validators(unique = "/ObjectsUniqueValidator"))
        private Set<SubDummyObj> listOfObjs;
    }

    public static class SubDummyObj {

        @InterchangeableId
        @Interchangeable(fieldTitle = "id")
        private Long id;

        @Interchangeable(fieldTitle = "unq_property", uniqueConstraint = true)
        private UniqueProperty uniqueProperty;
    }

    public static class UniqueProperty implements Identifiable {

        private Long id;

        @Override
        public Object getIdentifier() {
            return id;
        }
    }

    @BeforeClass
    public static void setUp() {
        dummyIntField = ValidatorUtil.getMetaData(DummyInt.class);
        dummyObjField = ValidatorUtil.getMetaData(DummyObj.class);
    }

    @Test
    public void testIntsUnique() {
        DummyInt dummy = new DummyInt();
        dummy.listOfInts = ImmutableList.of(1, 2, 3);

        Set<ConstraintViolation> violations = getConstraintViolations(dummyIntField, dummy);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testIntsRepeating() {
        DummyInt dummy = new DummyInt();
        dummy.listOfInts = ImmutableList.of(1, 2, 3, 2);

        Set<ConstraintViolation> violations = getConstraintViolations(dummyIntField, dummy);

        assertThat(violations, contains(violation("list_of_ints")));
    }

    @Test
    public void testIntsEmpty() {
        DummyInt dummy = new DummyInt();
        dummy.listOfInts = ImmutableList.of();

        Set<ConstraintViolation> violations = getConstraintViolations(dummyIntField, dummy);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testIntsNull() {
        DummyInt dummy = new DummyInt();

        Set<ConstraintViolation> violations = getConstraintViolations(dummyIntField, dummy);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testObjsNull() {

        DummyObj dummy = new DummyObj();

        Set<ConstraintViolation> violations = getConstraintViolations(dummyObjField, dummy);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testObjsEmpty() {

        DummyObj dummy = new DummyObj();
        dummy.listOfObjs = ImmutableSet.of();

        Set<ConstraintViolation> violations = getConstraintViolations(dummyObjField, dummy);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testObjsUnique() {
        UniqueProperty uqProp1 = new UniqueProperty();
        uqProp1.id = 1L;

        UniqueProperty uqProp2 = new UniqueProperty();
        uqProp2.id = 2L;

        SubDummyObj subDummyObj1 = new SubDummyObj();
        subDummyObj1.uniqueProperty = uqProp1;

        SubDummyObj subDummyObj2 = new SubDummyObj();
        subDummyObj2.uniqueProperty = uqProp2;

        DummyObj dummy = new DummyObj();
        dummy.listOfObjs = ImmutableSet.of(subDummyObj1, subDummyObj2);

        Set<ConstraintViolation> violations = getConstraintViolations(dummyObjField, dummy);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testObjsRepeated() {
        UniqueProperty uqProp1 = new UniqueProperty();
        uqProp1.id = 1L;

        SubDummyObj subDummyObj1 = new SubDummyObj();
        subDummyObj1.uniqueProperty = uqProp1;

        SubDummyObj subDummyObj2 = new SubDummyObj();
        subDummyObj2.uniqueProperty = uqProp1;

        DummyObj dummy = new DummyObj();
        dummy.listOfObjs = ImmutableSet.of(subDummyObj1, subDummyObj2);

        Set<ConstraintViolation> violations = getConstraintViolations(dummyObjField, dummy);

        assertThat(violations, contains(violation("list_of_objs")));
    }

    @Test
    public void testValidatorIsDisabled() {
        DummyInt dummy = new DummyInt();
        dummy.listOfInts = ImmutableList.of(1, 2, 3, 2);

        APIField dummyIntField = ValidatorUtil.getMetaData(DummyInt.class, ImmutableSet.of("/IntegersUniqueValidator"));
        Set<ConstraintViolation> violations = getConstraintViolations(dummyIntField, dummy);

        assertThat(violations, emptyIterable());
    }

    private Matcher<ConstraintViolation> violation(String path) {
        return ValidatorMatchers.violationFor(UniqueValidator.class, path, anything(),
                ValidationErrors.FIELD_UNQUE_VALUES);
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField dummyObjField, Object object) {
        Validator validator = new Validator();
        return validator.validate(dummyObjField, object, getDefaultTranslationContext());
    }
}