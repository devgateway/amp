package org.digijava.kernel.ampapi.endpoints.activity.validators;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceService;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Viorel Chihai
 */
public class UUIDValidatorTest {

    private static final String VALID_UUID = "8db8dfa6-36d8-4e18-8ada-11207926e023";
    private static final String INVALID_UUID = "8db8dfa6-36d8-4e18-8ada-11207926e024";

    private ResourceService resourceService;
    private ActivityImporter importer;
    private APIField uuidFieldDescription;

    private List<String> validPrivateUUIDs = ImmutableList.of(VALID_UUID);

    @Before
    public void setUp() {
        importer = mock(ActivityImporter.class);
        resourceService = mock(ResourceService.class);

        when(importer.getResourceService()).thenReturn(resourceService);
        when(resourceService.getPrivateUuids()).thenReturn(validPrivateUUIDs);

        uuidFieldDescription = new APIField();
        uuidFieldDescription.setFieldName(FieldMap.underscorify(ActivityFieldsConstants.UUID));
        uuidFieldDescription.setImportable(true);
    }

    @Test
    public void testValidUUID() {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(FieldMap.underscorify(ActivityFieldsConstants.UUID), VALID_UUID);

        UUIDValidator uuidValidator = new UUIDValidator();

        assertTrue("Value is valid",
                uuidValidator.isValid(importer, newFieldParent, uuidFieldDescription, ActivityFieldsConstants.UUID));
    }

    @Test
    public void testInvalidUUID() {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(FieldMap.underscorify(ActivityFieldsConstants.UUID), INVALID_UUID);

        UUIDValidator uuidValidator = new UUIDValidator();

        assertFalse("Value is invalid",
                uuidValidator.isValid(importer, newFieldParent, uuidFieldDescription, ActivityFieldsConstants.UUID));
    }

}
