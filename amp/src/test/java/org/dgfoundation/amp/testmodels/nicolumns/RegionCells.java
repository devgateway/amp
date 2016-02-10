package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class RegionCells extends HardcodedCells<PercentageTextCell>{

	public RegionCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "Chisinau County", 1.000000),
			createCell("Activity Linked With Pledge", "Chisinau City", 1.000000),
			createCell("Activity With Zones and Percentages", "Anenii Noi County", 0.200000),
			createCell("Activity With Zones and Percentages", "Balti County", 0.800000),
			createCell("Activity with Zones", "Anenii Noi County", 0.500000),
			createCell("Activity with Zones", "Balti County", 0.500000),
			createCell("Activity with both MTEFs and Act.Comms", "Balti County", 0.300000),
			createCell("Activity with both MTEFs and Act.Comms", "Drochia County", 0.700000),
			createCell("Activity with planned disbursements", "", 1.000000),
			createCell("Activity with primary_tertiary_program", "", 1.000000),
			createCell("Eth Water", "Anenii Noi County", 1.000000),
			createCell("Project with documents", "Balti County", 1.000000),
			createCell("Proposed Project Cost 1 - USD", "Drochia County", 1.000000),
			createCell("Proposed Project Cost 2 - EUR", "Anenii Noi County", 1.000000),
			createCell("Pure MTEF Project", "Cahul County", 1.000000),
			createCell("SSC Project 1", "Anenii Noi County", 1.000000),
			createCell("SSC Project 2", "Edinet County", 1.000000),
			createCell("SubNational no percentages", "Anenii Noi County", 0.500000),
			createCell("SubNational no percentages", "Balti County", 0.500000),
			createCell("TAC_activity_1", "Dubasari County", 1.000000),
			createCell("TAC_activity_2", "Falesti County", 1.000000),
			createCell("Test MTEF directed", "Anenii Noi County", 1.000000),
			createCell("Unvalidated activity", "", 1.000000),
			createCell("activity 1 with agreement", "Balti County", 1.000000),
			createCell("activity with capital spending", "Chisinau County", 1.000000),
			createCell("activity with components", "Anenii Noi County", 1.000000),
			createCell("activity with contracting agency", "Balti County", 0.550000),
			createCell("activity with contracting agency", "Transnistrian Region", 0.450000),
			createCell("activity with directed MTEFs", "Chisinau City", 1.000000),
			createCell("activity with funded components", "", 1.000000),
			createCell("activity with incomplete agreement", "", 1.000000),
			createCell("activity with many MTEFs", "Drochia County", 1.000000),
			createCell("activity with pipeline MTEFs and act. disb", "Chisinau County", 1.000000),
			createCell("activity with primary_program", "", 1.000000),
			createCell("activity with tertiary_program", "", 1.000000),
			createCell("activity-with-unfunded-components", "Transnistrian Region", 1.000000),
			createCell("activity_with_disaster_response", "", 1.000000),
			createCell("crazy funding 1", "Balti County", 1.000000),
			createCell("date-filters-activity", "", 1.000000),
			createCell("execution rate activity", "Chisinau City", 0.500000),
			createCell("execution rate activity", "Dubasari County", 0.500000),
			createCell("mtef activity 1", "", 1.000000),
			createCell("mtef activity 2", "Anenii Noi County", 1.000000),
			createCell("pledged 2", "Cahul County", 1.000000),
			createCell("pledged education activity 1", "Chisinau County", 1.000000),
			createCell("ptc activity 1", "Anenii Noi County", 1.000000),
			createCell("ptc activity 2", "Anenii Noi County", 1.000000),
			createCell("third activity with agreements", "Chisinau City", 1.000000),
			createCell("with weird currencies", "", 1.000000)
);
	}

}
