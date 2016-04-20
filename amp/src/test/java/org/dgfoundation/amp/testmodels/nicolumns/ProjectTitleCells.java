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
			cell("activity 1 with agreement", "activity 1 with agreement", 65),
			cell("Activity 2 with multiple agreements", "Activity 2 with multiple agreements", 66),
			cell("Activity Linked With Pledge", "Activity Linked With Pledge", 41),
			cell("Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", 70),
			cell("activity with capital spending", "activity with capital spending", 50),
			cell("activity with components", "activity with components", 21),
			cell("activity with contracting agency", "activity with contracting agency", 52),
			cell("activity with directed MTEFs", "activity with directed MTEFs", 73),
			cell("activity with funded components", "activity with funded components", 63),
			cell("activity with incomplete agreement", "activity with incomplete agreement", 68),
			cell("activity with many MTEFs", "activity with many MTEFs", 78),
			cell("activity with pipeline MTEFs and act. disb", "activity with pipeline MTEFs and act. disb", 76),
			cell("Activity with planned disbursements", "Activity with planned disbursements", 69),
			cell("activity with primary_program", "activity with primary_program", 44),
			cell("Activity with primary_tertiary_program", "Activity with primary_tertiary_program", 43),
			cell("activity with tertiary_program", "activity with tertiary_program", 45),
			cell("Activity with Zones", "Activity with Zones", 33),
			cell("Activity With Zones and Percentages", "Activity With Zones and Percentages", 36),
			cell("activity-with-unfunded-components", "activity-with-unfunded-components", 61),
			cell("activity_with_disaster_response", "activity_with_disaster_response", 71),
			cell("crazy funding 1", "crazy funding 1", 32),
			cell("date-filters-activity", "date-filters-activity", 26),
			cell("Eth Water", "Eth Water", 24),
			cell("execution rate activity", "execution rate activity", 77),
			cell("mtef activity 1", "mtef activity 1", 25),
			cell("mtef activity 2", "mtef activity 2", 27),
			cell("new activity with contracting", "new activity with contracting", 53),
			cell("pledged 2", "pledged 2", 48),
			cell("pledged education activity 1", "pledged education activity 1", 46),
			cell("Project with documents", "Project with documents", 23),
			cell("Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD", 15),
			cell("Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR", 17),
			cell("ptc activity 1", "ptc activity 1", 28),
			cell("ptc activity 2", "ptc activity 2", 29),
			cell("Pure MTEF Project", "Pure MTEF Project", 19),
			cell("SSC Project 1", "SSC Project 1", 30),
			cell("SSC Project 2", "SSC Project 2", 31),
			cell("SubNational no percentages", "SubNational no percentages", 40),
			cell("TAC_activity_1", "TAC_activity_1", 12),
			cell("TAC_activity_2", "TAC_activity_2", 13),
			cell("Test MTEF directed", "Test MTEF directed", 18),
			cell("third activity with agreements", "third activity with agreements", 67),
			cell("Unvalidated activity", "Unvalidated activity", 64),
			cell("with weird currencies", "with weird currencies", 79)
);
	}

}