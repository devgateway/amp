package org.digijava.kernel.validators.activity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.persistence.InMemoryCategoryValuesManager;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.validator.groups.Submit;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FeaturesUtil.class})
public class OnBudgetValidatorTest {

    private static APIField activityField;
    private static InMemoryCategoryValuesManager categoryValues;

    @Before
    public void setUp() {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        PowerMockito.mockStatic(FeaturesUtil.class);
        activityField = ValidatorUtil.getMetaData();
        categoryValues = InMemoryCategoryValuesManager.getInstance();
    }

    @Test
    public void testEmptyActivity() {
        AmpActivityVersion activity = new ActivityBuilder().getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testOffBudget() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(categoryValues.getActivityBudgets().getOffBudget())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testOnBudgetNotRequiredOnDraftSave() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(categoryValues.getActivityBudgets().getOnBudget())
                .getActivity();

        mockValidation();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    private void mockValidation() {
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM)).thenReturn(null);
    }

    @Test
    public void testOnBudgetAllMissingFYNotRequired() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(categoryValues.getActivityBudgets().getOnBudget())
                .getActivity();

        APIField activityField = ValidatorUtil.getMetaData(ImmutableSet.of(
                "/Activity Form/Identification/Budget Extras/Required Validator for fy"));

        Set<ConstraintViolation> violations = getConstraintViolations(activityField, activity, Submit.class);

        assertThat(violations, containsInAnyOrder(ImmutableList.of(
                violation("vote"),
                violation("sub_vote"),
                violation("sub_program"),
                violation("project_code"),
                violation("ministry_code"))));
    }

    @Test
    public void testOnBudgetAllMissing() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(categoryValues.getActivityBudgets().getOnBudget())
                .getActivity();

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
        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(categoryValues.getActivityBudgets().getOnBudget())
                .getActivity();

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
        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(categoryValues.getActivityBudgets().getOnBudget())
                .getActivity();

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
        return ValidatorMatchers.violationFor(OnBudgetValidator.class, path, anything(),
                ValidationErrors.FIELD_REQUIRED);
    }

    private Set<ConstraintViolation> getConstraintViolations(Object object, Class<?>... groups) {
        return getConstraintViolations(activityField, object, groups);
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField type, Object object, Class<?>... groups) {
        Set<ConstraintViolation> violations = ActivityValidatorUtil.validate(type, object, groups);
        return filter(violations, OnBudgetValidator.class);
    }
}