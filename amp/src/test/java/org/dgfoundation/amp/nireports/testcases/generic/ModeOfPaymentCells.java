package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ModeOfPaymentCells extends HardcodedCells<TextCell>{

    public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<TextCell> populateCells() {
        return  Arrays.asList(
            cell("Activity with both MTEFs and Act.Comms", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with directed MTEFs", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with directed MTEFs", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21700),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity with directed MTEFs", "Non-Cash", 2096,
                coos(
                    entry("cats", "Mode of Payment", 1, 2096),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2124))),
            cell("activity with pipeline MTEFs and act. disb", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Activity with primary_tertiary_program", "Cash", 2093,
                coos(
                    entry("cats", "Mode of Payment", 1, 2093),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity-weird-funding", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21701),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity-weird-funding", "No Information", 2095,
                coos(
                    entry("cats", "Mode of Payment", 1, 2095),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity-weird-funding", "Non-Cash", 2096,
                coos(
                    entry("cats", "Mode of Payment", 1, 2096),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21695),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("activity_with_disaster_response", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("crazy funding 1", "Cash", 2093,
                coos(
                    entry("cats", "Mode of Payment", 1, 2093),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("crazy funding 1", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2124))),
            cell("Eth Water", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2124))),
            cell("mtef activity 2", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("ptc activity 1", "Reimbursable", 2097,
                coos(
                    entry("cats", "Mode of Payment", 1, 2097),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("ptc activity 2", "No Information", 2095,
                coos(
                    entry("cats", "Mode of Payment", 1, 2095),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21696),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("SSC Project 1", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21698),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("SSC Project 2", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21701),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("SubNational no percentages", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21700),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("Test MTEF directed", "Cash", 2093,
                coos(
                    entry("cats", "Mode of Payment", 1, 2093),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21700),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2120),
                    entry("cats", "Type Of Assistance", 1, 2119))),
            cell("with weird currencies", "Direct payment", 2094,
                coos(
                    entry("cats", "Mode of Payment", 1, 2094),
                    entry("agrs", "agr", 0, -999999999),
                    entry("orgs", "DN", 2, 21699),
                    entry("cats", "Funding Status", 1, -999999999),
                    entry("cats", "Financing Instrument", 1, 2125),
                    entry("cats", "Type Of Assistance", 1, 2119))));
    }

}
