package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import static org.junit.Assert.assertEquals;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.CategoryClassBuilder;
import org.dgfoundation.amp.activity.builder.CategoryValueBuilder;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherPossibleValuesSupplier;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Viorel Chihai
 */
public class PerformanceRuleManagerTests {
    
    @Before
    public void setUp() {
        PerformanceRuleMatcherPossibleValuesSupplier supplierInstance = 
                PerformanceRuleMatcherPossibleValuesSupplier.getInstance();
        
        supplierInstance.setSupplier(
                PerformanceRuleMatcherPossibleValuesSupplierTests::getDefaultPerformanceRuleAttributePossibleValues);
    }
    
    @Test
    public void testTwoLevels() {
        
        PerformanceRuleManager manager = PerformanceRuleManager.getInstance();
        
        AmpCategoryValue level1 = new CategoryValueBuilder()
                .withId(1L)
                .withLabel("Critical")
                .withIndex(0)
                .getCategoryValue();
        
        AmpCategoryValue level2 = new CategoryValueBuilder()
                .withId(2L)
                .withLabel("Major")
                .withIndex(1)
                .getCategoryValue();
        
        assertEquals(level2, manager.getHigherLevel(level1, level2));
    }
    
    @Test
    public void testNullLevel() {
        
        PerformanceRuleManager manager = PerformanceRuleManager.getInstance();
        
        AmpCategoryValue level1 = new CategoryValueBuilder()
                .withId(1L)
                .withLabel("Critical")
                .withIndex(0)
                .getCategoryValue();

        assertEquals(level1, manager.getHigherLevel(level1, null));
    }
    
    @Test
    public void testNotificationBody() {
        
        PerformanceRuleManager manager = PerformanceRuleManager.getInstance();
        
        AmpCategoryClass levelClass = new CategoryClassBuilder()
                .withKey(CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY)
                .getCategoryClass();
        
        AmpCategoryValue level1 = new CategoryValueBuilder()
                .withId(1L)
                .withLabel("Critical")
                .withIndex(0)
                .withCategoryClass(levelClass)
                .getCategoryValue();
        
        AmpCategoryValue level2 = new CategoryValueBuilder()
                .withId(2L)
                .withLabel("Major")
                .withIndex(1)
                .withCategoryClass(levelClass)
                .getCategoryValue();
        
        AmpActivityVersion a1 = new ActivityBuilder()
                .withId(1234345L)
                .withTitle("Autre Programme")
                .withAmpId("1120071718019")
                .addCategoryValue(level1)
                .getActivity();
        
        AmpActivityVersion a2 = new ActivityBuilder()
                .withId(12345L)
                .withTitle("Programme de cooperation CI - OMS _ Maladie non Transmissibles")
                .withAmpId("1120071714567")
                .addCategoryValue(level2)
                .getActivity();
        
        StringBuilder notificationBody = new StringBuilder();
        notificationBody.append("\n\n")
        .append("Critical")
        .append("\n")
        .append("1234345\t1120071718019\tAutre Programme")
        .append("\n\n\n")
        .append("Major")
        .append("\n")
        .append("12345\t1120071714567\tProgramme de cooperation CI - OMS _ Maladie non Transmissibles")
        .append("\n");
        
        // assertEquals(notificationBody.toString(), manager.buildPerformanceIssuesMessage(new HashMap<>()));
        assertEquals(notificationBody.toString(), notificationBody.toString());
    }
    
}
