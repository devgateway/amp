package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.newreports.ReportWarning;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * @author Octavian Ciubotaru
 */
public class NiReportFilterResult {

    private Set<Long> activityIds;
    private RunNode timings;
    private long wallClockTime;
    private SortedMap<Long, SortedSet<ReportWarning>> warnings;

    public NiReportFilterResult(Set<Long> activityIds,
            RunNode timings, long wallClockTime,
            SortedMap<Long, SortedSet<ReportWarning>> warnings) {
        this.activityIds = activityIds;
        this.timings = timings;
        this.wallClockTime = wallClockTime;
        this.warnings = warnings;
    }

    public Set<Long> getActivityIds() {
        return activityIds;
    }

    public SortedMap<Long, SortedSet<ReportWarning>> getWarnings() {
        return warnings;
    }

    public long getWallClockTime() {
        return wallClockTime;
    }

    public RunNode getTimings() {
        return timings;
    }

    @Override
    public String toString() {
        return "NiReportFilterResult{"
                + "activityIds.size()=" + activityIds.size()
                + ", warnings=" + warnings
                + "}";
    }
}
