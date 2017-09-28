package org.digijava.kernel.ampapi.endpoints.activity.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ValueValidatorTest {

    private static final String SECTOR_FIELD = "sector";

    private static final String ROOT_SECTOR_NAME = "root sector";
    private static final Long ROOT_SECTOR_ID = 1L;
    private static final String LEAF_SECTOR_NAME = "root sector";
    private static final Long LEAF_SECTOR_ID = 2L;
    private static final Long INVALID_SECTOR_ID = 999L;

    private static final List<PossibleValue> SECTOR_POSSIBLE_VALUES = Arrays.asList(
            new PossibleValue(ROOT_SECTOR_ID, ROOT_SECTOR_NAME)
                    .withChildren(Arrays.asList(new PossibleValue(LEAF_SECTOR_ID, LEAF_SECTOR_NAME)))
    );

    private ActivityImporter importer;
    private APIField sectorFieldDescription;

    @Before
    public void setUp() throws Exception {
        importer = mock(ActivityImporter.class);
        when(importer.getPossibleValuesForFieldCached(any())).thenReturn(SECTOR_POSSIBLE_VALUES);

        sectorFieldDescription = new APIField();
        sectorFieldDescription.setFieldName(SECTOR_FIELD);
        sectorFieldDescription.setImportable(true);
        sectorFieldDescription.setIdOnly(true);
    }

    @Test
    public void testPossibleValueNonRootSector() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(SECTOR_FIELD, LEAF_SECTOR_ID.intValue());

        ValueValidator valueValidator = new ValueValidator();

        assertTrue("Sector must be valid",
                valueValidator.isValid(importer, newFieldParent, null, sectorFieldDescription, SECTOR_FIELD));
    }

    @Test
    public void testPossibleValueInvalidSector() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(SECTOR_FIELD, INVALID_SECTOR_ID.intValue());

        ValueValidator valueValidator = new ValueValidator();

        assertFalse("Sector must be invalid",
                valueValidator.isValid(importer, newFieldParent, null, sectorFieldDescription, SECTOR_FIELD));
        assertEquals(ActivityErrors.FIELD_INVALID_VALUE, valueValidator.getErrorMessage());
    }
}
