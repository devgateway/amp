package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class PrimarySectorCells extends HardcodedCells<PercentageTextCell>{

	public PrimarySectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "110 - EDUCATION", 1.000000),
			createCell("Activity Linked With Pledge", "110 - EDUCATION", 1.000000),
			createCell("Activity With Zones and Percentages", "110 - EDUCATION", 0.300000),
			createCell("Activity With Zones and Percentages", "120 - HEALTH", 0.700000),
			createCell("Activity with Zones", "110 - EDUCATION", 1.000000),
			createCell("Activity with both MTEFs and Act.Comms", "110 - EDUCATION", 0.600000),
			createCell("Activity with both MTEFs and Act.Comms", "112 - BASIC EDUCATION", 0.400000),
			createCell("Activity with planned disbursements", "112 - BASIC EDUCATION", 1.000000),
			createCell("Activity with primary_tertiary_program", "110 - EDUCATION", 1.000000),
			createCell("Eth Water", "110 - EDUCATION", 1.000000),
			createCell("Project with documents", "110 - EDUCATION", 1.000000),
			createCell("Proposed Project Cost 1 - USD", "110 - EDUCATION", 1.000000),
			createCell("Proposed Project Cost 2 - EUR", "110 - EDUCATION", 1.000000),
			createCell("Pure MTEF Project", "110 - EDUCATION", 1.000000),
			createCell("SSC Project 1", "110 - EDUCATION", 1.000000),
			createCell("SSC Project 2", "112 - BASIC EDUCATION", 1.000000),
			createCell("SubNational no percentages", "110 - EDUCATION", 1.000000),
			createCell("TAC_activity_1", "112 - BASIC EDUCATION", 1.000000),
			createCell("TAC_activity_2", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", 1.000000),
			createCell("Test MTEF directed", "110 - EDUCATION", 1.000000),
			createCell("Unvalidated activity", "110 - EDUCATION", 1.000000),
			createCell("activity 1 with agreement", "110 - EDUCATION", 0.500000),
			createCell("activity 1 with agreement", "112 - BASIC EDUCATION", 0.500000),
			createCell("activity with capital spending", "110 - EDUCATION", 1.000000),
			createCell("activity with components", "110 - EDUCATION", 1.000000),
			createCell("activity with contracting agency", "110 - EDUCATION", 0.600000),
			createCell("activity with contracting agency", "112 - BASIC EDUCATION", 0.100000),
			createCell("activity with contracting agency", "120 - HEALTH", 0.300000),
			createCell("activity with directed MTEFs", "110 - EDUCATION", 1.000000),
			createCell("activity with funded components", "110 - EDUCATION", 1.000000),
			createCell("activity with incomplete agreement", "110 - EDUCATION", 1.000000),
			createCell("activity with many MTEFs", "110 - EDUCATION", 1.000000),
			createCell("activity with pipeline MTEFs and act. disb", "110 - EDUCATION", 1.000000),
			createCell("activity with primary_program", "110 - EDUCATION", 1.000000),
			createCell("activity with tertiary_program", "110 - EDUCATION", 1.000000),
			createCell("activity-with-unfunded-components", "110 - EDUCATION", 1.000000),
			createCell("activity_with_disaster_response", "110 - EDUCATION", 0.600000),
			createCell("activity_with_disaster_response", "113 - SECONDARY EDUCATION", 0.400000),
			createCell("crazy funding 1", "110 - EDUCATION", 1.000000),
			createCell("date-filters-activity", "110 - EDUCATION", 1.000000),
			createCell("execution rate activity", "110 - EDUCATION", 1.000000),
			createCell("mtef activity 1", "110 - EDUCATION", 1.000000),
			createCell("mtef activity 2", "110 - EDUCATION", 1.000000),
			createCell("pledged 2", "113 - SECONDARY EDUCATION", 1.000000),
			createCell("pledged education activity 1", "110 - EDUCATION", 1.000000),
			createCell("ptc activity 1", "110 - EDUCATION", 1.000000),
			createCell("ptc activity 2", "110 - EDUCATION", 1.000000),
			createCell("third activity with agreements", "110 - EDUCATION", 1.000000),
			createCell("with weird currencies", "110 - EDUCATION", 0.900000),
			createCell("with weird currencies", "112 - BASIC EDUCATION", 0.100000)
);
	}

}
