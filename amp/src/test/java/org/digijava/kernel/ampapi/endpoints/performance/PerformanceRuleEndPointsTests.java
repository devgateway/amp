package org.digijava.kernel.ampapi.endpoints.performance;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueLabel;
import org.junit.Test;

public class PerformanceRuleEndPointsTests {
    
    @Test
    public void testPerformanceRuleAlertSerialization() throws JsonProcessingException {
        CategoryValueLabel obj = new CategoryValueLabel(1L, "Critical", "Critical translated");
        
        ObjectMapper om = new ObjectMapper();
        String actualJson = om.writeValueAsString(obj);
        
        assertEquals("{\"id\":1,\"value\":\"Critical\",\"label\":\"Critical translated\"}", actualJson);
    }
}
