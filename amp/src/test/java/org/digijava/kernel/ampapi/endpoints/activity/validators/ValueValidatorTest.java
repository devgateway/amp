package org.digijava.kernel.ampapi.endpoints.activity.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.activity.values.PossibleValuesCache;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ValueValidatorTest {

    private static final String SECTOR_FIELD = "sector";
    private static final String FY_FIELD = "fy";

    private static final String ROOT_SECTOR_NAME = "root sector";
    private static final Long ROOT_SECTOR_ID = 1L;
    private static final String LEAF_SECTOR_NAME = "root sector";
    private static final Long LEAF_SECTOR_ID = 2L;
    private static final Long INVALID_SECTOR_ID = 999L;
    private static final Long VALID_FY_ID = 2000L;
    private static final Long INVALID_FY_ID = 2001L;

    private static final List<PossibleValue> SECTOR_POSSIBLE_VALUES = Arrays.asList(
            new PossibleValue(ROOT_SECTOR_ID, ROOT_SECTOR_NAME)
                    .withChildren(Arrays.asList(new PossibleValue(LEAF_SECTOR_ID, LEAF_SECTOR_NAME)))
    );

    private static final List<PossibleValue> FY_POSSIBLE_VALUES = Arrays.asList(
            new PossibleValue(VALID_FY_ID, VALID_FY_ID.toString())
    );

    private ActivityImporter importer;
    private APIField sectorFieldDescription;
    private APIField fyFieldDescription;
    private PossibleValuesCache possibleValuesCached;

    @Before
    public void setUp() throws Exception {
        importer = mock(ActivityImporter.class);
        possibleValuesCached = mock(PossibleValuesCache.class);
        when(importer.getPossibleValuesCache()).thenReturn(possibleValuesCached);
        when(possibleValuesCached.getPossibleValues(SECTOR_FIELD)).thenReturn(SECTOR_POSSIBLE_VALUES);
        when(possibleValuesCached.getPossibleValues(FY_FIELD)).thenReturn(FY_POSSIBLE_VALUES);

        sectorFieldDescription = new APIField();
        sectorFieldDescription.setFieldName(SECTOR_FIELD);
        sectorFieldDescription.setImportable(true);
        sectorFieldDescription.setIdOnly(true);
        sectorFieldDescription.setApiType(new APIType(String.class));

        fyFieldDescription = new APIField();
        fyFieldDescription.setFieldName(FY_FIELD);
        fyFieldDescription.setImportable(true);
        fyFieldDescription.setIdOnly(true);
        fyFieldDescription.setApiType(new APIType(Collection.class, Long.class));
    }

    @Test
    public void testPossibleValueNonRootSector() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(SECTOR_FIELD, LEAF_SECTOR_ID.intValue());

        ValueValidator valueValidator = new ValueValidator();

        assertTrue("Sector must be valid",
                valueValidator.isValid(importer, newFieldParent, sectorFieldDescription, SECTOR_FIELD));
    }

    @Test
    public void testPossibleValueInvalidSector() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(SECTOR_FIELD, INVALID_SECTOR_ID.intValue());

        ValueValidator valueValidator = new ValueValidator();

        assertFalse("Sector must be invalid",
                valueValidator.isValid(importer, newFieldParent, sectorFieldDescription, SECTOR_FIELD));
        assertEquals(ActivityErrors.FIELD_INVALID_VALUE, valueValidator.getErrorMessage());
    }

    @Test
    public void testPossibleValueFYValid() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(FY_FIELD, Arrays.asList(VALID_FY_ID));

        ValueValidator valueValidator = new ValueValidator();

        assertTrue("FY must be valid",
                valueValidator.isValid(importer, newFieldParent, fyFieldDescription, FY_FIELD));
    }

    @Test
    public void testPossibleValueFYInvalid() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(FY_FIELD, Arrays.asList(INVALID_FY_ID));

        ValueValidator valueValidator = new ValueValidator();

        assertFalse("FY must be invalid",
                valueValidator.isValid(importer, newFieldParent, fyFieldDescription, FY_FIELD));
        assertEquals(ActivityErrors.FIELD_INVALID_VALUE, valueValidator.getErrorMessage());
    }
}
