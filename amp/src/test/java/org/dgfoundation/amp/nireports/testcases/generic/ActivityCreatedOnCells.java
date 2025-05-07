package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ActivityCreatedOnCells extends HardcodedCells<DateCell>{

    public ActivityCreatedOnCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<DateCell> populateCells() {
        return  Arrays.asList(
        dateCell("activity 1 with agreement", "2015-03-22"),
        dateCell("Activity 2 with multiple agreements", "2015-03-22"),
        dateCell("Activity Linked With Pledge", "2014-03-27"),
        dateCell("Activity with both MTEFs and Act.Comms", "2015-08-06"),
        dateCell("activity with capital spending", "2014-11-21"),
        dateCell("activity with components", "2013-11-15"),
        dateCell("activity with contracting agency", "2014-11-26"),
        dateCell("activity with directed MTEFs", "2015-09-29"),
        dateCell("activity with funded components", "2014-12-16"),
        dateCell("activity with incomplete agreement", "2015-03-22"),
        dateCell("activity with many MTEFs", "2015-11-05"),
        dateCell("activity with pipeline MTEFs and act. disb", "2015-10-19"),
        dateCell("Activity with planned disbursements", "2015-04-10"),
        dateCell("activity with primary_program", "2014-03-28"),
        dateCell("Activity with primary_tertiary_program", "2014-03-28"),
        dateCell("activity with tertiary_program", "2014-03-28"),
        dateCell("Activity with Zones", "2013-12-23"),
        dateCell("Activity With Zones and Percentages", "2013-12-23"),
        dateCell("activity-weird-funding", "2016-05-05"),
        dateCell("activity-with-unfunded-components", "2014-12-15"),
        dateCell("activity_with_disaster_response", "2015-08-24"),
        dateCell("arrears test", "2016-04-27"),
        dateCell("crazy funding 1", "2013-12-20"),
        dateCell("date-filters-activity", "2013-09-21"),
        dateCell("department/division", "2016-06-10"),
        dateCell("Eth Water", "2013-08-01"),
        dateCell("execution rate activity", "2015-10-19"),
        dateCell("expenditure class", "2016-05-02"),
        dateCell("mtef activity 1", "2013-08-05"),
        dateCell("mtef activity 2", "2013-08-05"),
        dateCell("new activity with contracting", "2014-11-26"),
        dateCell("PID: original", "2016-04-27"),
        dateCell("PID: original > actual", "2016-04-27"),
        dateCell("PID: original, actual", "2016-04-27"),
        dateCell("PID: original, proposed", "2016-04-27"),
        dateCell("PID: original, proposed, actual", "2016-04-27"),
        dateCell("pledged 2", "2014-04-29"),
        dateCell("pledged education activity 1", "2014-04-29"),
        dateCell("Project with documents", "2013-11-18"),
        dateCell("Proposed Project Cost 1 - USD", "2013-10-01"),
        dateCell("Proposed Project Cost 2 - EUR", "2013-10-01"),
        dateCell("ptc activity 1", "2013-08-19"),
        dateCell("ptc activity 2", "2013-08-19"),
        dateCell("Pure MTEF Project", "2013-10-11"),
        dateCell("Real SSC Activity 1", "2014-02-20"),
        dateCell("Real SSC Activity 2", "2014-02-20"),
        dateCell("second with disaster response", "2016-06-28"),
        dateCell("SSC Project 1", "2013-08-20"),
        dateCell("SSC Project 2", "2013-08-20"),
        dateCell("SubNational no percentages", "2014-02-21"),
        dateCell("TAC_activity_1", "2013-08-23"),
        dateCell("TAC_activity_2", "2013-08-23"),
        dateCell("Test MTEF directed", "2013-10-10"),
        dateCell("third activity with agreements", "2015-03-22"),
        dateCell("Unvalidated activity", "2015-01-25"),
        dateCell("with annual ppc and actual comm", "2016-08-05"),
        dateCell("with weird currencies", "2015-12-15")
);
    }

}
