package org.digijava.kernel.ampapi.endpoints.publicportal;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class PublicEndpointTest {

    @Test
    public void testTopDonorFundingSerialization() throws JsonProcessingException {
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
        PublicTopDonorFundingData donorFundingData =
                new PublicTopDonorFundingData(c);

        String actualJson = new ObjectMapper().writeValueAsString(donorFundingData);
        assertEquals("{\"headers\":"
                + "{\"donor-agency\":\"Donor Agency\",\"actual-disbursements\":\"Actual Disbursements\"},"
                + "\"totals\":{\"Total Actual Disbursements\":533325163.92},"
                + "\"count\":2,"
                + "\"donorFunding\":["
                + "{\"donor-agency\":\"Donor 1\",\"actual-disbursements\":\"533 202 040,49\"},"
                + "{\"donor-agency\":\"Donor 2\",\"actual-disbursements\":\"123 123,43\"}],"
                + "\"numberformat\":\"###,###,###.##\","
                + "\"Currency\":\"USD\"}", actualJson);
    }

    @Test
    public void testTopProjectsSerialization() throws JsonProcessingException {
        PublicTopData c = new PublicTopData(
                ImmutableMap.of("project-title", "Project Title", "actual-disbursements", "Actual Disbursements"),
                ImmutableMap.of(),
                ImmutableList.of(
                        ImmutableMap.of("project-title", "Prj 1", "actual-disbursements", "433 202 040,49"),
                        ImmutableMap.of("project-title", "Prj 2", "actual-disbursements", "23 123,43")
                ),
                2,
                "###,###,###.##",
                "USD"
        );
        PublicTopProjectsData topProjectsData =
                new PublicTopProjectsData(c);

        String actualJson = new ObjectMapper().writeValueAsString(topProjectsData);
        assertEquals("{\"headers\":"
                + "{\"project-title\":\"Project Title\",\"actual-disbursements\":\"Actual Disbursements\"},"
                + "\"totals\":{},"
                + "\"count\":2,"
                + "\"topprojects\":["
                + "{\"project-title\":\"Prj 1\",\"actual-disbursements\":\"433 202 040,49\"},"
                + "{\"project-title\":\"Prj 2\",\"actual-disbursements\":\"23 123,43\"}],"
                + "\"numberformat\":\"###,###,###.##\","
                + "\"Currency\":\"USD\"}", actualJson);
    }
}