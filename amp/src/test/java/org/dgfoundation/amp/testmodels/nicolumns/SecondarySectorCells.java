package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class SecondarySectorCells extends HardcodedCells<PercentageTextCell>{

	public SecondarySectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Activity Linked With Pledge", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Activity With Zones and Percentages", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Activity with Zones", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Activity with both MTEFs and Act.Comms", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Activity with planned disbursements", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Activity with primary_tertiary_program", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Eth Water", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Project with documents", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Proposed Project Cost 1 - USD", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Proposed Project Cost 2 - EUR", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Pure MTEF Project", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("SSC Project 1", "02 TRANSDNISTRIAN CONFLICT", 1.000000),
			createCell("SSC Project 2", "3 NATIONAL COMPETITIVENESS", 1.000000),
			createCell("SubNational no percentages", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("TAC_activity_1", "3 NATIONAL COMPETITIVENESS", 1.000000),
			createCell("TAC_activity_2", "5 REGIONAL DEVELOPMENT", 1.000000),
			createCell("Test MTEF directed", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("Unvalidated activity", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity 1 with agreement", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity with capital spending", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity with components", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity with contracting agency", "5 REGIONAL DEVELOPMENT", 1.000000),
			createCell("activity with directed MTEFs", "1-DEMOCRATIC COUNTRY", 0.500000),
			createCell("activity with directed MTEFs", "4 HUMAN RESOURCES", 0.500000),
			createCell("activity with funded components", "5 REGIONAL DEVELOPMENT", 1.000000),
			createCell("activity with incomplete agreement", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity with many MTEFs", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity with pipeline MTEFs and act. disb", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity with primary_program", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity with tertiary_program", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity-with-unfunded-components", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("activity_with_disaster_response", "3 NATIONAL COMPETITIVENESS", 1.000000),
			createCell("crazy funding 1", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("date-filters-activity", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("execution rate activity", "3 NATIONAL COMPETITIVENESS", 1.000000),
			createCell("mtef activity 1", "02 TRANSDNISTRIAN CONFLICT", 1.000000),
			createCell("mtef activity 2", "02 TRANSDNISTRIAN CONFLICT", 1.000000),
			createCell("pledged 2", "4 HUMAN RESOURCES", 1.000000),
			createCell("pledged education activity 1", "4 HUMAN RESOURCES", 1.000000),
			createCell("ptc activity 1", "02 TRANSDNISTRIAN CONFLICT", 1.000000),
			createCell("ptc activity 2", "02 TRANSDNISTRIAN CONFLICT", 1.000000),
			createCell("third activity with agreements", "1-DEMOCRATIC COUNTRY", 1.000000),
			createCell("with weird currencies", "1-DEMOCRATIC COUNTRY", 1.000000)
);
	}

}
