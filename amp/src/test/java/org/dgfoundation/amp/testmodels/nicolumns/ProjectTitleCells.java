package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class ProjectTitleCells extends HardcodedCells<TextCell>{

	public ProjectTitleCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "Activity 2 with multiple agreements"),
			createCell("Activity Linked With Pledge", "Activity Linked With Pledge"),
			createCell("Activity With Zones and Percentages", "Activity With Zones and Percentages"),
			createCell("Activity with Zones", "Activity with Zones"),
			createCell("Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms"),
			createCell("Activity with planned disbursements", "Activity with planned disbursements"),
			createCell("Activity with primary_tertiary_program", "Activity with primary_tertiary_program"),
			createCell("Eth Water", "Eth Water"),
			createCell("Project with documents", "Project with documents"),
			createCell("Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD"),
			createCell("Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR"),
			createCell("Pure MTEF Project", "Pure MTEF Project"),
			createCell("SSC Project 1", "SSC Project 1"),
			createCell("SSC Project 2", "SSC Project 2"),
			createCell("SubNational no percentages", "SubNational no percentages"),
			createCell("TAC_activity_1", "TAC_activity_1"),
			createCell("TAC_activity_2", "TAC_activity_2"),
			createCell("Test MTEF directed", "Test MTEF directed"),
			createCell("Unvalidated activity", "Unvalidated activity"),
			createCell("activity 1 with agreement", "activity 1 with agreement"),
			createCell("activity with capital spending", "activity with capital spending"),
			createCell("activity with components", "activity with components"),
			createCell("activity with contracting agency", "activity with contracting agency"),
			createCell("activity with directed MTEFs", "activity with directed MTEFs"),
			createCell("activity with funded components", "activity with funded components"),
			createCell("activity with incomplete agreement", "activity with incomplete agreement"),
			createCell("activity with many MTEFs", "activity with many MTEFs"),
			createCell("activity with pipeline MTEFs and act. disb", "activity with pipeline MTEFs and act. disb"),
			createCell("activity with primary_program", "activity with primary_program"),
			createCell("activity with tertiary_program", "activity with tertiary_program"),
			createCell("activity-with-unfunded-components", "activity-with-unfunded-components"),
			createCell("activity_with_disaster_response", "activity_with_disaster_response"),
			createCell("crazy funding 1", "crazy funding 1"),
			createCell("date-filters-activity", "date-filters-activity"),
			createCell("execution rate activity", "execution rate activity"),
			createCell("mtef activity 1", "mtef activity 1"),
			createCell("mtef activity 2", "mtef activity 2"),
			createCell("new activity with contracting", "new activity with contracting"),
			createCell("pledged 2", "pledged 2"),
			createCell("pledged education activity 1", "pledged education activity 1"),
			createCell("ptc activity 1", "ptc activity 1"),
			createCell("ptc activity 2", "ptc activity 2"),
			createCell("third activity with agreements", "third activity with agreements"),
			createCell("with weird currencies", "with weird currencies")
);
	}

}
