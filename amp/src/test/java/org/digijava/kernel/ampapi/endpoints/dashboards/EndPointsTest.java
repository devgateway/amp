package org.digijava.kernel.ampapi.endpoints.dashboards;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.digijava.module.aim.helper.HeatMapConfig;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class EndPointsTest {

    @Test
    public void testHeatMapConfigIndexedSerialization() throws JsonProcessingException {
        HeatMapConfig obj = new HeatMapConfig("abc", HeatMapConfig.Type.LOCATION,
                ImmutableList.of("Column 1", "Column 2"),
                ImmutableList.of("Column 2", "Column 3"));

        ObjectMapper om = new ObjectMapper();
        String actualJson = om.writeValueAsString(obj);

        assertEquals("{\"name\":\"abc\",\"type\":\"L\",\"xColumns\":[\"Column 1\",\"Column 2\"],\"yColumns\":[\"Column 2\",\"Column 3\"]}", actualJson);
    }
}