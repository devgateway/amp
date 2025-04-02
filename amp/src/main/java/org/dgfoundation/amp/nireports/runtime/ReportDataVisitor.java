package org.dgfoundation.amp.nireports.runtime;

/**
 * an interface used for recursively visiting a {@link ReportData}
 * @author Dolghier Constantin
 *
 * @param <K> the output type 
 */
public interface ReportDataVisitor<K> {
    public K visitLeaf(ColumnReportData crd);
    public K visitGroup(GroupReportData grd);
}
