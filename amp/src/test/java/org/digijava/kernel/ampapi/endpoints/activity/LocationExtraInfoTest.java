package org.digijava.kernel.ampapi.endpoints.activity;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AmpMediaType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.ApiCompat;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class LocationExtraInfoTest {

    private LocationExtraInfo locationExtraInfo = new LocationExtraInfo(1L, "PL", 2L, "CV", null);

    private String originalJson = "{\"parent_location_id\":1,\"parent_location_name\":\"PL\","
            + "\"implementation_level_id\":2,\"implementation_location_name\":\"CV\"}";

    private String treeJson = "{\"implementation_level_id\":2,\"implementation_location_name\":\"CV\"}";

    @Test
    public void testSimple() throws Exception {
        assertJsonEquals(originalJson, locationExtraInfo);
    }

    @Test
    public void testWildcard() throws Exception {
        ApiCompat.withRequestedMediaType(MediaType.WILDCARD,
                () -> assertJsonEquals(originalJson, locationExtraInfo));
    }

    @Test
    public void testApplicationJson() throws Exception {
        ApiCompat.withRequestedMediaType(MediaType.APPLICATION_JSON,
                () -> assertJsonEquals(originalJson, locationExtraInfo));
    }

    @Test
    public void testPossibleValues2Json() throws Exception {
        ApiCompat.withRequestedMediaType(AmpMediaType.POSSIBLE_VALUES_V2_JSON,
                () -> assertJsonEquals(treeJson, locationExtraInfo));
    }

    private void assertJsonEquals(String expectedJson, LocationExtraInfo locationExtraInfo) {
        try {
            String actualJson = new ObjectMapper().writeValueAsString(locationExtraInfo);
            Assert.assertEquals(expectedJson, actualJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
