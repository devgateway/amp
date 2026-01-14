package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportWarning;
import org.dgfoundation.amp.nireports.NiHeaderInfo;
import org.dgfoundation.amp.nireports.NiReportsEngine;

import java.util.SortedMap;
import java.util.SortedSet;

/**
 * a container for the artefacts of running a report in {@link NiReportsEngine}
 * @author Dolghier Constantin
 *
 */
public class NiReportRunResult {
    public final NiReportData reportOut;
    public final RunNode timings;
    public final NiHeaderInfo headers;
    public final long wallclockTime;
    public final SortedMap<Long, SortedSet<ReportWarning>> warnings; 
    public final CalendarConverter calendar;
    
    public NiReportRunResult(NiReportData reportOut, RunNode timings, long wallclockTime, NiHeaderInfo headers, 
            SortedMap<Long, SortedSet<ReportWarning>> warnings, CalendarConverter calendar) {
        this.reportOut = reportOut;
        this.timings = timings;
        this.wallclockTime = wallclockTime;
        this.headers = headers;
        this.warnings = warnings;
        this.calendar = calendar;
    }
}
