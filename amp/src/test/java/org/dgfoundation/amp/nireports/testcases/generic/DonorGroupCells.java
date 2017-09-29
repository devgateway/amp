package org.dgfoundation.amp.nireports.testcases.generic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import org.dgfoundation.amp.nireports.TextCell;


public class DonorGroupCells extends HardcodedCells<TextCell>{

    public DonorGroupCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<TextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, 1),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity 2 with multiple agreements", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, 1),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity 2 with multiple agreements", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, 2),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity Linked With Pledge", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity with both MTEFs and Act.Comms", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with capital spending", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2124))),
            cell("activity with contracting agency", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with directed MTEFs", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21700),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with funded components", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with incomplete agreement", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, 3),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with many MTEFs", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with many MTEFs", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with pipeline MTEFs and act. disb", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity with planned disbursements", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity with planned disbursements", "European", 18,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21694),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with primary_program", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21697),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity with primary_tertiary_program", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, 2093),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with tertiary_program", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21700),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity with Zones", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity With Zones and Percentages", "European", 18,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21694),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity-weird-funding", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, 2095),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity-with-unfunded-components", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2124))),
            cell("activity_with_disaster_response", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("arrears test", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("crazy funding 1", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, 2093),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("crazy funding 1", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2124))),
            cell("date-filters-activity", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("department/division", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Eth Water", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Eth Water", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Eth Water", "European", 18,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21694),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("execution rate activity", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("expenditure class", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21700),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("mtef activity 1", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("mtef activity 2", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("new activity with contracting", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("pledged 2", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2124))),
            cell("pledged education activity 1", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("ptc activity 1", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, 2097),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("ptc activity 2", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, 2095),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Pure MTEF Project", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Real SSC Activity 1", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Real SSC Activity 1", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Real SSC Activity 1", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21697),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Real SSC Activity 2", "European", 18,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21694),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("second with disaster response", "European", 18,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21694),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("SSC Project 1", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("SSC Project 2", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21701),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("SubNational no percentages", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21700),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("TAC_activity_1", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21697),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("TAC_activity_2", "American", 19,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21702),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Test MTEF directed", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, 2093),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21700),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("third activity with agreements", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, 2),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Unvalidated activity", "International", 20,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("with annual ppc and actual comm", "Default Group", 17,
                coos(
                    entry("cats", "Mode of Payment", 1, -999999999),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("with weird currencies", "National", 21,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))));
    }

}
