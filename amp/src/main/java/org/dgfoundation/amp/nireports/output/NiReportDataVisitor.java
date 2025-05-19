package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.newreports.ReportSpecification;

/**
 * a visitor for {@link NiReportData}, which is also disguising as a {@link NiReportOutputBuilder}
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public interface NiReportDataVisitor<K> extends NiReportOutputBuilder<K> {
    public K visit(NiColumnReportData crd);
    public K visit(NiGroupReportData grd);
    
    @Override
    public default K buildOutput(ReportSpecification spec, NiReportRunResult reportRun) {
        return reportRun.reportOut.accept(this);
    }
}
