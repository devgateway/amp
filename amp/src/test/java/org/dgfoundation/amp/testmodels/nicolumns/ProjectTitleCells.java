package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class ProjectTitleCells extends HardcodedCells<TextCell>{

	public ProjectTitleCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames, null);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity 2 with multiple agreements", "Activity 2 with multiple agreements"),
			cell("Activity Linked With Pledge", "Activity Linked With Pledge"),
			cell("Activity With Zones and Percentages", "Activity With Zones and Percentages"),
			cell("Activity with Zones", "Activity with Zones"),
			cell("Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms"),
			cell("Activity with planned disbursements", "Activity with planned disbursements"),
			cell("Activity with primary_tertiary_program", "Activity with primary_tertiary_program"),
			cell("Eth Water", "Eth Water"),
			cell("Project with documents", "Project with documents"),
			cell("Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD"),
			cell("Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR"),
			cell("Pure MTEF Project", "Pure MTEF Project"),
			cell("SSC Project 1", "SSC Project 1"),
			cell("SSC Project 2", "SSC Project 2"),
			cell("SubNational no percentages", "SubNational no percentages"),
			cell("TAC_activity_1", "TAC_activity_1"),
			cell("TAC_activity_2", "TAC_activity_2"),
			cell("Test MTEF directed", "Test MTEF directed"),
			cell("Unvalidated activity", "Unvalidated activity"),
			cell("activity 1 with agreement", "activity 1 with agreement"),
			cell("activity with capital spending", "activity with capital spending"),
			cell("activity with components", "activity with components"),
			cell("activity with contracting agency", "activity with contracting agency"),
			cell("activity with directed MTEFs", "activity with directed MTEFs"),
			cell("activity with funded components", "activity with funded components"),
			cell("activity with incomplete agreement", "activity with incomplete agreement"),
			cell("activity with many MTEFs", "activity with many MTEFs"),
			cell("activity with pipeline MTEFs and act. disb", "activity with pipeline MTEFs and act. disb"),
			cell("activity with primary_program", "activity with primary_program"),
			cell("activity with tertiary_program", "activity with tertiary_program"),
			cell("activity-with-unfunded-components", "activity-with-unfunded-components"),
			cell("activity_with_disaster_response", "activity_with_disaster_response"),
			cell("crazy funding 1", "crazy funding 1"),
			cell("date-filters-activity", "date-filters-activity"),
			cell("execution rate activity", "execution rate activity"),
			cell("mtef activity 1", "mtef activity 1"),
			cell("mtef activity 2", "mtef activity 2"),
			cell("new activity with contracting", "new activity with contracting"),
			cell("pledged 2", "pledged 2"),
			cell("pledged education activity 1", "pledged education activity 1"),
			cell("ptc activity 1", "ptc activity 1"),
			cell("ptc activity 2", "ptc activity 2"),
			cell("third activity with agreements", "third activity with agreements"),
			cell("with weird currencies", "with weird currencies")
);
	}

}
