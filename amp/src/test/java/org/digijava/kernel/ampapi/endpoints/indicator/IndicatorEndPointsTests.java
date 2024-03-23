package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueLabel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndicatorEndPointsTests {
    
    @Test
    public void testAdmLevelSerialization() throws JsonProcessingException {
        CategoryValueLabel obj = new CategoryValueLabel(20L, "Region", "Region translated");
        
        ObjectMapper om = new ObjectMapper();
        String actualJson = om.writeValueAsString(obj);
        
        assertEquals("{\"id\":20,\"value\":\"Region\",\"label\":\"Region translated\"}", actualJson);
    }
}
