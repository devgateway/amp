package org.digijava.kernel.ampapi.endpoints.publicportal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author Octavian Ciubotaru
 */
public class PublicEndpointTest {

    @Test
    public void testTopDataSerialization() throws JsonProcessingException {
        PublicTopData c = new PublicTopData(
                ImmutableMap.of("donor-agency", "Donor Agency", "actual-disbursements", "Actual Disbursements"),
                ImmutableMap.of("Total Actual Disbursements", new BigDecimal("533325163.92")),
                ImmutableList.of(
                        ImmutableMap.of("donor-agency", "Donor 1", "actual-disbursements", "533 202 040,49"),
                        ImmutableMap.of("donor-agency", "Donor 2", "actual-disbursements", "123 123,43")
                ),
                2,
                "###,###,###.##",
                "USD"
        );

        String actualJson = new ObjectMapper().writeValueAsString(c);
        assertEquals("{\"headers\":"
                + "{\"donor-agency\":\"Donor Agency\",\"actual-disbursements\":\"Actual Disbursements\"},"
                + "\"totals\":{\"Total Actual Disbursements\":533325163.92},"
                + "\"data\":["
                + "{\"donor-agency\":\"Donor 1\",\"actual-disbursements\":\"533 202 040,49\"},"
                + "{\"donor-agency\":\"Donor 2\",\"actual-disbursements\":\"123 123,43\"}],"
                + "\"count\":2,"
                + "\"numberformat\":\"###,###,###.##\","
                + "\"Currency\":\"USD\"}", actualJson);
    }
}