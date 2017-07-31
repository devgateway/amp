package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import org.dgfoundation.amp.activity.builder.CategoryValueBuilder;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherPossibleValuesSupplier;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.junit.Before;

/**
 * @author Viorel Chihai
 */
public class PerformanceRuleMatcherTest {
    
    PerformanceRuleMatcherDefinition definition;
    
    @Before
    public void setUp() {
        PerformanceRuleMatcherPossibleValuesSupplier supplierInstance = 
                PerformanceRuleMatcherPossibleValuesSupplier.getInstance();
        
        supplierInstance.setSupplier(
                PerformanceRuleMatcherPossibleValuesSupplierTest::getDefaultPerformanceRuleAttributePossibleValues);
    }

    protected AmpCategoryValue getCriticalLevel() {
        return new CategoryValueBuilder().withLabel("Critical").withIndex(0).getCategoryValue();
    }

    protected AmpCategoryValue getMajorLevel() {
        return new CategoryValueBuilder().withLabel("Major").withIndex(1).getCategoryValue();
    }

    protected AmpCategoryValue getMinorLevel() {
        return new CategoryValueBuilder().withLabel("Minor").withIndex(2).getCategoryValue();
    }

}
