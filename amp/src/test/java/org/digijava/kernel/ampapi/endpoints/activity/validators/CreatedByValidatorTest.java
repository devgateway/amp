package org.digijava.kernel.ampapi.endpoints.activity.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Nadejda Mandrescu
 */
public class CreatedByValidatorTest {

    private static final String CREATED_BY_FIELD = FieldMap.underscorify(ActivityFieldsConstants.CREATED_BY);

    private static final Long TM1 = 1l;
    private static final Long TM2 = 2l;

    private ActivityImporter importer;
    private ActivityImportRules importRules;
    private APIField createdByFieldDesc;
    private AmpActivityVersion oldActivity;
    private AmpTeamMember ampTeamMember;

    @Before
    public void setUp() throws Exception {
        importer = mock(ActivityImporter.class);
        importRules = mock(ActivityImportRules.class);
        oldActivity = mock(AmpActivityVersion.class);
        ampTeamMember = mock(AmpTeamMember.class);
        when(importer.getImportRules()).thenReturn(importRules);
        when(oldActivity.getActivityCreator()).thenReturn(ampTeamMember);

        createdByFieldDesc = new APIField();
        createdByFieldDesc.setFieldName(CREATED_BY_FIELD);
        createdByFieldDesc.setImportable(true);
        createdByFieldDesc.setApiType(new APIType(Long.class));
    }

    @Test
    public void testNewActivityNoCreatorNoTrackEditor() {
        Map<String, Object> activity = configure(null, false, null, null);
        CreatedByValidator validator = new CreatedByValidator();

        assertTrue("Missing created_by must be ignored",
                validator.isValid(importer, activity, createdByFieldDesc, CREATED_BY_FIELD));
    }

    @Test
    public void testNewActivityNoCreatorTrackEditor() {
        Map<String, Object> activity = configure(null, true, null, null);
        CreatedByValidator validator = new CreatedByValidator();

        assertFalse("Missing created_by must be reported",
                validator.isValid(importer, activity, createdByFieldDesc, CREATED_BY_FIELD));
    }

    @Test
    public void testNewActivityCreatorTrackEditor() {
        Map<String, Object> activity = configure(null, true, TM1, null);
        CreatedByValidator validator = new CreatedByValidator();

        assertTrue("created_by must be valid",
                validator.isValid(importer, activity, createdByFieldDesc, CREATED_BY_FIELD));
    }

    @Test
    public void testExistingActivityNoCreatorNoTrackEditor() {
        Map<String, Object> activity = configure(oldActivity, false, null, TM2);
        CreatedByValidator validator = new CreatedByValidator();

        assertTrue("Missing created_by must be ignored",
                validator.isValid(importer, activity, createdByFieldDesc, CREATED_BY_FIELD));
    }

    @Test
    public void testExistingActivityNoCreatorTrackEditor() {
        Map<String, Object> activity = configure(oldActivity, true, null, TM2);
        CreatedByValidator validator = new CreatedByValidator();

        assertTrue("Missing created_by must be ignored",
                validator.isValid(importer, activity, createdByFieldDesc, CREATED_BY_FIELD));
    }

    @Test
    public void testExistingActivityMatchingCreatorTrackEditor() {
        Map<String, Object> activity = configure(oldActivity, true, TM2, TM2);
        CreatedByValidator validator = new CreatedByValidator();

        assertTrue("created_by must be valid",
                validator.isValid(importer, activity, createdByFieldDesc, CREATED_BY_FIELD));
    }

    @Test
    public void testExistingActivityNotMatchingCreatorTrackEditor() {
        Map<String, Object> activity = configure(oldActivity, true, TM1, TM2);
        CreatedByValidator validator = new CreatedByValidator();

        assertFalse("created_by must be invalid",
                validator.isValid(importer, activity, createdByFieldDesc, CREATED_BY_FIELD));
    }

    private Map<String, Object> configure(AmpActivityVersion oldActivity, boolean isTrackEditors,
            Long newCreatedBy, Long oldCreatedBy) {
        when(importer.getOldActivity()).thenReturn(oldActivity);
        when(importRules.isTrackEditors()).thenReturn(isTrackEditors);
        if (oldActivity != null) {
            when(ampTeamMember.getAmpTeamMemId()).thenReturn(oldCreatedBy);
        }

        Map<String, Object> json = new HashMap<>();
        if (newCreatedBy != null) {
            json.put(CREATED_BY_FIELD, newCreatedBy);
        }
        return json;
    }

}
