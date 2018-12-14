package org.digijava.kernel.ampapi.endpoints.dashboards;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.HeatMapConfigIndexed;
import org.digijava.module.aim.helper.HeatMapConfig;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class EndPointsTest {

    @Test
    public void testHeatMapConfigIndexedSerialization() throws JsonProcessingException {
        HeatMapConfigIndexed obj = new HeatMapConfigIndexed("abc", HeatMapConfig.Type.LOCATION,
                ImmutableList.of(1, 2),
                ImmutableList.of(2, 3));

        ObjectMapper om = new ObjectMapper();
        String actualJson = om.writeValueAsString(obj);

        assertEquals("{\"name\":\"abc\",\"type\":\"L\",\"xColumns\":[1,2],\"yColumns\":[2,3]}", actualJson);
    }
}