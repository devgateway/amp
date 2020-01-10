package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.ampapi.filters.ClientMode;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityImporterTest {

    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();

    @Test
    public void testValidationReportUnknownField() throws Exception {
        Map<String, Object> json = new HashMap<>();
        json.put("foo", "bar");

        Collection<ApiErrorMessage> actualErrors = new ArrayList<>(validateAndRetrieveImporter(json).getWarnings());
        Collection<ApiErrorMessage> expectedErrors = Arrays.asList(ValidationErrors.FIELD_INVALID.withDetails("foo"));

        assertThat(actualErrors, is(expectedErrors));
    }

    @Test
    public void testValidationIgnoreUnknownFieldInAmpOffline() throws Exception {
        try {
            AmpClientModeHolder.setClientMode(ClientMode.AMP_OFFLINE);

            Map<String, Object> json = new HashMap<>();
            json.put("foo", "bar");

            Map<Integer, ApiErrorMessage> actualErrors = validate(json);

            assertThat(actualErrors, is(emptyMap()));
        } finally {
            AmpClientModeHolder.setClientMode(null);
        }
    }

    private Map<Integer, ApiErrorMessage> validate(Map<String, Object> json) {
        return validateAndRetrieveImporter(json).getErrors();
    }

    private ActivityImporter validateAndRetrieveImporter(Map<String, Object> json) {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setApprovalStatus(ApprovalStatus.STARTED);
        APIField activityField = new APIField();
        ActivityImporter importer = new ActivityImporter(activityField, new ActivityImportRules(true, false,
                false));
        importer.validateAndImport(activity, json, true);
        return importer;
    }

    private Map<Integer, ApiErrorMessage> errors(ApiErrorMessage... messages) {
        Map<Integer, ApiErrorMessage> errors = new HashMap<>();
        for (ApiErrorMessage message : messages) {
            errors.put(message.id, message);
        }
        return errors;
    }
}
