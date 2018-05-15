package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityImporterTest {

    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();

    private List<APIField> apiFields = Arrays.asList();

    @Test
    public void testValidationReportUnknownField() throws Exception {
        JsonBean json = new JsonBean();
        json.set("foo", "bar");

        Map<Integer, ApiErrorMessage> actualErrors = validate(json);
        Map<Integer, ApiErrorMessage> expectedErrors = errors(ActivityErrors.FIELD_INVALID.withDetails("foo"));

        assertThat(actualErrors, is(expectedErrors));
    }

    @Test
    public void testValidationIgnoreUnknownFieldInAmpOffline() throws Exception {
        try {
            AmpOfflineModeHolder.setAmpOfflineMode(true);

            JsonBean json = new JsonBean();
            json.set("foo", "bar");

            Map<Integer, ApiErrorMessage> actualErrors = validate(json);

            assertThat(actualErrors, is(emptyMap()));
        } finally {
            AmpOfflineModeHolder.setAmpOfflineMode(false);
        }
    }

    private Map<Integer, ApiErrorMessage> validate(JsonBean json) {
        AmpActivityVersion activity = new AmpActivityVersion();
        ActivityImporter importer = new ActivityImporter();
        importer.validateAndImport(activity, null, apiFields, json.any(), null, null );
        return importer.getErrors();
    }

    private Map<Integer, ApiErrorMessage> errors(ApiErrorMessage... messages) {
        Map<Integer, ApiErrorMessage> errors = new HashMap<>();
        for (ApiErrorMessage message : messages) {
            errors.put(message.id, message);
        }
        return errors;
    }
}
