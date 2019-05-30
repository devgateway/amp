package org.digijava.kernel.ampapi.endpoints.activity.validators;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_NOT_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.DRAFT;
import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.SUBMIT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Octavian Ciubotaru
 */
public class RequiredValidatorTest {

    private static final String SAMPLE_FIELD = "field";
    private static final String SAMPLE_VALUE = "value";
    private static final String EMPTY_VALUE = "";

    private static final Map<String, Object> EMPTY_BEAN = ImmutableMap.of();
    private static final Map<String, Object> SAMPLE_BEAN = ImmutableMap.of(SAMPLE_FIELD, SAMPLE_VALUE);
    private static final Map<String, Object> LIST_BEAN = ImmutableMap.of(SAMPLE_FIELD, Arrays.asList(SAMPLE_VALUE));
    private static final Map<String, Object> EMPTY_VALUE_BEAN = ImmutableMap.of(SAMPLE_FIELD, EMPTY_VALUE);
    private static final Map<String, Object> EMPTY_LIST_BEAN = ImmutableMap.of(SAMPLE_FIELD, Collections.EMPTY_LIST);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ActivityImporter importer = mock(ActivityImporter.class);
    @Mock
    private ActivityImportRules importRules = mock(ActivityImportRules.class);

    @Before
    public void setUp() throws Exception {
        when(importer.isDraftFMEnabled()).thenReturn(true); // by default save as draft is allowed in fm
        when(importer.getImportRules()).thenReturn(importRules);
    }

    @Test
    public void testNotRequiredFieldNotPresent() throws Exception {
        assertValidator(EMPTY_BEAN, fd(FIELD_NOT_REQUIRED), null, false);
    }

    @Test
    public void testNotRequiredCollectionNotPresent() throws Exception {
        assertValidator(EMPTY_BEAN, fd(FIELD_NOT_REQUIRED), null, false);
    }

    @Test
    public void testNotRequiredCollectionEmpty() throws Exception {
        assertValidator(EMPTY_LIST_BEAN, fd(FIELD_NOT_REQUIRED), null, false);
    }

    @Test
    public void testNotImportableFieldNotPresent() throws Exception {
        APIField fd = fd(FIELD_NOT_REQUIRED);
        fd.setImportable(false);

        assertValidator(EMPTY_BEAN, fd, null, false);
    }

    @Test
    public void testAlwaysRequiredFieldPresent() throws Exception {
        assertValidator(SAMPLE_BEAN, fd(FIELD_ALWAYS_REQUIRED), null, false);
    }

    @Test
    public void testAlwaysRequiredCollectionPresent() throws Exception {
        assertValidator(LIST_BEAN, fdList(FIELD_ALWAYS_REQUIRED), null, false);
    }

    @Test
    public void testAlwaysRequiredFieldNotPresent() throws Exception {
        assertValidator(EMPTY_BEAN, fd(FIELD_ALWAYS_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testAlwaysRequiredCollectionNotPresent() throws Exception {
        assertValidator(EMPTY_BEAN, fdList(FIELD_ALWAYS_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testAlwaysRequiredFieldEmptyValueNotPresent() throws Exception {
        assertValidator(EMPTY_VALUE_BEAN, fd(FIELD_ALWAYS_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testAlwaysRequiredCollctionEmptyValueNotPresent() throws Exception {
        assertValidator(EMPTY_LIST_BEAN, fdList(FIELD_ALWAYS_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testSubmissionRequiredFieldPresent() throws Exception {
        assertValidator(SAMPLE_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), null, false);
    }

    @Test
    public void testSubmissionRequiredFieldMissingAndSaveAsDraftDisabled() throws Exception {
        when(importer.isDraftFMEnabled()).thenReturn(false);
        when(importRules.isCanDowngradeToDraft()).thenReturn(true);

        assertValidator(EMPTY_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), ActivityErrors.SAVE_AS_DRAFT_FM_DISABLED, false);
    }

    @Test
    public void testSubmissionRequiredFieldMissingAndSaveAsDraftEnabledDowngradeAllowed() throws Exception {
        when(importRules.isCanDowngradeToDraft()).thenReturn(true);
        assertValidator(EMPTY_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), null, true);
    }

    @Test
    public void testSubmissionRequiredFieldMissingAndSaveAsDraftEnabledDowngradeNotAllowed() throws Exception {
        when(importRules.isCanDowngradeToDraft()).thenReturn(false);
        assertValidator(EMPTY_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testDraftModeAlwaysRequiredFieldNotPresent() throws Exception {
        when(importer.getRequestedSaveMode()).thenReturn(DRAFT);

        assertValidator(EMPTY_BEAN, fd(FIELD_ALWAYS_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testSubmitModeAlwaysRequiredFieldNotPresent() throws Exception {
        when(importer.getRequestedSaveMode()).thenReturn(SUBMIT);

        assertValidator(EMPTY_BEAN, fd(FIELD_ALWAYS_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testSubmitModeAlwaysRequiredFieldEmptyValueNotPresent() throws Exception {
        when(importer.getRequestedSaveMode()).thenReturn(SUBMIT);

        assertValidator(EMPTY_VALUE_BEAN, fd(FIELD_ALWAYS_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testDraftModeSubmissionRequiredFieldNotPresent() throws Exception {
        when(importer.getRequestedSaveMode()).thenReturn(DRAFT);

        assertValidator(EMPTY_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), null, false);
    }

    @Test
    public void testSubmitModeSubmissionRequiredFieldNotPresent() throws Exception {
        when(importer.getRequestedSaveMode()).thenReturn(SUBMIT);

        assertValidator(EMPTY_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testSubmitModeSubmissionRequiredFieldPresentEmptyValue() throws Exception {
        when(importer.getRequestedSaveMode()).thenReturn(SUBMIT);

        assertValidator(EMPTY_VALUE_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testSubmitModeSubmissionRequiredFieldNotPresentSaveAsDraftDisabled() throws Exception {
        when(importer.getRequestedSaveMode()).thenReturn(SUBMIT);
        when(importer.isDraftFMEnabled()).thenReturn(false);

        assertValidator(EMPTY_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    @Test
    public void testSubmitModeSubmissionRequiredFieldEmptyValueNotPresentSaveAsDraftDisabled() throws Exception {
        when(importer.getRequestedSaveMode()).thenReturn(SUBMIT);
        when(importer.isDraftFMEnabled()).thenReturn(false);

        assertValidator(EMPTY_VALUE_BEAN, fd(FIELD_NON_DRAFT_REQUIRED), ActivityErrors.FIELD_REQUIRED, false);
    }

    private APIField fd(String required) {
        APIField fd = new APIField();
        fd.setFieldName(SAMPLE_FIELD);
        fd.setImportable(true);
        fd.setUnconditionalRequired(required);
        fd.setApiType(new APIType(String.class));
        return fd;
    }

    private APIField fdList(String required) {
        APIField fd = fd(required);
        fd.setApiType(new APIType(String.class, FieldType.LIST));
        return fd;
    }

    private void assertValidator(Map<String, Object> bean, APIField fieldDescription,
            ApiErrorMessage expectedErrorMessage, boolean downgradeExpected) {

        RequiredValidator validator = new RequiredValidator();

        boolean valid = validator.isValid(importer, bean, fieldDescription, SAMPLE_FIELD);

        assertEquals(expectedErrorMessage == null, valid);
        if (expectedErrorMessage != null) {
            assertEquals(expectedErrorMessage, validator.getErrorMessage());
        }
        if (downgradeExpected) {
            verify(importer).downgradeToDraftSave();
        } else {
            verify(importer, never()).downgradeToDraftSave();
        }
    }
}
