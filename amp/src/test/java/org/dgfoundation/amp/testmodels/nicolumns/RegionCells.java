package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;


public class RegionCells extends HardcodedCells<PercentageTextCell> {

	public RegionCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity 2 with multiple agreements", "Chisinau County", 1.000000),
			cell("Activity Linked With Pledge", "Chisinau City", 1.000000),
			cell("Activity With Zones and Percentages", "Anenii Noi County", 0.200000),
			cell("Activity With Zones and Percentages", "Balti County", 0.800000),
			cell("Activity with Zones", "Anenii Noi County", 0.500000),
			cell("Activity with Zones", "Balti County", 0.500000),
			cell("Activity with both MTEFs and Act.Comms", "Balti County", 0.300000),
			cell("Activity with both MTEFs and Act.Comms", "Drochia County", 0.700000),
			cell("Activity with planned disbursements", "", 1.000000),
			cell("Activity with primary_tertiary_program", "", 1.000000),
			cell("Eth Water", "Anenii Noi County", 1.000000),
			cell("Project with documents", "Balti County", 1.000000),
			cell("Proposed Project Cost 1 - USD", "Drochia County", 1.000000),
			cell("Proposed Project Cost 2 - EUR", "Anenii Noi County", 1.000000),
			cell("Pure MTEF Project", "Cahul County", 1.000000),
			cell("SSC Project 1", "Anenii Noi County", 1.000000),
			cell("SSC Project 2", "Edinet County", 1.000000),
			cell("SubNational no percentages", "Anenii Noi County", 0.500000),
			cell("SubNational no percentages", "Balti County", 0.500000),
			cell("TAC_activity_1", "Dubasari County", 1.000000),
			cell("TAC_activity_2", "Falesti County", 1.000000),
			cell("Test MTEF directed", "Anenii Noi County", 1.000000),
			cell("Unvalidated activity", "", 1.000000),
			cell("activity 1 with agreement", "Balti County", 1.000000),
			cell("activity with capital spending", "Chisinau County", 1.000000),
			cell("activity with components", "Anenii Noi County", 1.000000),
			cell("activity with contracting agency", "Balti County", 0.550000),
			cell("activity with contracting agency", "Transnistrian Region", 0.450000),
			cell("activity with directed MTEFs", "Chisinau City", 1.000000),
			cell("activity with funded components", "", 1.000000),
			cell("activity with incomplete agreement", "", 1.000000),
			cell("activity with many MTEFs", "Drochia County", 1.000000),
			cell("activity with pipeline MTEFs and act. disb", "Chisinau County", 1.000000),
			cell("activity with primary_program", "", 1.000000),
			cell("activity with tertiary_program", "", 1.000000),
			cell("activity-with-unfunded-components", "Transnistrian Region", 1.000000),
			cell("activity_with_disaster_response", "", 1.000000),
			cell("crazy funding 1", "Balti County", 1.000000),
			cell("date-filters-activity", "", 1.000000),
			cell("execution rate activity", "Chisinau City", 0.500000),
			cell("execution rate activity", "Dubasari County", 0.500000),
			cell("mtef activity 1", "", 1.000000),
			cell("mtef activity 2", "Anenii Noi County", 1.000000),
			cell("pledged 2", "Cahul County", 1.000000),
			cell("pledged education activity 1", "Chisinau County", 1.000000),
			cell("ptc activity 1", "Anenii Noi County", 1.000000),
			cell("ptc activity 2", "Anenii Noi County", 1.000000),
			cell("third activity with agreements", "Chisinau City", 1.000000),
			cell("with weird currencies", "", 1.000000)
);
	}

}
