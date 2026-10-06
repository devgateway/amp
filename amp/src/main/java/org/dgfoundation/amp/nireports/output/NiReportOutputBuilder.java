package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.newreports.ReportSpecification;

/**
 * a generic interface for converting the result of running a NiReport into an arbitrary object
 * @author Dolghier Constantin
 *
 * @param <K> the type of the generated instances
 */
public interface NiReportOutputBuilder<K> {
    public K buildOutput(ReportSpecification spec, NiReportRunResult reportRun);
}
