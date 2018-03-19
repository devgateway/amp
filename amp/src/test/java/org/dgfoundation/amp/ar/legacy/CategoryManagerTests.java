package org.dgfoundation.amp.ar.legacy;


import static org.digijava.module.categorymanager.util.CategoryConstants.*;

import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.junit.Test;

public class CategoryManagerTests extends ReportsTestCase {

    @Test
    public void testHardCodedCategoryValueBasics()
    {
        assertTrue(ADJUSTMENT_TYPE_ACTUAL.existsInDatabase());
        assertEquals(Long.valueOf(272l), ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase());
        AmpCategoryValue acv = ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB();
        assertEquals(acv.getValue(), ADJUSTMENT_TYPE_ACTUAL.getValueKey());
        assertEquals(acv.getAmpCategoryClass().getKeyName(), ADJUSTMENT_TYPE_ACTUAL.getCategoryKey());
        assertTrue(ADJUSTMENT_TYPE_ACTUAL.equalsCategoryValue(acv));
        assertFalse(ADJUSTMENT_TYPE_ACTUAL.equalsCategoryValue(ADJUSTMENT_TYPE_PLANNED.getAmpCategoryValueFromDB()));
    }

    @Test
    public void testHardCodedCategoryValueErrors()
    {
        HardCodedCategoryValue nonExistingValueHcv = new HardCodedCategoryValue("adjustment_type", "DUMMY_VALUE", false);
        HardCodedCategoryValue nonExistingCategoryHcv = new HardCodedCategoryValue("non_existing_category", "Actual", false);
        
        assertFalse(nonExistingValueHcv.existsInDatabase());
        assertFalse(nonExistingCategoryHcv.existsInDatabase());
        
        assertNull(nonExistingValueHcv.getIdInDatabase());
        assertNull(nonExistingCategoryHcv.getIdInDatabase());

        assertNull(nonExistingValueHcv.getAmpCategoryValueFromDB());
        assertNull(nonExistingCategoryHcv.getAmpCategoryValueFromDB());
        
        assertFalse(nonExistingValueHcv.equalsCategoryValue(ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB()));
        assertFalse(nonExistingCategoryHcv.equalsCategoryValue(ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB()));
        
        assertFalse(nonExistingValueHcv.equalsCategoryValue(IMPLEMENTATION_LEVEL_NATIONAL.getAmpCategoryValueFromDB()));
        assertFalse(nonExistingCategoryHcv.equalsCategoryValue(IMPLEMENTATION_LEVEL_NATIONAL.getAmpCategoryValueFromDB()));
        
        assertFalse(nonExistingValueHcv.equalsCategoryValue(null));
        assertFalse(nonExistingCategoryHcv.equalsCategoryValue(null));
    }
}
