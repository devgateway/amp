package org.digijava.kernel.validators.activity;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.validator.groups.Submit;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class OnBudgetValidatorTest {

    private static APIField activityField;
    private static HardcodedCategoryValues categoryValues;

    @BeforeClass
    public static void setUp() {
        activityField = ValidatorUtil.getMetaData();
        categoryValues = new HardcodedCategoryValues();
    }

    @Test
    public void testEmptyActivity() {
        AmpActivity activity = new AmpActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testOffBudget() {
        AmpActivity activity = new AmpActivity();
        activity.setCategories(ImmutableSet.of(categoryValues.getActivityBudgets().getOffBudget()));

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testOnBudgetNotRequiredOnDraftSave() {
        AmpActivity activity = new AmpActivity();
        activity.setCategories(ImmutableSet.of(categoryValues.getActivityBudgets().getOnBudget()));

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testOnBudgetAllMissingFYNotRequired() {
        AmpActivity activity = new AmpActivity();
        activity.setCategories(ImmutableSet.of(categoryValues.getActivityBudgets().getOnBudget()));

        APIField activityField = ValidatorUtil.getMetaData(ImmutableSet.of(
                "/Activity Form/Identification/Budget Extras/Required Validator for fy"));

        Validator validator = new Validator();
        Set<ConstraintViolation> violations = validator.validate(activityField, activity, Submit.class);

        assertThat(violations, containsInAnyOrder(ImmutableList.of(
                violation("vote"),
                violation("sub_vote"),
                violation("sub_program"),
                violation("project_code"),
                violation("ministry_code"))));
    }

    @Test
    public void testOnBudgetAllMissing() {
        AmpActivity activity = new AmpActivity();
        activity.setCategories(ImmutableSet.of(categoryValues.getActivityBudgets().getOnBudget()));

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, containsInAnyOrder(ImmutableList.of(
                violation("fy"),
                violation("vote"),
                violation("sub_vote"),
                violation("sub_program"),
                violation("project_code"),
                violation("ministry_code"))));
    }

    @Test
    public void testOnBudgetHalfMissing() {
        AmpActivity activity = new AmpActivity();
        activity.setCategories(ImmutableSet.of(categoryValues.getActivityBudgets().getOnBudget()));
        activity.setFiscalYears(ImmutableSet.of(2019L));
        activity.setVote("Yes");
        activity.setSubVote("Yes");

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, containsInAnyOrder(ImmutableList.of(
                violation("sub_program"),
                violation("project_code"),
                violation("ministry_code"))));
    }

    @Test
    public void testOnBudgetAllSet() {
        AmpActivity activity = new AmpActivity();
        activity.setCategories(ImmutableSet.of(categoryValues.getActivityBudgets().getOnBudget()));
        activity.setFiscalYears(ImmutableSet.of(2019L));
        activity.setVote("Yes");
        activity.setSubVote("Yes");
        activity.setSubProgram("Yes");
        activity.setProjectCode("Yes");
        activity.setMinistryCode("Yes");

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, emptyIterable());
    }

    private Matcher<ConstraintViolation> violation(String path) {
        return ValidatorMatchers.violationFor(OnBudgetValidator.class, path, anything(), ActivityErrors.FIELD_REQUIRED);
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpActivity activity, Class<?>... groups) {
        Validator validator = new Validator();
        return validator.validate(activityField, activity, groups);
    }
}