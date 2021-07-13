package org.digijava.kernel.validators.activity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.TestFieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.TranslationContext;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Set;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.digijava.kernel.validators.ValidatorUtil.getDefaultTranslationContext;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FeaturesUtil.class})
public class UniqueActivityTitleValidatorTest {

    private static APIField activityField;
    private static APIField activityFieldML;

    @Before
    public void setUp() {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        PowerMockito.mockStatic(FeaturesUtil.class);
        activityField = ValidatorUtil.getMetaData();
        activityFieldML = ValidatorUtil.getMetaData(AmpActivityFields.class, ImmutableSet.of(),
                new TestFieldInfoProvider(true));
    }

    @Test
    public void testNullTitle() {
        AmpActivityVersion activity = new AmpActivityVersion();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, false);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmptyTitle() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setName("");
        mockValidation();
        Set<ConstraintViolation> violations = getConstraintViolations(activity, false);

        assertThat(violations, emptyIterable());
    }

    private void mockValidation() {
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM)).thenReturn(null);
    }

    @Test
    public void testBlankTitle() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setName(" ");

        Set<ConstraintViolation> violations = getConstraintViolations(activity, false);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testTitleNotUnique() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setName("Dummy Activity");

        Set<ConstraintViolation> violations = getConstraintViolations(activity, false);

        assertThat(violations, contains(violation()));
    }

    @Test
    public void testTitleIsUnique() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setName("Dummy Activity");

        Set<ConstraintViolation> violations = getConstraintViolations(activity, true);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_multilingual_missingTitle() {
        AmpActivityVersion activity = new AmpActivityVersion();

        List<AmpContentTranslation> contentTranslations = ImmutableList.of();

        Set<ConstraintViolation> violations = getConstraintViolationsML(activity, contentTranslations, false);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_multilingual_blankAndEmptyTitle() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setAmpActivityId(35L);

        List<AmpContentTranslation> contentTranslations = ImmutableList.of(
                newContentTranslation("en", ""),
                newContentTranslation("fr", " "));

        Set<ConstraintViolation> violations = getConstraintViolationsML(activity, contentTranslations, false);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_multilingual_unique() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setAmpActivityId(35L);

        List<AmpContentTranslation> contentTranslations = ImmutableList.of(
                newContentTranslation("en", "English title"),
                newContentTranslation("fr", "French title"));

        Set<ConstraintViolation> violations = getConstraintViolationsML(activity, contentTranslations, true);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_multilingual_notUnique() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setAmpActivityId(35L);

        List<AmpContentTranslation> contentTranslations = ImmutableList.of(
                newContentTranslation("en", "English title"),
                newContentTranslation("fr", "French title"));

        Set<ConstraintViolation> violations = getConstraintViolationsML(activity, contentTranslations, false);

        assertThat(violations, contains(violation()));
    }

    private AmpContentTranslation newContentTranslation(String languageCode, String value) {
        AmpContentTranslation ct = new AmpContentTranslation();
        ct.setTranslation(value);
        ct.setLocale(languageCode);
        ct.setObjectClass(AmpActivityVersion.class.getName());
        ct.setFieldName("name");
        ct.setObjectId(35L);
        return ct;
    }

    private Matcher<ConstraintViolation> violation() {
        return ValidatorMatchers.violationFor(UniqueActivityTitleValidator.class, "project_title", anything(),
                ActivityErrors.UNIQUE_ACTIVITY_TITLE);
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpActivityVersion activity, boolean unique) {
        Set<ConstraintViolation> violations = ActivityValidatorUtil.validate(activityField, activity,
                getDefaultTranslationContext(), unique);
        return filter(violations, UniqueActivityTitleValidator.class);
    }

    private Set<ConstraintViolation> getConstraintViolationsML(AmpActivityVersion activity,
            List<AmpContentTranslation> contentTranslations, boolean unique) {
        TranslationContext translationContext = getDefaultTranslationContext(new EditorStore(), contentTranslations);

        Set<ConstraintViolation> violations =
                ActivityValidatorUtil.validate(activityFieldML, activity, translationContext, unique);

        return filter(violations, UniqueActivityTitleValidator.class);
    }
}